import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        LocalTime startTime = LocalTime.parse(scanner.nextLine());
        String[] minusTime = scanner.nextLine().split(" ");
        System.out.println(startTime.minusHours(Integer.parseInt(minusTime[0])).
                minusMinutes(Integer.parseInt(minusTime[1])));
    }
}