package net.check321.springcontextdemo.beanloader;

/**
* @title 模拟Bean类型
 *
* @description
* @author fyang
* @date 2020/11/17 11:40 下午
*/
public class Foo {

    private int id;

    private String ref;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                '}';
    }
}
