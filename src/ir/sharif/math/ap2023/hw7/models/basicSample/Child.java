package ir.sharif.math.ap2023.hw7.models.basicSample;

import java.util.Arrays;

public class Child extends Father{
    private int primitiveVar = 11;
    int[] firstIntArray = {1, 2, 3, 4, 5};
    int[] secondIntArray;

    String stringVar;

    private Child() {
    }

    public Child(int unimportantArg) {
        primitiveVar = 0;
    }

    @Override
    public String toString() {
        return "Child{" +
                "\n\tprimitiveVar=" + primitiveVar +
                ",\n\tfirstIntArray=" + Arrays.toString(firstIntArray) +
                ",\n\tsecondIntArray=" + Arrays.toString(secondIntArray) +
                ",\n\tstringVar='" + stringVar + '\'' +
                ",\n\tvar1=" + getVar1() +
                ",\n\tvar2=" + var2 +
                ",\n\tvar3=" + var3 +
                ",\n\tvar4=" + var4 +
                ",\n\tvar5=" + var5 +
                ",\n\tvar6=" + var6 +
                "\n}";
    }
}
