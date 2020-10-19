import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloClassLoader extends ClassLoader{

    private final static byte MAGIC_NUM = (byte) 255;

    public static void main(String[] args) throws Exception {
        // call target via reflect.
        Class<?> helloClazz = new HelloClassLoader().findClass("Hello");
        helloClazz.getMethod("hello").invoke(helloClazz.newInstance());

    }

    @Override
    protected Class<?> findClass(String name) {

        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get("Week_01/Hello.xlass"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (MAGIC_NUM - bytes[i]);
            }

        return defineClass(name,bytes,0,bytes.length);
    }
}
