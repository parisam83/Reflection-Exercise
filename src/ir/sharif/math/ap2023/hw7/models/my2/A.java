package ir.sharif.math.ap2023.hw7.models.my2;

import ir.sharif.math.ap2023.hw7.Name;

public class A {
    private int x;
    @Name(name = "x")
    private int y;
    @Name(name = "x")
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
