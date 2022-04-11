import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        long num1 = scanner.nextLong();
        char operator = scanner.next().charAt(0);
        long num2 = scanner.nextLong();
        switch (operator) {
            case '+':
                System.out.println(num1 + num2);
                break;
            case '-':
                System.out.println(num1 - num2);
                break;
            case '*':
                System.out.println(num1 * num2);
                break;
            case '/':
                if (num2 == 0) {
                    System.out.println("Division by 0!");
                    break;
                }
                System.out.println(num1 / num2);
                break;
            default:
                System.out.println("Unknown operator");
        }
    }
}