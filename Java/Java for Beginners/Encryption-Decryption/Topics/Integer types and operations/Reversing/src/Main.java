import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        int origNum = scanner.nextInt();
        if (origNum % 10 == 0) {
            origNum /= 10;
        }
        while (origNum != 0) {
            System.out.print(origNum % 10);
            origNum /= 10;
        }
    }
}
