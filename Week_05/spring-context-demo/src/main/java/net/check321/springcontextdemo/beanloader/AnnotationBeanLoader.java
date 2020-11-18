package net.check321.springcontextdemo.beanloader;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
/**
* @title BeanDefinitionLoader
* @description 1. 导入BeanConfig
 *             2. 注册 BeanDefinitionLoader
* @author fyang
* @date 2020/11/18 10:52 上午
*/
@Import(AnnotationBeanLoader.FooConfig.class)
public class AnnotationBeanLoader {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AnnotationBeanLoader.class); // 向IOC容器注入配置类
        ctx.refresh(); // 启动IOC

        Foo fooF = ctx.getBean("fooF", Foo.class);

        System.out.println(fooF.toString());
        ctx.close();
    }



    @Component
    public static class FooConfig{

        @Bean(name = "fooF")
        public Foo foo(){
            Foo foo = new Foo();
            foo.setId(600);
            foo.setRef("annotation-bean");

            return foo;
        }

    }
}
