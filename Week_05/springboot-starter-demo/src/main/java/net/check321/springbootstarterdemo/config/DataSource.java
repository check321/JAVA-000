package net.check321.springbootstarterdemo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
* @title 连接池 - Hikari实现
* @description
* @author fyang
* @date 2020/11/18 4:18 下午
*/
public class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl( "jdbc:mysql://rm-uf6dyv7171b279m8ogo.mysql.rds.aliyuncs.com:3306/tax_core?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai" );
        config.setUsername( "tax_inter" );
        config.setPassword( "A7#0l@10I" );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
