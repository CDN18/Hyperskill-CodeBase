import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        String subString = scanner.nextLine();
        int index = 0;
        int counter = 0;
        while (index < string.length() && string.indexOf(subString, index) != -1) {
            counter++;
            index = string.indexOf(subString, index) + subString.length();
        }
        System.out.println(counter);
    }
}