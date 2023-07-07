package ir.sharif.math.ap2023.hw7.models.my6;

import ir.sharif.math.ap2023.hw7.Name;
import ir.sharif.math.ap2023.hw7.SetValue;

public class A {
    private static int x = 9;

    private int y;
    @SetValue(path = "y")
    private int z;

    @Override
    public String toString() {
        return "A{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
