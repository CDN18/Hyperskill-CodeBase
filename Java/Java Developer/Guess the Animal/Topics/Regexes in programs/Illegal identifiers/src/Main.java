import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numberOfVariables = Integer.parseInt(scanner.nextLine());

        String regex = "([A-Za-z]\\w*)|(_[A-Za-z\\d]\\w*)";

        for (int i = 0; i < numberOfVariables; i++) {
            String identifier = scanner.nextLine();
            if (!identifier.matches(regex)) {
                System.out.println(identifier);
            }
        }
    }
}