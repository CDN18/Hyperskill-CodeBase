import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Main {

    /**
     * Returns joined list elements with '(' for the prefix
     * and ')' for the suffix.
     *
     * @param symbols the input list of String elements
     * @return the result of joining
     */
    public static String asciiArt(List<String> symbols) {
        // write your code here
        return symbols.stream()
                .collect(Collectors.joining("", "(", ")"));
    }

    // Don't change the code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> symbols = Arrays.asList(scanner.nextLine().split("\\s+"));
        System.out.println(asciiArt(symbols));
    }
}