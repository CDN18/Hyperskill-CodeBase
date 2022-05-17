import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        LocalTime rang1Left = LocalTime.parse(scanner.next());
        LocalTime rang1Right = LocalTime.parse(scanner.next());
        LocalTime rang2Left = LocalTime.parse(scanner.next());
        LocalTime rang2Right = LocalTime.parse(scanner.next());
        LocalTime leftMax = rang1Left.compareTo(rang2Left) > 0 ? rang1Left : rang2Left;
        LocalTime rightMin = rang1Right.compareTo(rang2Right) < 0 ? rang1Right : rang2Right;
        System.out.println(leftMax.isBefore(rightMin) || leftMax.equals(rightMin));
    }
}