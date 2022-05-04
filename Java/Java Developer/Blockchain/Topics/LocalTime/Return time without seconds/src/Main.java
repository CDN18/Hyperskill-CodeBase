import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(":");
        System.out.println(LocalTime.of(Integer.parseInt(input[0]), Integer.parseInt(input[1])));
    }
}