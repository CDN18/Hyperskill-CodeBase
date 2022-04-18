package flashcards;

import java.util.Scanner;

public class LogUtil {
    public Scanner logInput;
    public StringBuilder log;

    {
        logInput = new Scanner(System.in);
        log = new StringBuilder();
    }

    public String ScanNextLine() {
        String input = logInput.nextLine();
        log.append(input).append("\n");
        return input;
    }

    public void Output(String output) {
        System.out.println(output);
        log.append(output);
        if (output.charAt(output.length() - 1) != '\n') {
            log.append("\n");
        }
    }
}
