import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        System.out.println(LocalDateTime.parse(input[0]).plusDays(Long.parseLong(input[1]))
                .minusHours(Long.parseLong(input[2])));
    }
}