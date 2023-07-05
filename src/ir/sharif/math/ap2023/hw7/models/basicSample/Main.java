package ir.sharif.math.ap2023.hw7.models.basicSample;

import ir.sharif.math.ap2023.hw7.ObjectMaker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        ObjectMaker objectMaker = new ObjectMaker();
        Map<String, Object> values = new HashMap<>();


        values.put("var1", -1);
        values.put("var2", -2);
        values.put("var3", -3);
        values.put("var4", -4);
        values.put("var5", -5);
        values.put("var6", -6);
        values.put("primitiveVar", -11);
        values.put("firstIntArray", Arrays.asList(-1, -2, -3, -4, -5));
        values.put("secondIntArray", Arrays.asList(-1, -2, -3));
        values.put("stringVar", "testValue");


        Child child = (Child) objectMaker.makeObject(
                values,
                "ir.sharif.math.ap2023.hw7.models.basicSample.Child");
        System.out.println(child);
    }
}
