package ir.sharif.math.ap2023.hw7.models.my1;

import ir.sharif.math.ap2023.hw7.ObjectMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        ObjectMaker maker = new ObjectMaker();
        List<List<Integer>> in = new ArrayList<>();
        List<Integer> i = new ArrayList<>();
        i.add(2);
        i.add(20);
        i.add(200);
        in.add(i);
        Map<String, Object> map = new HashMap<>();
        map.put("bs", in);
        A a = (A) maker.makeObject(map, A.class.getTypeName());

        System.out.println(a.toString()); // A{bs=[[2, 20, 200]]}
    }
}
