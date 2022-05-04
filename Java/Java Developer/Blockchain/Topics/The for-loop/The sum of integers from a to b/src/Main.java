import java.util.Scanner;
import java.util.stream.IntStream;

import static java.lang.Integer.sum;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        System.out.println(IntStream.rangeClosed(a, b).sum());
    }
}