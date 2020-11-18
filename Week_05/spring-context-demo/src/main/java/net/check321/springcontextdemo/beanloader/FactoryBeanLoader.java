package net.check321.springcontextdemo.beanloader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* @title FactoryBean 装配
* @description
* @author fyang
* @date 2020/11/18 1:45 上午
*/
public class FactoryBeanLoader {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext xmlCtx
                = new ClassPathXmlApplicationContext("application-context.xml");

        Foo fooD = xmlCtx.getBean("fooD", Foo.class);
        System.out.println(fooD.toString());
    }
}
