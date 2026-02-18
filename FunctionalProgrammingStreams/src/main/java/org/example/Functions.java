package org.example;

public class Functions {
    private Functions() {
    }

    public static FunctionMap<Integer, Integer> intFunctionMap() {
        FunctionMap<Integer, Integer> intFunctionMap = new FunctionMap<>();

        intFunctionMap.addFunction("abs", Math::abs);
        intFunctionMap.addFunction("sgn", x -> Integer.signum(x));
        intFunctionMap.addFunction("increment", x -> x + 1);
        intFunctionMap.addFunction("decrement", x -> x - 1);
        intFunctionMap.addFunction("square", x -> x * x);

        return intFunctionMap;
    }
}
