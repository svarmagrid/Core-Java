package org.example;

public class OOSumOfPrimes {
    public static void main(String[] args) {
        int sumOfPrimes = 0;
        int primes = 0;
        for (int i = 0; primes <= 20; i++) {
            if (isPrime(i)) {
                System.out.println(primes + " : " + i);
                sumOfPrimes += i;
                primes++;
            }
        }

        System.out.println("Sum of first 20 primes: " + sumOfPrimes);
    }

    private static boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}