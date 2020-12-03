package net.check321.databasedemo.aspect;

import lombok.extern.slf4j.Slf4j;
import net.check321.databasedemo.conf.DataSourceNode;
import net.check321.databasedemo.conf.DynamicDataSource;
import net.check321.databasedemo.util.DynamicDataSourceCtxHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
* @title 动态数据源切面
* @description
* @author fyang
* @date 2020/12/3 10:13 下午
*/
@Aspect
@Component
@Slf4j
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(net.check321.databasedemo.annotation.ReadOnly)")
    public void dataSourcePointCut(){
    }

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

}
