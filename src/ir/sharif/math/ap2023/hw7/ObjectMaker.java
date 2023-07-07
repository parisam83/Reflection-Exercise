package ir.sharif.math.ap2023.hw7;


import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ObjectMaker {
    private final URLClassLoader urlClassLoader;
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
        urlClassLoader = new URLClassLoader(urls);
    }

    public Object makeObject(Map<String, Object> values, String className) throws ReflectiveOperationException {
        Class givenClass = Class.forName(className, true, urlClassLoader);
        Field[] fields = getAllParentsFields(givenClass);
        Method[] methods = getAllParentsMethods(givenClass);
        Object instance = null;

        for (Method method : methods)
            if (method.getDeclaredAnnotation(UseAsConstructor.class) != null){
                String[] args = method.getDeclaredAnnotation(UseAsConstructor.class).args();
                Object[] elements = new Object[args.length];
                boolean goToNextMethod = false;
                for (int i = 0; i < args.length; i++){
                    if (values.containsKey(args[i])) elements[i] = values.get(args[i]);
                    else goToNextMethod = true;
                }
                if (!goToNextMethod){
                    method.setAccessible(true);
                    instance = method.invoke(null, elements);
                    givenClass = instance.getClass();
                    fields = getAllParentsFields(givenClass);
                    methods = getAllParentsMethods(givenClass);
                    break;
                }
            }

        if (instance == null) {
            Constructor defaultConstructor = givenClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            instance = defaultConstructor.newInstance();
        }

        for (Field field : fields){
            field.setAccessible(true);

            // Check if field has setValue annotation
            if (field.getDeclaredAnnotation(SetValue.class) != null){
                /*String path = field.getDeclaredAnnotation(SetValue.class).path();
                while (path.length() > 0){
                    System.out.println(firstPartOfAddress(path));
                    // givenClass.getField(path.)
                }
                if (path.length() == 0) field.set(object, object);*/

            }
            else {
                // Check if @Name is used and set the name of the field in fieldName
                String fieldName = findFieldName(field);

                // Check if fieldName key is available in the map
                if (values.containsKey(fieldName)) {
                    Object value = values.get(fieldName);
                    if (value instanceof HashMap<?,?>)
                        field.set(instance, makeObject((Map<String, Object>) value, field.getType().getTypeName()));
                    else if (value instanceof List<?>)
                        handleListValue(instance, field, (List<?>) value);
                    else
                        handlePrimitiveValue(instance, field, value);
                }
            }
        }
        return instance;
    }

    private Class loadGetClass(String className) {
        try {
            Class.forName(className);
            return ObjectMaker.class.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
                // return urlClassLoader.loadClass(className);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private String findFieldName(Field field) {
        if (field.getDeclaredAnnotation(Name.class) == null) return field.getName();
        return field.getDeclaredAnnotation(Name.class).name();
    }

    private void handleListValue(Object instance, Field field, List<?> value) throws IllegalAccessException {
        Object valueToSet = Array.newInstance(field.getType().getComponentType(), value.size());
        for (int i = 0; i < value.size(); i++)
            Array.set(valueToSet, i, value.get(i));
        field.set(instance, valueToSet);
    }
    private void handlePrimitiveValue(Object instance, Field field, Object value) throws IllegalAccessException {
        field.set(instance, value);
    }

    private Field[] getAllParentsFields(Class c){
        if (c.getSuperclass() == null)
            return c.getDeclaredFields();

        Field[] superClassFields = getAllParentsFields(c.getSuperclass());
        Field[] thisClassFields = c.getDeclaredFields();
        Field[] allFields = new Field[superClassFields.length + thisClassFields.length];
        System.arraycopy(superClassFields, 0, allFields, 0, superClassFields.length);
        System.arraycopy(thisClassFields, 0, allFields, superClassFields.length, thisClassFields.length);
        return allFields;
    }

    private Method[] getAllParentsMethods(Class c){
        if (c.getSuperclass() == null)
            return c.getDeclaredMethods();

        Method[] superClassMethods = getAllParentsMethods(c.getSuperclass());
        Method[] thisClassMethods = c.getDeclaredMethods();
        Method[] allMethods = new Method[superClassMethods.length + thisClassMethods.length];
        System.arraycopy(superClassMethods, 0, allMethods, 0, superClassMethods.length);
        System.arraycopy(thisClassMethods, 0, allMethods, superClassMethods.length, thisClassMethods.length);
        return allMethods;
    }

    private String firstPartOfAddress(String path){
        for (int i = 0; i < path.length(); i++)
            if (path.charAt(i) == '/')
                return path.substring(0, i);
        return path;
    }
}
