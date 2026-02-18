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
}
