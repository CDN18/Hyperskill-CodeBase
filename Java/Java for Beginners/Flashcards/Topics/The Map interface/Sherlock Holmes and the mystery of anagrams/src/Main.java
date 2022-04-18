import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String word1 = scanner.nextLine().toLowerCase();
        String word2 = scanner.nextLine().toLowerCase();
        Map<Character, Integer> freq1 = new HashMap<>();
        Map<Character, Integer> freq2 = new HashMap<>();
        for (int i = 0; i < 26; i++) {
            freq1.put((char) ('a' + i), 0);
            freq2.put((char) ('a' + i), 0);
        }
        for (char c : word1.toCharArray()) {
            freq1.put(c, freq1.get(c) + 1);
        }
        for (char c : word2.toCharArray()) {
            freq2.put(c, freq2.get(c) + 1);
        }
        if (Objects.equals(freq1, freq2)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}