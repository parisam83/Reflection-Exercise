package ir.sharif.math.ap2023.hw7.models.urlSample;

import ir.sharif.math.ap2023.hw7.ObjectMaker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException, MalformedURLException {
        String className = "com.fasterxml.jackson.core.JsonFactory";

        //      ATTENTION:
        //      FOR jarPath: copy the jar file's address (until containing directory)
        //      and paste it in a browser, the address should change to "file:///  ...".
        //      then add the jar file's name and replace the value for below.
        String jarPath = "file:///D:/Mefi/University/Term%202/Advanced%20Programming/Tamrin%207/hw7/" +
                "src/ir/sharif/math/ap2023/hw7/models/urlSample/jackson-core-2.14.2.jar";

        ObjectMaker objectMaker = new ObjectMaker(new URL(jarPath));
        Map<String, Object> values = new HashMap<>();

        Object obj = objectMaker.makeObject(values, className);

        System.out.println(obj);    //      com.fasterxml.jackson.core.JsonFactory@2f2c9b19
    }
}
