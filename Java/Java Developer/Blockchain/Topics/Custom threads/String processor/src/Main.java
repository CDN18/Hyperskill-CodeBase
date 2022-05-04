import java.util.Locale;
import java.util.Scanner;

class StringProcessor extends Thread {

    final Scanner scanner = new Scanner(System.in); // use it to read string from the standard input

    @Override
    public void run() {
        // implement this method
        boolean finished = false;
        while (!finished) {
            String input = scanner.nextLine();
            if (input.equals(input.toUpperCase())) {
                System.out.println("FINISHED");
                finished = true;
            } else {
                System.out.println(input.toUpperCase());
            }
        }
    }
}