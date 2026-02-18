package org.example;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.*;
import java.util.Comparator;

public class CrazyLambdas {

    public static Supplier<String> helloSupplier() {
        return () -> "Hello";
    }

    public static Predicate<String> isEmptyPredicate() {
        return String::isEmpty;
    }

    public static BiFunction<String, Integer, String> stringMultiplier() {
        return (s, n) -> s.repeat(n);
    }

    public static Function<BigDecimal, String> toDollarStringFunction() {
        return bd -> "$" + bd.toString();
    }

    public static Predicate<String> lengthInRangePredicate(int min, int max) {
        return s -> s.length() >= min && s.length() < max;
    }

    public static IntSupplier randomIntSupplier() {
        return () -> (int) (Math.random() * Integer.MAX_VALUE);
    }

    public static IntUnaryOperator boundedRandomIntSupplier() {
        return bound -> (int) (Math.random() * bound);
    }

    public static IntUnaryOperator intSquareOperation() {
        return x -> x * x;
    }

    public static LongBinaryOperator longSumOperation() {
        return (a, b) -> a + b;
    }

    public static ToIntFunction<String> stringToIntConverter() {
        return Integer::parseInt;
    }

    public static Supplier<IntUnaryOperator> nMultiplyFunctionSupplier(int n) {
        return () -> x -> n * x;
    }

    public static UnaryOperator<Function<String, String>> composeWithTrimFunction() {
        return f -> f.andThen(String::trim);
    }

    public static Supplier<Thread> runningThreadSupplier(Runnable runnable) {
        return () -> {
            Thread t = new Thread(runnable);
            t.start();
            return t;
        };
    }

    public static Consumer<Runnable> newThreadRunnableConsumer() {
        return r -> new Thread(r).start();
    }

    public static Function<Runnable, Supplier<Thread>> runnableToThreadSupplierFunction() {
        return r -> () -> {
            Thread t = new Thread(r);
            t.start();
            return t;
        };
    }

    public static BiFunction<IntUnaryOperator, IntPredicate, IntUnaryOperator> functionToConditionalFunction() {
        return (func, pred) -> x -> pred.test(x) ? func.applyAsInt(x) : x;
    }

    public static BiFunction<Map<String, IntUnaryOperator>, String, IntUnaryOperator> functionLoader() {
        return (map, name) -> map.getOrDefault(name, IntUnaryOperator.identity());
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> mapper) {
        return (a, b) -> mapper.apply(a).compareTo(mapper.apply(b));
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> thenComparing(
            Comparator<? super T> comparator, Function<? super T, ? extends U> mapper) {
        return (a, b) -> {
            int res = comparator.compare(a, b);
            return res != 0 ? res : mapper.apply(a).compareTo(mapper.apply(b));
        };
    }

    public static Supplier<Supplier<Supplier<String>>> trickyWellDoneSupplier() {
        return () -> () -> () -> "WELL DONE!";
    }
}
