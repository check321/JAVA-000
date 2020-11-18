package net.check321.springbootstarterdemo.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
* @title JDBC操作数据库
* @description
* @author fyang
* @date 2020/11/18 2:30 下午
*/
@Slf4j
public class RawJDBCDemo {

    private static final String URL = "***";

    private static final String U_NAME = "**";

    private static final String PWD = "**";

    public static void main(String[] args)  {

        try(Connection con = DriverManager.getConnection(URL,U_NAME,PWD)){
            try(Statement statement = con.createStatement()){
                try(ResultSet rs = statement.executeQuery("select id , sid,tax_code from t_core_task_record where id < 20")){
                    while (rs.next()){
                        long id = rs.getLong(1);
                        String sid = rs.getString(2);
                        String tax_code = rs.getString(3);

                        log.info("id: {}, sid: {}, tax_code: {}",id,sid,tax_code);
                    }
                }
            }
        }catch (Exception e){
            log.error("jdbc error: ",e);
        }
    }
}
