import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate = LocalDate.parse(scanner.nextLine() + " 1", DateTimeFormatter.ofPattern("yyyy M d"));
        int startMonth = startDate.getMonthValue();
        while (startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            startDate = startDate.plusDays(1);
        }
        while (startDate.getMonthValue() == startMonth) {
            System.out.println(startDate);
            startDate = startDate.plusDays(7);
        }
    }
}