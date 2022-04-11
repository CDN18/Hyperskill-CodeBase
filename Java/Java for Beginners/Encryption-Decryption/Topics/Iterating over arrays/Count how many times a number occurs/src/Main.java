import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int[] array = new int[scanner.nextInt()];
        Arrays.setAll(array, i -> scanner.nextInt());
        System.out.println(count(array, scanner.nextInt()));
    }

    public static int count(int[] array, int n) {
        int counter = 0;
        for (int num : array) {
            if (num == n) {
                counter++;
            }
        }
        return counter;
    }
}