package org.example;

import java.util.stream.IntStream;

public class SumOfSquares {
    public static void main(String[] args) {
        System.out.println("Sum of squares from 5 to 10 is " + calculateSumOfSquaresInRange(5, 10));
    }

    static int calculateSumOfSquaresInRange(int startInclusive, int endInclusive) {
        if (endInclusive < startInclusive) {
            throw new RuntimeException();
        }

        return IntStream.rangeClosed(startInclusive, endInclusive)
                        .map(i -> i * i)
                        .sum();
    }
}
