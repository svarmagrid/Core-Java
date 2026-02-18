//package org.example;
//
//import com.bobocode.model.Account;
//import com.bobocode.util.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.Month;
//import java.util.*;
//import java.util.stream.Collectors;
////
//@AllArgsConstructor
//public class CrazyStreams {
//    private Collection<Account> accounts;
//
//    public Optional<Account> findRichestPerson() {
//        return accounts.stream()
//                .max(Comparator.comparing(Account::getBalance));
//    }
//
//    public List<Account> findAccountsByBirthdayMonth(Month birthdayMonth) {
//        return accounts.stream()
//                .filter(a -> a.getBirthday().getMonth() == birthdayMonth)
//                .toList();
//    }
//
//    public Map<Boolean, List<Account>> partitionMaleAccounts() {
//        return accounts.stream()
//                .collect(Collectors.partitioningBy(Account::isMale));
//    }
//
//    public Map<String, List<Account>> groupAccountsByEmailDomain() {
//        return accounts.stream()
//                .collect(Collectors.groupingBy(a -> a.getEmail().split("@")[1]));
//    }
//
//    public int getNumOfLettersInFirstAndLastNames() {
//        return accounts.stream()
//                .mapToInt(a -> a.getFirstName().length() + a.getLastName().length())
//                .sum();
//    }
//
//    public BigDecimal calculateTotalBalance() {
//        return accounts.stream()
//                .map(Account::getBalance)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    public List<Account> sortByFirstAndLastNames() {
//        return accounts.stream()
//                .sorted(Comparator.comparing(Account::getFirstName)
//                        .thenComparing(Account::getLastName))
//                .toList();
//    }
//
//    public boolean containsAccountWithEmailDomain(String emailDomain) {
//        return accounts.stream()
//                .anyMatch(a -> a.getEmail().endsWith("@" + emailDomain));
//    }
//
//    public BigDecimal getBalanceByEmail(String email) {
//        return accounts.stream()
//                .filter(a -> a.getEmail().equals(email))
//                .map(Account::getBalance)
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("Cannot find Account by email=" + email));
//    }
//
//    public Map<Long, Account> collectAccountsById() {
//        return accounts.stream()
//                .collect(Collectors.toMap(Account::getId, a -> a));
//    }
//
//    public Map<String, BigDecimal> collectBalancesByEmailForAccountsCreatedOn(int year) {
//        return accounts.stream()
//                .filter(a -> a.getCreationDate().getYear() == year)
//                .collect(Collectors.toMap(Account::getEmail, Account::getBalance));
//    }
//
//    public Map<String, Set<String>> groupFirstNamesByLastNames() {
//        return accounts.stream()
//                .collect(Collectors.groupingBy(Account::getLastName,
//                        Collectors.mapping(Account::getFirstName, Collectors.toSet())));
//    }
//
//    public Map<Month, String> groupCommaSeparatedFirstNamesByBirthdayMonth() {
//        return accounts.stream()
//                .collect(Collectors.groupingBy(a -> a.getBirthday().getMonth(),
//                        Collectors.mapping(Account::getFirstName, Collectors.joining(", "))));
//    }
//
//    public Map<Month, BigDecimal> groupTotalBalanceByCreationMonth() {
//        return accounts.stream()
//                .collect(Collectors.groupingBy(a -> a.getCreationDate().getMonth(),
//                        Collectors.mapping(Account::getBalance,
//                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
//    }
//
//    public Map<Character, Long> getCharacterFrequencyInFirstNames() {
//        return accounts.stream()
//                .flatMap(a -> a.getFirstName().chars().mapToObj(c -> (char) c))
//                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
//    }
//
//    public Map<Character, Long> getCharacterFrequencyIgnoreCaseInFirstAndLastNames(int nameLengthBound) {
//        return accounts.stream()
//                .flatMap(a -> Stream.concat(
//                        a.getFirstName().chars().mapToObj(c -> (char) c),
//                        a.getLastName().chars().mapToObj(c -> (char) c)))
//                .map(Character::toLowerCase)
//                .filter(c -> c >= 'a' && c <= 'z') // optional: letters only
//                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
//    }
//}
