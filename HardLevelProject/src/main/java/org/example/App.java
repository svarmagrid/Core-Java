package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class App {

    static Scanner scanner = new Scanner(System.in);

    // inverted index: word -> set of line numbers
    static Map<String, Set<Integer>> invertedIndex = new HashMap<>();

    public static void main(String[] args) {

        String filename = null;
        for (int i = 0; i < args.length - 1; i++) {
            if ("--data".equals(args[i])) {
                filename = args[i + 1];
                break;
            }
        }

        if (filename == null) {
            System.out.println("Error: --data argument is required");
            return;
        }

        List<String> people = new ArrayList<>();

        // read file and build inverted index
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            int lineNumber = 0;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                people.add(line);

                String[] words = line.toLowerCase().split("\\s+");
                for (String word : words) {
                    invertedIndex
                            .computeIfAbsent(word, k -> new HashSet<>())
                            .add(lineNumber);
                }

                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }

        // menu loop
        while (true) {
            System.out.println("=== Menu ===");
            System.out.println("1. Find a person");
            System.out.println("2. Print all people");
            System.out.println("0. Exit");

            String isValidInput=scanner.nextLine().trim();
            int choice;
            try {
                choice=Integer.parseInt(isValidInput);
            }catch (NumberFormatException e){
                System.out.println("Incorrect option! Try again");
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Bye!");
                    return;
                case 1:
                    findPeople(people);
                    break;
                case 2:
                    printPeople(people);
                    break;
                default:
                    System.out.println("\nIncorrect option! Try again");
            }
        }
    }

    public static void findPeople(List<String> people) {
        System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine().trim().toUpperCase();

        System.out.println("\nEnter a name or email to search all suitable people.");
        String[] queryWords = scanner.nextLine().trim().toLowerCase().split("\\s+");

        Set<Integer> resultIndexes = search(strategy, queryWords, people.size());

        if (resultIndexes.isEmpty()) {
            System.out.println("No matching people found");
            return;
        }

        System.out.println(resultIndexes.size() + " persons found:");
        for (int index : resultIndexes) {
            System.out.println(people.get(index));
        }
    }

    private static Set<Integer> search(String strategy, String[] queryWords, int totalPeople) {
        Set<Integer> result = new LinkedHashSet<>();

        if ("ALL".equals(strategy)) {
            result = getAllMatches(queryWords);
        } else if ("ANY".equals(strategy)) {
            result = getAnyMatches(queryWords);
        } else if ("NONE".equals(strategy)) {
            result = getNoneMatches(queryWords, totalPeople);
        }

        return result;
    }

    private static Set<Integer> getAllMatches(String[] queryWords) {
        Set<Integer> result = null;

        for (String word : queryWords) {
            Set<Integer> indexes = invertedIndex.getOrDefault(word, Collections.emptySet());
            if (result == null) {
                result = new HashSet<>(indexes);
            } else {
                result.retainAll(indexes);
            }
        }

        return result == null ? Collections.emptySet() : result;
    }

    private static Set<Integer> getAnyMatches(String[] queryWords) {
        Set<Integer> result = new LinkedHashSet<>();

        for (String word : queryWords) {
            result.addAll(invertedIndex.getOrDefault(word, Collections.emptySet()));
        }

        return result;
    }

    private static Set<Integer> getNoneMatches(String[] queryWords, int totalPeople) {
        Set<Integer> excluded = getAnyMatches(queryWords);
        Set<Integer> result = new LinkedHashSet<>();

        for (int i = 0; i < totalPeople; i++) {
            if (!excluded.contains(i)) {
                result.add(i);
            }
        }

        return result;
    }

    public static void printPeople(List<String> people) {
        System.out.println("=== List of people ===");
        for (String person : people) {
            System.out.println(person);
        }
    }
}

