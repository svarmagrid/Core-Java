package org.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class HeterogeneousMaxHolderTest {

    private final Map<Class<?>, Object> maxValues = new HashMap<>();

    public <T extends Comparable<T>> T put(Class<T> key, T value) {
        Objects.requireNonNull(key);
        T currentMax = key.cast(maxValues.get(key));
        if (currentMax == null || value.compareTo(currentMax) > 0) {
            maxValues.put(key, value);
            return currentMax;
        } else {
            return value;
        }
    }

    public <T> T put(Class<T> key, T value, Comparator<T> comparator) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        Objects.requireNonNull(comparator);

        T currentMax = key.cast(maxValues.get(key));
        Comparator<T> nullSafeComparator = Comparator.nullsFirst(comparator);

        if (currentMax == null || nullSafeComparator.compare(value, currentMax) > 0) {
            maxValues.put(key, value);
            return currentMax;
        } else {
            return value;
        }
    }

    public <T> T getMax(Class<T> key) {
        Objects.requireNonNull(key);
        return key.cast(maxValues.get(key));
    }
}
public class HeterogeneousMaxHolder {
    public static void main(String[] args) {
        HeterogeneousMaxHolderTest holder = new HeterogeneousMaxHolderTest();

        Integer smaller1 = holder.put(Integer.class, 10); // returns null
        Integer smaller2 = holder.put(Integer.class, 5);  // returns 5 (smaller)
        Integer smaller3 = holder.put(Integer.class, 20); // returns 10

        String smallerStr = holder.put(String.class, "abc", String::compareTo); // returns null
        String smallerStr2 = holder.put(String.class, "aaa", String::compareTo); // returns "aaa"

        System.out.println(holder.getMax(Integer.class)); // 20
        System.out.println(holder.getMax(String.class));  // "abc"

    }
}
