# Week_05_Spring

## 写代码实现Spring Bean的装配，方式越多越好

```
│   │   ├── java
│   │   │   └── net
│   │   │       └── check321
│   │   │           └── springcontextdemo
│   │   │               ├── SpringContextDemoApplication.java
│   │   │               └── beanloader
│   │   │                   ├── AnnotationBeanLoader.java [注解实现]
│   │   │                   ├── BeanDefinitionLoader.java [BeanDefinition实现]
│   │   │                   ├── BeanFactoryLoader.java [Bean工厂实现]
│   │   │                   ├── BeanServiceLoader.java [ServiceLoader实现]
│   │   │                   ├── FactoryBeanLoader.java [FactoryBean实现]
│   │   │                   ├── Foo.java [目标Bean]
│   │   │                   ├── XMLBeanLoader.java [XML构造实现]
│   │   │                   └── factory [各种工厂]
│   │   │                       ├── DefaultFooFactory.java
│   │   │                       ├── FooFactory.java
│   │   │                       ├── FooFactoryBean.java
│   │   │                       └── ServiceLoaderFooFactory.java
│   │   └── resources
│   │       ├── META-INF
│   │       │   └── services
│   │       │       └── net.check321.springcontextdemo.beanloader.factory.FooFactory [serviceloader支持]
```


##  实现自动配置和 Starter

### springboot-starter-demo

> 做为jar包配合AutoConfiguration为依赖方提供自动装配目标类Foo的能力

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── net
│   │   │       └── check321
│   │   │           └── springbootstarterdemo
│   │   │               ├── Foo.java // 测试目标类
│   │   │               ├── SpringbootStarterDemoApplication.java
│   │   │               ├── config
│   │   │               │   └── FooAutoConfiguration.java // 自动配置
│   │   └── resources
│   │       ├── META-INF
│   │       │   └── spring.factories // SPI配置
```

- 依赖工程引入Starter

```
<dependency>
        <groupId>net.check321</groupId>
        <artifactId>foo-demo-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

- 依赖注入

```java
    @Component
@Slf4j
public class FooInitializer {

    private final Foo foo;

    public FooInitializer(Foo foo) {
        this.foo = foo;
    }

    @PostConstruct
    public void init(){
        log.info("load bean via springboot starter: [{}]",foo.toString());
    }
}
```

- Foo装配成功

```
2020-11-18 13:29:21.693  INFO 79521 --- [  restartedMain] net.smarttax.sim.demo.FooInitializer     : load bean via springboot starter: [Foo{id=900, ref='springboot-starter'}]
```

## 研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法:
### SQL执行
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── net
│   │   │       └── check321
│   │   │           └── springbootstarterdemo
│   │   │               ├── SpringbootStarterDemoApplication.java
│   │   │               ├── config
│   │   │               │   ├── DataSource.java // hikari连接池配置
│   │   │               └── jdbc
│   │   │                   ├── RawJDBCDemo.java // JDBC实现
│   │   │                   └── TransactionJDBCDemo.java // JDBC事务实现
```

- 通过jdbc地址获取`java.sql.Connection`
- 从connection获取`java.sql.Statement` /  利用占位符防止sql注入的 `java.sql.PreparedStatement`
- `statement.executeQuery`执行sql语句并通过`java.sql.ResultSet`获取结果：

```
15:14:16.635 [main] INFO net.check321.springbootstarterdemo.jdbc.RawJDBCDemo - id: 11, sid: DZFPQZDFXJ100120200805w1ebc41ac, tax_code: 110109500321655
15:14:16.639 [main] INFO net.check321.springbootstarterdemo.jdbc.RawJDBCDemo - id: 12, sid: DZFPQZDFXJ100420200805wuohm6ctq, tax_code: 110109500321655
15:14:16.639 [main] INFO net.check321.springbootstarterdemo.jdbc.RawJDBCDemo - id: 13, sid: DZFPQZDFXJ1004202008056h3ae3g7d, tax_code: 110109500321655
15:14:16.639 [main] INFO net.check321.springbootstarterdemo.jdbc.RawJDBCDemo - id: 14, sid: DZFPQZDFXJ100820200806z40fqc0nu, tax_code: 110109500321655
```

### 事务实现

```java
public static void main(String[] args)  {
    try(Connection conn = DataSource.getConnection()){
        try(PreparedStatement statement = conn.prepareStatement("insert into t_core_task_record (pre_sid,sid) values ( ? , ?)")){

            conn.setAutoCommit(false); // 关闭自动提交
            statement.setString(1,"aaa");
            statement.setString(2,"AAA");

             statement.execute();
            // 模拟业务异常
           int a = 1 / 0;

            statement.setString(1,"bbb");
            statement.setString(2,"BBB");
            statement.execute();

        }catch (Exception e){
            // rollback for exception.
            conn.rollback();
            log.info("rollback for exception: [{}]",e.getClass());
        }finally {
            conn.setAutoCommit(true); // 恢复自动提交
        }
    }catch (Exception e){
        log.error("jdbc error: ",e);
    }
}
```

-  `conn.setAutoCommit(false);` 关闭sql自动提交
-  执行多个SQL操作，并过程中模拟业务异常
-  在异常catch中通过 `conn.rollback();`回滚事务
-  最后恢复连接自动提交 `conn.setAutoCommit(true);` 
- 执行结果： 因为除0异常回滚了事务，在此之前的插入记录没有插入

```
16:25:57.611 [main] INFO net.check321.springbootstarterdemo.jdbc.TransactionJDBCDemo - rollback for exception: [class java.lang.ArithmeticException]
```






