package net.check321.springcontextdemo.beanloader.factory;

import net.check321.springcontextdemo.beanloader.Foo;
import org.springframework.beans.factory.FactoryBean;

/** 
* @title FactoryBean装配
* @description 
* @author fyang 
* @date 2020/11/18 1:42 上午
*/ 
public class FooFactoryBean implements FactoryBean<Foo> {

    @Override
    public Foo getObject() throws Exception {

        Foo foo = new Foo();
        foo.setId(400);
        foo.setRef("factory-bean");

        return foo;
    }

    @Override
    public Class<?> getObjectType() {
        return Foo.class;
    }
}
