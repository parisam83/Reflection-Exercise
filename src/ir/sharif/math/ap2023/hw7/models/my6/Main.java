package ir.sharif.math.ap2023.hw7.models.my6;

import ir.sharif.math.ap2023.hw7.ObjectMaker;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        ObjectMaker maker = new ObjectMaker();
        Map<String, Object> map = new HashMap<>();
        map.put("y", 5);
        A a = (A) maker.makeObject(map, A.class.getTypeName());

        System.out.println(a); // A{x=9, y=5, z=5}
    }
}
