import java.util.Locale;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        char[] dna = scanner.nextLine().toLowerCase(Locale.ROOT).toCharArray();
        int counter = 0;
        for (char ch : dna) {
            if (ch == 'g' || ch == 'c') {
                counter++;
            }
        }
        double ratio = (double) counter / dna.length * 100;
        System.out.println(ratio);
    }
}