package org.example;

import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimeNumbers {
    private PrimeNumbers(){

    }

    public static IntStream stream(){
        return IntStream.iterate(2,i->i+1).filter(PrimeNumbers::isPrime);
    }

    public static IntStream stream(int size){
        return stream().limit(size);
    }
    public static int sum(int n){
        return stream(n).sum();
    }

    public static List<Integer> list(int n){
        return stream(n).boxed().collect(Collectors.toList());
    }

    public static void processByIndex(int idx, IntConsumer consumer){
        stream().skip(idx).findFirst().ifPresent(consumer);
    }

    public static Map<Integer,List<Integer>> groupByAmountOfDigits(int n){
        return stream(n).boxed().collect(Collectors.groupingBy(
                num->String.valueOf(num).length()
        ));
    }

    public static boolean isPrime(int num){
        if(num<2) return false;
        return IntStream.rangeClosed(2,(int)Math.sqrt(num)).noneMatch(divisor->num % divisor==0);
    }

    public static void main(String[] args) {
        System.out.println("First 10 Prime numbers");
        PrimeNumbers.stream(10).forEach(System.out::println);

        System.out.print("Sum of first 5 primes: ");
        int sum=PrimeNumbers.sum(5);
        System.out.println(sum);

        System.out.print("List of first 10 prime numbers:");
        System.out.println(PrimeNumbers.list(10));

        System.out.println("Prime number at index 4:");
        PrimeNumbers.processByIndex(4,prime-> System.out.println("Found prime: "+prime));

        System.out.println("Grouped first 20 digits by digit count:");
        System.out.println(PrimeNumbers.groupByAmountOfDigits(20));
    }
}
