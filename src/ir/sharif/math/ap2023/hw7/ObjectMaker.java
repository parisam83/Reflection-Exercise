package ir.sharif.math.ap2023.hw7;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class ObjectMaker {
    private static final List<Class<?>> primitiveClasses = Arrays.asList(
            boolean.class,
            short.class,
            byte.class,
            int.class,
            long.class,
            float.class,
            double.class,
            String.class
    );

    public ObjectMaker(URL... urls) {
        // TODO
    }

    public Object makeObject(Map<String, Object> values, String className) throws ReflectiveOperationException {
        ClassLoader classLoader = ObjectMaker.class.getClassLoader();
        Class givenClass = classLoader.loadClass(className);
        Field[] fields = givenClass.getDeclaredFields();
        Method[] methods = givenClass.getDeclaredMethods();

        Constructor defaultConstructor = givenClass.getDeclaredConstructor();
        defaultConstructor.setAccessible(true);
        Object object = defaultConstructor.newInstance();

        for (Field field : fields){
            field.setAccessible(true);
            Object value = values.get(field.getName());
            if (value != null) field.set(object, value);
        }
        return null;
    }

}
