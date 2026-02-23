package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FunctionMap<T, R> {
    private final Map<String, Function<T, R>> functionMap;

    public FunctionMap() {
        functionMap = new HashMap<>();
    }

    public void addFunction(String name, Function<T, R> function) {
        functionMap.put(name, function);
    }

    public Function<T, R> getFunction(String name) {
            return functionMap.get(name);
    }

    public static void main(String[] args) {

        FunctionMap<Integer, Integer> map = Functions.intFunctionMap();

        Function<Integer, Integer> abs = map.getFunction("abs");
        Function<Integer, Integer> square = map.getFunction("square");
        Function<Integer, Integer> increment = map.getFunction("increment");

        System.out.println("abs(-5) = " + abs.apply(-5));
        System.out.println("square(4) = " + square.apply(4));
        System.out.println("increment(10) = " + increment.apply(10));
    }
}
