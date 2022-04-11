import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int[] array = new int[scanner.nextInt()];
        Arrays.setAll(array, i -> scanner.nextInt());
        System.out.println(minimum(array));
    }

    public static int minimum(int[] array) {
        int minimum = array[0];
        for (int element : array) {
            if (element < minimum) {
                minimum = element;
            }
        }
        return minimum;
    }
}