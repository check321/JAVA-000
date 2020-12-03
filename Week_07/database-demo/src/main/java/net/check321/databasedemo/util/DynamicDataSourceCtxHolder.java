package net.check321.databasedemo.util;

import net.check321.databasedemo.conf.DataSourceNode;

import java.util.Optional;

public class DynamicDataSourceCtxHolder {

    private static final ThreadLocal<String> DATASOURCE_CTX_HOLDER = new ThreadLocal<>();

    public static void setDatasourceNode(String key){
        DATASOURCE_CTX_HOLDER.set(key);
    }

    public static String getDatasourceNode(){
       return Optional.ofNullable(DATASOURCE_CTX_HOLDER.get()).orElse(DataSourceNode.MASTER_NODE);
    }


    public static void clean(){
        DATASOURCE_CTX_HOLDER.remove();
    }
}
