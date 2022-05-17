import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner sc = new Scanner(System.in);
        LocalDateTime range1 = LocalDateTime.parse(sc.nextLine());
        LocalDateTime range2 = LocalDateTime.parse(sc.nextLine());
        if (range1.isAfter(range2)) {
            LocalDateTime temp = range1;
            range1 = range2;
            range2 = temp;
        }
        int number = Integer.parseInt(sc.nextLine());
        int count = 0;
        for (int i = 0; i < number; i++) {
            LocalDateTime time = LocalDateTime.parse(sc.nextLine());
            if (time.isAfter(range1) && time.isBefore(range2)) {
                count++;
            } else if (time.isEqual(range1)) {
                count++;
            }
        }
        System.out.println(count);
    }
}