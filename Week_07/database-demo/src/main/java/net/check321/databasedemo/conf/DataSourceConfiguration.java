package net.check321.databasedemo.conf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
