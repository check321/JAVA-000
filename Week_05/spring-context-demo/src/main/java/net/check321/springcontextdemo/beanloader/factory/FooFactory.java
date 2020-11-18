package net.check321.springcontextdemo.beanloader.factory;

import net.check321.springcontextdemo.beanloader.Foo;

/**
* @title Foo工厂接口
* @description
* @author fyang
* @date 2020/11/18 1:58 上午
*/
public interface FooFactory {

    Foo create(String ref);
}
