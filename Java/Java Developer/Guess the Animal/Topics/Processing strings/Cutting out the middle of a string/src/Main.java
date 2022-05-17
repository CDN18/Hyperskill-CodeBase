import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input.length() % 2) {
            case 0:
                System.out.println(input.substring(0, input.length() / 2 - 1)  + input.substring(input.length() / 2 + 1));
                break;
            case 1:
                System.out.println(input.substring(0, input.length() / 2) + input.substring(input.length() / 2 + 1));
                break;
            default:
        }
    }
}