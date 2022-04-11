import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        int prev = scanner.nextInt();
        int next = scanner.nextInt();
        if (prev > next) {
            System.out.println(next >= scanner.nextInt());
        }
        else if (prev < next) {
            System.out.println(next <= scanner.nextInt());
        }
        else {
            System.out.println(true);
        }
    }
}