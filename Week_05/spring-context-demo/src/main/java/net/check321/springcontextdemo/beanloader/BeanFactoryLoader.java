package net.check321.springcontextdemo.beanloader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* @title 实例Bean工厂装配
* @description
* @author fyang
* @date 2020/11/18 1:38 上午
*/
public class BeanFactoryLoader {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext xmlCtx
                = new ClassPathXmlApplicationContext("application-context.xml");

        Foo fooC = xmlCtx.getBean("fooC", Foo.class);
        System.out.println(fooC.toString());

    }
}
