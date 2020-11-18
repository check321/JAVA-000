package net.check321.springcontextdemo.beanloader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* @title Bean装配--XML版本
* @description
* @author fyang
* @date 2020/11/17 11:42 下午
*/
public class XMLBeanLoader {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext xmlCtx
                = new ClassPathXmlApplicationContext("application-context.xml");

        Foo fooA = (Foo) xmlCtx.getBean("fooA");
        Foo fooB = xmlCtx.getBean("fooB",Foo.class);

        System.out.println(fooA.toString() + " " + fooB.toString());
    }
}
