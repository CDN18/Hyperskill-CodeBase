import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // write your code here
        int startSeed = scanner.nextInt(); // k
        final int n = scanner.nextInt();
        final double m = scanner.nextDouble();
        boolean found = false;
        while (!found) {
            Random random = new Random(startSeed);
            int counter = 0;
            while (counter < n) {
                double number = random.nextGaussian();
                if (number <= m) {
                    counter++;
                } else {
                    break;
                }
            }
            if (counter == n) {
                found = true;
            } else {
                startSeed++;
            }
        }
        System.out.println(startSeed);
    }
}