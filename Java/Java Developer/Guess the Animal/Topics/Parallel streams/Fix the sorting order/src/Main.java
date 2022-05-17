import java.util.Arrays;
import java.util.Scanner;

class ParallelMapping {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Arrays.stream(scanner.nextLine().split("\\s+"))
                .map(Integer::parseInt)
                .sorted()
                .map(n -> n * 2)
                //.parallel()
                .forEach(System.out::println);
    }
}