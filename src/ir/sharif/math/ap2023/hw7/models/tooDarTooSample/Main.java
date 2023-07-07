package ir.sharif.math.ap2023.hw7.models.tooDarTooSample;

import ir.sharif.math.ap2023.hw7.ObjectMaker;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        ObjectMaker objectMaker = new ObjectMaker();
        Map<String, Object> values = new HashMap<>();
        Map<String, Object> valuesC = new HashMap<>();
        Map<String, Object> valuesB = new HashMap<>();
        Map<String, Object> valuesA = new HashMap<>();
        valuesA.put("a", 5);
        valuesB.put("a", valuesA);
        valuesC.put("bb", valuesB);
        values.put("cc", valuesC);
        // A a = (A) objectMaker.makeObject(valuesA, "ir.sharif.math.ap2023.hw7.models.tooDarTooSample.A");
        D d = (D) objectMaker.makeObject(values, "ir.sharif.math.ap2023.hw7.models.tooDarTooSample.D");
        System.out.println(d.c.b.a.a); // 5
        System.out.println(d == d.d); // true
        System.out.println(d.c.b.a.INT == d.DDD); // true
    }
}
