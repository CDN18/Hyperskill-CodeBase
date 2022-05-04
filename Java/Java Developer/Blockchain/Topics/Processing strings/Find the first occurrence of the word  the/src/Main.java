import java.util.Locale;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        final String keyWord = "the";
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder(scanner.nextLine().toLowerCase());
        System.out.println(input.indexOf(keyWord));
    }
}