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
        Field[] fields = getAllParentsFields(givenClass);
        Method[] methods = getAllParentsMethods(givenClass);

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
                    givenClass = method.invoke(null, elements).getClass();
                    fields = getAllParentsFields(givenClass);
                    methods = getAllParentsMethods(givenClass);
                    break;
                }
            }

        Constructor defaultConstructor = givenClass.getDeclaredConstructor();
        defaultConstructor.setAccessible(true);
        Object object = defaultConstructor.newInstance();

        for (Field field : fields){
            field.setAccessible(true);

            // Check if @Name is used and set the name of the field in fieldName
            String fieldName;
            if (field.getDeclaredAnnotation(Name.class) == null) fieldName = field.getName();
            else fieldName = field.getDeclaredAnnotation(Name.class).name();

            // Check if fieldName key is available in the map
            if (values.containsKey(fieldName)){
                Object value = values.get(fieldName);
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
        return object;
    }

    private Field[] getAllParentsFields(Class c){
        if (c.getSuperclass() == null)
            return c.getDeclaredFields();

        Field[] superClassFields = getAllParentsFields(c.getSuperclass());
        Field[] thisClassFields = c.getDeclaredFields();
        Field[] ans = new Field[superClassFields.length + thisClassFields.length];
        System.arraycopy(superClassFields, 0, ans, 0, superClassFields.length);
        System.arraycopy(thisClassFields, 0, ans, superClassFields.length, thisClassFields.length);
        return ans;
    }

    private Method[] getAllParentsMethods(Class c){
        if (c.getSuperclass() == null)
            return c.getDeclaredMethods();

        Method[] superClassMethods = getAllParentsMethods(c.getSuperclass());
        Method[] thisClassMethods = c.getDeclaredMethods();
        Method[] ans = new Method[superClassMethods.length + thisClassMethods.length];
        System.arraycopy(superClassMethods, 0, ans, 0, superClassMethods.length);
        System.arraycopy(thisClassMethods, 0, ans, superClassMethods.length, thisClassMethods.length);
        return ans;
    }
}
