package net.check321.springbootstarterdemo;

public class Foo {

    private Integer id;

    private String ref;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
