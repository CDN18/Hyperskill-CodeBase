import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        LocalDate input = LocalDate.parse(scanner.nextLine());
        System.out.println(input.minusDays(10));
    }
}