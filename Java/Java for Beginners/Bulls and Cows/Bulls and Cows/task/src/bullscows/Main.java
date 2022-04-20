package bullscows;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {
    final static String symbols = "0123456789abcdefghijklmnopqrstuvwxyz";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the length of the secret code:");
        String lenthInput = scanner.nextLine();
        for (char c : lenthInput.toCharArray()) {
            if (c < '0' || c > '9') {
                System.out.printf("Error: \"%s\" isn't a valid number.", lenthInput);
                return;
            }
        }
        int length = Integer.parseInt(lenthInput);
        if (length <= 0) {
            System.out.printf("Error: \"%d\" isn't a valid number.", length);
            return;
        }
        System.out.println("Input the number of possible symbols in the code:");
        String rangeInput = scanner.nextLine();
        for (char c : rangeInput.toCharArray()) {
            if (c < '0' || c > '9') {
                System.out.printf("Error: \"%s\" isn't a valid number.", rangeInput);
                return;
            }
        }
        int range = Integer.parseInt(rangeInput);
        if (range <= 0) {
            System.out.printf("Error: \"%d\" isn't a valid number.", range);
            return;
        }
        if (length > range) {
            System.out.printf("Error: it's not possible to generate a code " +
                    "with a length of %d with %d unique symbols.\n", length, range);
            return;
        }
        if (range > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        }
        final char[] secret = Objects.requireNonNull(GenerateDigits(length, range))
                .toCharArray();
        String rangeInfo;
        if (range <= 10) {
            rangeInfo = "(0-" + (range - 1) + ")";
        } else {
            rangeInfo = "(0-9, a-" + symbols.charAt(range - 1) + ")";
        }
        System.out.printf("The secret is prepared: %s %s.\n", "*".repeat(length), rangeInfo);
        System.out.println("Okay, let's start a game!");
        boolean success = false;
        int turn = 1;
        while (!success) {
            System.out.printf("Turn %d:\n", turn);
            char[] input = scanner.nextLine().toCharArray();
            int bulls = 0;
            int cows = 0;
            String bull = "bull";
            String cow = "cow";
            for (int i = 0; i < Math.min(length, input.length); i++) {
                if (secret[i] == input[i]) {
                    bulls++;
                } else if (String.valueOf(secret).contains(String.valueOf(input[i]))) {
                    cows++;
                }
            }
            if (bulls > 1) {
                bull = bull + "s";
            }
            if (cows > 1) {
                cow = cow + "s";
            }
            if (bulls != length) {
                System.out.printf("Grade: %d %s and %d %s.\n",
                        bulls, bull, cows, cow);
                turn++;
            } else {
                System.out.printf("Grade: %d bulls\n", length);
                success = true;
            }
        }
        System.out.println("Congratulations! You guessed the secret code.");
    }

    private static String GenerateDigits(int length, int range) {
        Random generator = new Random();
        StringBuilder uniqueDigits = new StringBuilder();
        while (uniqueDigits.length() < length) {
            char nextSymbol = symbols.charAt(generator.nextInt(range));
            if (uniqueDigits.indexOf(String.
                    valueOf(nextSymbol)) == -1) {
                uniqueDigits.append(nextSymbol);
            }
        }
        if (length > symbols.length()) {
            return null;
        }
        return uniqueDigits.toString();
    }
}
