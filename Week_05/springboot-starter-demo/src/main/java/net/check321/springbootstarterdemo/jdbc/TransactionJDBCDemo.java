package net.check321.springbootstarterdemo.jdbc;

import lombok.extern.slf4j.Slf4j;
import net.check321.springbootstarterdemo.config.DataSource;

import java.sql.*;

/**
* @title JDBC 事务支持
* @description Datasource - HikariCP
* @author fyang
* @date 2020/11/18 3:18 下午
*/
@Slf4j
public class TransactionJDBCDemo {

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
}
