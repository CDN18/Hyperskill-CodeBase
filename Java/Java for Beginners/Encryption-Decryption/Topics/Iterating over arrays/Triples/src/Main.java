import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int[] array = new int[scanner.nextInt()];
        Arrays.setAll(array, i -> scanner.nextInt());
        System.out.println(findTriples(array));
    }

    public static int findTriples(int[] array) {
        final int triple = 3;
        int counter = 0;
        if (array.length < triple) {
            return counter;
        } else {
            for (int i = 0; i < array.length - 2; i++) {
                if (array[i + 1] - array[i] == 1 && array[i + 2] - array[i + 1] == 1) {
                    counter++;
                }
            }
        }
        return counter;
    }
}