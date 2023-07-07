package ir.sharif.math.ap2023.hw7;


import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ObjectMaker {
    private final URLClassLoader urlClassLoader;
    private HashMap<Object, Object> parent = new HashMap<>();
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
        Object instance = handleNoneSetValueFields(values, className);
        handleSetValueFields(instance);
        return instance;
    }

    private Object handleNoneSetValueFields(Map<String, Object> values, String className) throws ReflectiveOperationException {
        Class givenClass = Class.forName(className, true, urlClassLoader);
        Field[] fields = getAllParentsFields(givenClass);
        Method[] methods = getAllParentsMethods(givenClass);
        Object instance = null;

        for (Method method : methods)
            if (method.getDeclaredAnnotation(UseAsConstructor.class) != null){
                String[] args = method.getDeclaredAnnotation(UseAsConstructor.class).args();
                Object[] elements = new Object[args.length];
                for (int i = 0; i < args.length; i++){
                    if (values.containsKey(args[i])) elements[i] = values.get(args[i]);
                    else elements[i] = null;
                }
                method.setAccessible(true);
                instance = method.invoke(null, elements);
                givenClass = instance.getClass();
                fields = getAllParentsFields(givenClass);
                methods = getAllParentsMethods(givenClass);
                break;
            }

        if (instance == null) {
            Constructor defaultConstructor = givenClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            instance = defaultConstructor.newInstance();
        }

        for (Field field : fields){
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (field.getDeclaredAnnotation(SetValue.class) == null){
                // Check if @Name is used and set the name of the field in fieldName
                String fieldName = findFieldName(field);

                // Check if fieldName key is available in the map
                if (values.containsKey(fieldName)) {
                    Object value = values.get(fieldName);
                    if (value instanceof HashMap<?,?>) {
                        Object fieldValue = handleNoneSetValueFields((Map<String, Object>) value, field.getType().getTypeName());
                        field.set(instance, fieldValue);
                        parent.put(fieldValue, instance);
                    }
                    else if (value instanceof List<?>)
                        handleListValue(instance, field, (List<?>) value);
                    else
                        handlePrimitiveValue(instance, field, value);
                }
            }
        }
        return instance;
    }
    private void handleSetValueFields(Object instance) throws ReflectiveOperationException {
        if (instance == null) return;

        if (instance.getClass().isArray() && !primitiveClasses.contains(instance.getClass().getComponentType())) {
            for (int i = 0; i < Array.getLength(instance); i++)
                handleSetValueFields(Array.get(instance, i));
            return;
        }

        for (Field field : getAllParentsFields(instance.getClass())){
            field.setAccessible(true);

            if (Modifier.isStatic(field.getModifiers())) continue;
            if (field.getDeclaredAnnotation(SetValue.class) != null){
                String path = field.getDeclaredAnnotation(SetValue.class).path();
                Object instancePointer = instance;
                while (path.length() > 0){
                    String firstPart = firstPartOfAddress(path);
                    path = getTrimmedPath(path);
                    if (instancePointer == null) break;
                    if (firstPart.equals(".."))
                        instancePointer = parent.get(instancePointer);
                    else{
                        Field fieldPointer = null;
                        for (Field field1 : getAllParentsFields(instancePointer.getClass()))
                            if (findFieldName(field1).equals(firstPart)){
                                field1.setAccessible(true);
                                fieldPointer = field1;
                            }
                        fieldPointer.setAccessible(true);
                        instancePointer = fieldPointer.get(instancePointer);
                    }
                }
                if (instancePointer != null) field.set(instance, instancePointer);
            }
            else if (!primitiveClasses.contains(field.getType()))
                handleSetValueFields(field.get(instance));
        }

    }

    private String getTrimmedPath(String path) {
        String firstPart = firstPartOfAddress(path);
        if (firstPart.length() == path.length())
            return "";
        return path.replaceFirst(firstPart + "/", "");
    }

    private String findFieldName(Field field) {
        if (field.getDeclaredAnnotation(Name.class) == null) return field.getName();
        return field.getDeclaredAnnotation(Name.class).name();
    }

    private void handleListValue(Object instance, Field field, List<?> value) throws ReflectiveOperationException {
        Object valueToSet = Array.newInstance(field.getType().getComponentType(), value.size());
        for (int i = 0; i < value.size(); i++) {
            Object fieldValue = findFieldValue(instance, value.get(i), field.getType().getComponentType());
            parent.put(fieldValue, instance);
            Array.set(valueToSet, i, fieldValue);
        }
        field.set(instance, valueToSet);
        parent.put(valueToSet, instance);
    }
    private void handlePrimitiveValue(Object instance, Field field, Object value) throws IllegalAccessException {
        field.set(instance, value);
        parent.put(value, instance);
    }

    private Object findFieldValue(Object Parent, Object value, Class<?> componentType) throws ReflectiveOperationException {
        if (componentType.isArray()){
            Object valueToSet = Array.newInstance(componentType.getComponentType(), ((List<?>) value).size());
            for (int i = 0; i < ((List<?>) value).size(); i++) {
                Object fieldValue = findFieldValue(Parent, ((List<?>) value).get(i), componentType.getComponentType());
                parent.put(fieldValue, Parent);
                Array.set(valueToSet, i, fieldValue);
            }
            return valueToSet;
        }
        else if (value instanceof Map<?,?>){
            Object ans = handleNoneSetValueFields((Map<String, Object>) value, componentType.getName());
            parent.put(ans, Parent);
            return ans;
        }
        return value;
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