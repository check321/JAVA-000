package net.check321.springcontextdemo.beanloader;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
* @title BeanDefinition 装配
* @description
* @author fyang
* @date 2020/11/18 11:44 上午
*/
public class BeanDefinitionLoader {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AnnotationBeanLoader.class); // 向IOC容器注入配置类
        ctx.refresh(); // 启动IOC

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Foo.class);
        builder.addPropertyValue("id",700);
        builder.addPropertyValue("ref","bean-definition");

        // 通过BeanDefinition注册
        ctx.registerBeanDefinition("fooG",builder.getBeanDefinition());
        Foo fooG = ctx.getBean("fooG",Foo.class);

        System.out.println(fooG.toString());
    }

}
