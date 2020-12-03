package net.check321.databasedemo.conf;

import net.check321.databasedemo.util.DynamicDataSourceCtxHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/** 
* @title 根据当前线程上下文标记动态切换数据源
* @description 
* @author fyang 
* @date 2020/12/3 9:52 下午
*/ 
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceCtxHolder.getDatasourceNode();
    }
}
