import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        List<String> list = List.of(scanner.nextLine().split(""));
        System.out.println(Collections.frequency(list, scanner.nextLine()));
    }
}