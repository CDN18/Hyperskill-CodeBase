import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int N = scanner.nextInt();
        for (int i = 1; i * i <= N; i++) {
            System.out.println(i * i);
        }
    }
}