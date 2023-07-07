package ir.sharif.math.ap2023.hw7.models.my4;

import ir.sharif.math.ap2023.hw7.ObjectMaker;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        ObjectMaker maker = new ObjectMaker();
        Map<String, Object> map = new HashMap<>();
        map.put("s", "fuck ap!");
        map.put("y", 12);
        map.put("x", 2);
        B b = (B) maker.makeObject(map, B.class.getTypeName());

        System.out.println(b.getX()); // 2
        System.out.println(b.getY()); // 2
    }
}
