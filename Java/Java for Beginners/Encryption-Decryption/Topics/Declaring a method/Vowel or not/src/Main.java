import java.util.Scanner;

public class Main {

    public static boolean isVowel(char ch) {
        // write your code here
        String vowels = "aAeEiIoOuU";
        for (int i = 0; i < vowels.length(); i++) {
            if (ch ==  vowels.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char letter = scanner.nextLine().charAt(0);
        System.out.println(isVowel(letter) ? "YES" : "NO");
    }
}