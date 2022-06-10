import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        System.out.println(Integer.valueOf(LocalDateTime.now().withYear(scanner.nextInt()).withDayOfYear(scanner.nextInt()).getDayOfMonth()).equals(1));
    }
}