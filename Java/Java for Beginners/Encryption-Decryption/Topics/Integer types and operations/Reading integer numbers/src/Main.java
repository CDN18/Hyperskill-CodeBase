import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int[] nums = new int[4];
        Arrays.setAll(nums, i -> scanner.nextInt());
        Arrays.stream(nums).forEach(System.out::println);
    }
}