import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // write your code here
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int[][] array = new int[b - a + 1][n];
        ArrayList<Integer> max = new ArrayList<>(b - a + 1);
        for (int i = a; i <= b; i++) {
            Random random = new Random(i);
            Arrays.setAll(array[i - a], index -> random.nextInt(k));
            int maxnum = array[i - a][0];
            for (int num : array[i - a]) {
                if (num > maxnum) {
                    maxnum = num;
                }
            }
            max.add(maxnum);
        }
        int minInMax = max.get(0);
        for (int num : max) {
            if (num < minInMax) {
                minInMax = num;
            }
        }
        System.out.println(max.indexOf(minInMax) + a);
        System.out.println(minInMax);
    }
}