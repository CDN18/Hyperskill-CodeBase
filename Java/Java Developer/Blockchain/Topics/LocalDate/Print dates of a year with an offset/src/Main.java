import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        int step = Integer.parseInt(scanner.nextLine());
        int currentYear = startDate.getYear();
        while (startDate.getYear() == currentYear) {
            System.out.println(startDate);
            startDate = startDate.plusDays(step);
        }
    }
}