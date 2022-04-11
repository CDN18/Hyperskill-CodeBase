import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        String input = scanner.next();
        System.out.println(input.startsWith("J") || input.startsWith("j"));
    }
}