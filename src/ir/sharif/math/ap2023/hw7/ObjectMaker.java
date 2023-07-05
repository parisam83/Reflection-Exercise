package ir.sharif.math.ap2023.hw7;


import java.lang.reflect.Array;
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
            Object value;

            // Check if @Name is used and find the name of the field in the given map
            if (field.getDeclaredAnnotation(Name.class) == null) value = values.get(field.getName());
            else value = values.get(field.getDeclaredAnnotation(Name.class).name());

            if (value != null) {
                // Check if value is a List of not
                if (value instanceof List<?>){
                    Object valueToSet = Array.newInstance(field.getType().getComponentType(), ((List<?>) value).size());
                    for (int i = 0; i < ((List<?>) value).size(); i++)
                        Array.set(valueToSet, i, ((List<?>) value).get(i));
                    field.set(object, valueToSet);
                }
                else
                    field.set(object, value);
            }
        }
        return null;
    }

}
