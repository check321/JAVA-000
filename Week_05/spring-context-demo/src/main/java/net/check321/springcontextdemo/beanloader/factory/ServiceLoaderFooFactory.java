package net.check321.springcontextdemo.beanloader.factory;

import com.sun.tools.javac.util.ServiceLoader;
import net.check321.springcontextdemo.beanloader.Foo;


/**
* @title Foo {@link ServiceLoader}实现
* @description
* @author fyang
* @date 2020/11/18 2:05 上午
*/
public class ServiceLoaderFooFactory implements FooFactory{

    @Override
    public Foo create(String ref) {

        Foo foo = new Foo();
        foo.setId(500);
        foo.setRef("service-loader");

        return foo;
    }
}
