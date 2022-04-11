import java.util.Scanner;
import java.util.Arrays;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        String[] words = new String[4];
        Arrays.setAll(words, i -> scanner.next());
        for (String word : words) {
            System.out.println(word);
        }
    }
}
