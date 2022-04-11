import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        long product = scanner.nextLong();
        int factNum = 1;
        long factProduct = 1;
        while (factProduct <= product) {
            factProduct *= factNum++;
        }
        System.out.println(--factNum);
    }
}