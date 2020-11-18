package net.check321.springcontextdemo.beanloader;

import net.check321.springcontextdemo.beanloader.factory.FooFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
* @title ServiceLoader装配
* @description
* @author fyang
* @date 2020/11/18 2:09 上午
*/
public class BeanServiceLoader {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext xmlCtx
                = new ClassPathXmlApplicationContext("application-context.xml");

        ServiceLoader<FooFactory> fooE = xmlCtx.getBean("fooE", ServiceLoader.class);
        final Iterator<FooFactory> iterator = fooE.iterator();
        while (iterator.hasNext()){
            FooFactory factory = iterator.next();
            System.out.println(factory.create("service-loader"));
        }
    }
}
