import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        var words = new String[] {"", "", ""};
        Arrays.setAll(words, i -> scanner.nextLine());
        for (int i = words.length - 1; i >= 0; i--) {
            System.out.println(words[i]);
        }
    }
}