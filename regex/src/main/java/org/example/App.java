package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    public Pattern findSpecificWord() {
        return Pattern.compile("\\bCuriosity\\b");
    }

    public Pattern findFirstWord() {
        return Pattern.compile("^\\b\\w+\\b");
    }

    public Pattern findLastWord() {
        return Pattern.compile("\\b\\w+\\b$");
    }

    public Pattern findAllNumbers() {
        return Pattern.compile("\\d+");
    }

    public Pattern findDates() {
        return Pattern.compile("\\b\\d{4}-\\d{2}-\\d{2}\\b");
    }

    public Pattern findDifferentSpellingsOfColor() {
        return Pattern.compile("\\bcolou?rs?\\b", Pattern.CASE_INSENSITIVE);
    }

    public Pattern findZipCodes() {
        return Pattern.compile("\\b\\d{5}\\b");
    }

    public Pattern findDifferentSpellingsOfLink() {
        return Pattern.compile("\\bl[iy]?\\s?\\(?nk\\b", Pattern.CASE_INSENSITIVE);
    }

    public Pattern findSimplePhoneNumber() {
        return Pattern.compile("\\b\\d{3}-\\d{3}-\\d{4}\\b");
    }

    public Pattern findNumbersFromZeroToFiveWithLengthThree() {
        return Pattern.compile("\\b[0-5]{3}\\b");
    }

    public Pattern findAllWordsWithFiveLength() {
        return Pattern.compile("\\b\\w{5}\\b");
    }

    public Pattern findAllLettersAndDigitsWithLengthThree() {
        return Pattern.compile("\\b[a-zA-Z0-9]{2,3}\\b");
    }

    public Pattern findAllWordsWhichBeginWithCapitalLetter() {
        return Pattern.compile("\\b[A-Z][a-z]*\\b");
    }

    public Pattern findAbbreviation() {
        return Pattern.compile("\\b(AK|AL|AR|AZ|CA|CO|CT|PR|PA|PD)\\b");
    }

    public Pattern findAllOpenBraces() {
        return Pattern.compile("\\{");
    }

    public Pattern findOnlyResources() {
        return Pattern.compile("\\[(.*?)]");
    }

    public Pattern findOnlyLinksInNote() {
        return Pattern.compile("https://\\S+");
    }

    public Pattern findOnlyLinksInJson() {
        return Pattern.compile("http://\\S+");
    }

    public Pattern findAllEmails() {
        return Pattern.compile("\\b[\\w.%+-]+@[\\w.-]+\\.(com|net|edu)\\b", Pattern.CASE_INSENSITIVE);
    }

    public Pattern findAllPatternsForPhoneNumbers() {
        return Pattern.compile(
                "\\b\\d{3}[-.]\\d{3}[-.]\\d{4}\\b" +        // 555-555-5555 or 555.555.5555
                        "|" +
                        "\\(\\d{3}\\)\\d{3}-\\d{4}"                  // (555)555-5555
        );
    }

    public Pattern findOnlyDuplicates() {
        return Pattern.compile("\\b(\\w+)\\b(?=.*\\b\\1\\b)", Pattern.CASE_INSENSITIVE);
    }

    public String replaceFirstAndLastNames(String names) {
        return names.replaceAll("\\b(\\w+),\\s*(\\w+)\\b", "$2 $1");
    }

    public String replaceLastSevenDigitsOfPhoneNumberToX(String phones) {
        return phones.replaceAll("\\b(\\d{3})[-.]?\\d{3}[-.]?\\d{4}\\b", "$1-XXX-XXXX");
    }

    public String insertLinksAndResourcesIntoHref(String links) {
        return links.replaceAll(
                "\\[(.*?)]\\((https?://.*?)\\)",
                "<a href=\"$2\">$1</a>"
        );
    }

    public static void main(String[] args) {
        App regex=new App();
        String text = "Curiosity is powerful. Curiosity drives learning.";
        Matcher matcher = regex.findSpecificWord().matcher(text);

        System.out.println("Finding word 'Curiosity':");
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

        System.out.println("\nFinding numbers:");
        String numbersText = "Call 555-555 or (555)555 on 30th.";
        matcher = regex.findAllNumbers().matcher(numbersText);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

        System.out.println("\nFinding dates:");
        String dateText = "My birthday is 1971-11-23.";
        matcher = regex.findDates().matcher(dateText);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

        System.out.println("\nReplacing names:");
        String names = "John, Smith\nAlice, Cooper";
        System.out.println(regex.replaceFirstAndLastNames(names));

        System.out.println("\nReplacing phone digits:");
        String phones = "555-555-5555";
        System.out.println(regex.replaceLastSevenDigitsOfPhoneNumberToX(phones));

        System.out.println("\nConverting markdown links:");
        String link = "[Bobocode](https://www.bobocode.com)";
        System.out.println(regex.insertLinksAndResourcesIntoHref(link));

        System.out.print("Last word:");
        String word="what is the last word";
        matcher=regex.findLastWord().matcher(word);
        while (matcher.find()){
            System.out.println(matcher.group());
        }

        System.out.println("Finding all emails");
        String email="satish@gmail.com is an email id of satish";
        matcher=regex.findAllEmails().matcher(email);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
