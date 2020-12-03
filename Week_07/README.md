# Week_07_Database

## 1、(必做)按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。


1. 利用JDBC`PreparedStatement.addBatch()`将100w分批次插入，减少数据库IO开销及每次执行优化的次数。
2. 插入时去除相应字段索引，减少每次插入索引计算时间。

## 2、读写分离-动态切换数据源版本1.0

### 基于 Spring/Spring Boot，配置多个数据源(例如2个，master 和 slave)

#### 一主一从搭建
- 主库
```
mysql> show master status;
```

```
+------------------------+----------+--------------+------------------+-------------------+
| File                   | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------------+----------+--------------+------------------+-------------------+
| local-mysql-bin.000001 | 38953693 |              | mysql            |                   |
+------------------------+----------+--------------+------------------+-------------------+
```

- 从库
> mysql -h 127.0.0.1 -P 4406 -u root
> change master to master_host='172.17.0.2',master_user='replication',master_port=3306,master_password='123456',master_log_file='local-mysql-bin.000001', master_log_pos=38953693, master_connect_retry=30;

| 参数      |    注释 |   
| :-------- | --------:| 
| master_host    |  主库IP |  
| master_port    |  主库端口 |  
| master_user    |  主库授权同步账号 |  
| master_password    |  主库授权同步密码 |  
| master_log_file    |  主库binlog |  
| master_log_pos    |  binlog同步位置|  

- 查看从库状态
> mysql> show slave status \G; 

```
*************************** 1. row ***************************
               Slave_IO_State: Waiting for master to send event
                  Master_Host: 172.17.0.2
                  Master_User: replication
                  Master_Port: 3306
                Connect_Retry: 30
              Master_Log_File: local-mysql-bin.000001
          Read_Master_Log_Pos: 38953693
               Relay_Log_File: local-mysql-relay-bin.000002
                Relay_Log_Pos: 326
        Relay_Master_Log_File: local-mysql-bin.000001
             Slave_IO_Running: Yes
            Slave_SQL_Running: Yes
              Replicate_Do_DB:
          Replicate_Ignore_DB:
           Replicate_Do_Table:
       Replicate_Ignore_Table:
      Replicate_Wild_Do_Table:
  Replicate_Wild_Ignore_Table:
                   Last_Errno: 0
                   Last_Error:
                 Skip_Counter: 0
          Exec_Master_Log_Pos: 38953693
              Relay_Log_Space: 539
              Until_Condition: None
               Until_Log_File:
                Until_Log_Pos: 0
           Master_SSL_Allowed: No
           Master_SSL_CA_File:
           Master_SSL_CA_Path:
              Master_SSL_Cert:
            Master_SSL_Cipher:
               Master_SSL_Key:
        Seconds_Behind_Master: 0
Master_SSL_Verify_Server_Cert: No
                Last_IO_Errno: 0
                Last_IO_Error:
               Last_SQL_Errno: 0
               Last_SQL_Error:
  Replicate_Ignore_Server_Ids:
             Master_Server_Id: 100
                  Master_UUID: 87f95dab-2f32-11eb-b78d-0242ac110002
             Master_Info_File: /var/lib/mysql/master.info
                    SQL_Delay: 0
          SQL_Remaining_Delay: NULL
      Slave_SQL_Running_State: Slave has read all relay log; waiting for more updates
           Master_Retry_Count: 86400
                  Master_Bind:
      Last_IO_Error_Timestamp:
     Last_SQL_Error_Timestamp:
               Master_SSL_Crl:
           Master_SSL_Crlpath:
           Retrieved_Gtid_Set:
            Executed_Gtid_Set:
                Auto_Position: 0
         Replicate_Rewrite_DB:
                 Channel_Name:
           Master_TLS_Version:
```

```
Slave_IO_Running: Yes
Slave_SQL_Running: Yes
从库就绪
```

- 主库执行作业1的插入100w条数据
> 01:38:21.363 [main] INFO net.check321.databasedemo.jdbc.BatchInsertByJDBC - total insert [1000000] records.
> 01:38:21.369 [main] INFO net.check321.databasedemo.jdbc.BatchInsertByJDBC - total spend: [10550] ms 


- 从库显示成功同步100w条数据


#### 动态切分主从库

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── net
│   │   │       └── check321
│   │   │           └── databasedemo
│   │   │               ├── DatabaseDemoApplication.java
│   │   │               ├── annotation
│   │   │               │   └── ReadOnly.java [注解标记只读操作切分到从库]
│   │   │               ├── aspect
│   │   │               │   └── DynamicDataSourceAspect.java [AOP降低对业务代码的侵入]
│   │   │               ├── conf
│   │   │               │   ├── DataSourceConfiguration.java [主从库数据源配置]
│   │   │               │   ├── DataSourceNode.java [主从库标记]
│   │   │               │   └── DynamicDataSource.java 
│   │   │               ├── entity
│   │   │               │   └── User.java [测试业务实体对象]
│   │   │               ├── jdbc
│   │   │               │   └── BatchInsertByJDBC.java [作业1：批量插入100w条数据 测试约10s]
│   │   │               ├── mapper
│   │   │               │   └── UserMapper.java [实体Mapper]
│   │   │               └── util
│   │   │                   └── DynamicDataSourceCtxHolder.java [当前线程数据容器]
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java
│           └── net
│               └── check321
│                   └── databasedemo
│                       ├── DatabaseDemoApplicationTests.java
│                       └── DynamicSourceNodeTest.java [动态数据源测试]
```

#####  核心实现

- 继承`AbstractRoutingDataSource`通过`determineCurrentLookupKey()`实现数据源选取策略
```
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceCtxHolder.getDatasourceNode();
    }
}
```

- 配置主从数据源集合并注册于`DataSource`

```
@Configuration
@MapperScan(basePackages = "net.check321.databasedemo.mapper" )
public class DataSourceConfiguration {

    @Bean(DataSourceNode.MASTER_NODE)
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterNode(){
        return DataSourceBuilder.create().build();
    }

    @Bean(DataSourceNode.SLAVE_NODE)
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveNode(){
        return DataSourceBuilder.create().build();
    }

   @Bean
   @Primary
   public DataSource dynamicDataSource(){

       Map<Object, Object> nodeMap = new HashMap<>();
       nodeMap.put(DataSourceNode.MASTER_NODE,masterNode());
       nodeMap.put(DataSourceNode.SLAVE_NODE,slaveNode());

       DynamicDataSource dynamicDataSource = new DynamicDataSource();
       dynamicDataSource.setTargetDataSources(nodeMap);
       dynamicDataSource.setDefaultTargetDataSource(masterNode());

       return dynamicDataSource;
   }

}
```

- 自定义注解`@ReadOnly`标识只读操作

 

```
	@ReadOnly
    @Results({
            @Result(property = "userCode",column = "user_code"),
            @Result(property = "userName",column = "user_name")
    })
    @Select("select * from t_user where id = #{id}")
    User getById(@Param("id") Long id);
```

- 切面于`@ReadOnly`作为切点并利用线程数据容器切换数据源标识

```
   @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        // @ReadOnly 读操作走从库
        DynamicDataSourceCtxHolder.setDatasourceNode(DataSourceNode.SLAVE_NODE);
        try {
            return jp.proceed();
        }finally {
            DynamicDataSourceCtxHolder.clean();
        }

    }
```

- 测试
  1. 插入数据`sharding_test_02`后，观察自动同步至从库主键`1000003`
  2. 修改从库`1000003`数据至`sharding_test_02_slave`
  3. 查询`1000003`数据：`sharding_test_02_slave`，动态切分成功

```
INFO 50341 --- [           main] n.c.databasedemo.DynamicSourceNodeTest   : query record: User(id=1000003, userCode=sharding_02_slave, userName=sharding_test_02, password=123456)
```