package net.check321.springcontextdemo.beanloader.factory;

import net.check321.springcontextdemo.beanloader.Foo;

/**
* @title Foo工厂，可设计为抽象工厂
* @description 提供Foo实例创建
* @author fyang
* @date 2020/11/18 1:32 上午
*/
public class DefaultFooFactory implements FooFactory{

    @Override
    public Foo create(String ref) {
        Foo foo = new Foo();
        foo.setId(300);
        foo.setRef(ref);

        return foo;
    }
}
