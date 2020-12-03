package net.check321.databasedemo.annotation;

import net.check321.databasedemo.conf.DataSourceNode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {

    String dataSourceNode() default DataSourceNode.MASTER_NODE;

}
