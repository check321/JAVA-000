package net.check321.springbootstarterdemo.config;

import net.check321.springbootstarterdemo.Foo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @title Foo springboot-starter 配置
* @description
* @author fyang
* @date 2020/11/18 12:12 下午
*/
@Configuration
@ConditionalOnResource(resources = "META-INF/spring.factories")
public class FooAutoConfiguration {

    @Bean
    public Foo foo(){

        final Foo foo = new Foo();
        foo.setId(900);
        foo.setRef("springboot-starter");

        return foo;

    }
}
