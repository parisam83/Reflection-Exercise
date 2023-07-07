package ir.sharif.math.ap2023.hw7.models.my5;

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
        Map<String, Object> cMap = new HashMap<>();
        cMap.put("b", map);
        cMap.put("q", -9);
        C c = (C) maker.makeObject(cMap, C.class.getTypeName());

        System.out.println(c.toString()); // C{b=B{s='fuck ap!', x=2, y=-9}, q=-9}
    }
}
