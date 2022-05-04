import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        LocalDateTime start = LocalDateTime.parse(scanner.nextLine());
        start = start.plusMinutes(scanner.nextLong());
        System.out.println(start.getYear() + " " + start.getDayOfYear() + " " + start.toLocalTime());
    }
}