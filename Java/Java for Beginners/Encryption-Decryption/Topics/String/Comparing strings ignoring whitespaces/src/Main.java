import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        System.out.println(scanner.nextLine().replace(" ", "").
                equals(scanner.nextLine().replace(" ", "")));
    }
}