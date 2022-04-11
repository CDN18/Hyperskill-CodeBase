import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        ArrayList<Integer> time = new ArrayList<>();
        for(int i = 0; i < 2 * 3; i++) {
            time.add(scanner.nextInt());
        }
        System.out.println((time.get(3) - time.get(0)) * 60 * 60 +
                (time.get(4) - time.get(1)) * 60 +
                (time.get(5) - time.get(2)));
    }
}