package ir.sharif.math.ap2023.hw7.models.my3;

import ir.sharif.math.ap2023.hw7.ObjectMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        ObjectMaker maker = new ObjectMaker();
        List<String> strB1 = new ArrayList<>();
        strB1.add("salam");
        strB1.add("khbi?");
        Map<String, Object> bMap1 = new HashMap<>();
        bMap1.put("str", strB1);
        List<Map<String, Object>> bList1 = new ArrayList<>();
        bList1.add(bMap1);
        List<List<Map<String, Object>>> twoDBArray = new ArrayList<>();
        twoDBArray.add(bList1);
        List<String> strB2 = new ArrayList<>();
        strB2.add("hi");
        strB2.add("good?");
        Map<String, Object> bMap2 = new HashMap<>();
        bMap2.put("str", strB2);
        List<Map<String, Object>> bList2 = new ArrayList<>();
        bList2.add(bMap2);
        twoDBArray.add(bList2);
        Map<String, Object> aMap = new HashMap<>();
        aMap.put("bs", twoDBArray);
        A a = (A) maker.makeObject(aMap, A.class.getTypeName());

        System.out.println(a.toString()); // A{bs=[[B{str=[salam, khbi?]}], [B{str=[hi, good?]}]]}
    }
}
