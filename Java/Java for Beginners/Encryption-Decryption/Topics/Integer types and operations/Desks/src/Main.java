import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        int desk = 0;
        while (scanner.hasNext()) {
            int stuNumber = scanner.nextInt();
            desk += stuNumber / 2 + stuNumber % 2;
        }
        System.out.println(desk);
    }
}