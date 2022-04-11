import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();
        while (a % 3 != 0) {
            a++;
        }
        while (b % 3 != 0) {
            b--;
        }
        double result = (a + b) / 2;
        System.out.println(result);
    }
}