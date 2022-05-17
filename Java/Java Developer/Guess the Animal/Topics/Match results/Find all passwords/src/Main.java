import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        // write your code here
        Pattern pattern = Pattern.compile("password:?\\s*([A-Za-z\\d]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        boolean isFound = matcher.find();
        if (!isFound) {
            System.out.println("No passwords found.");
        } else {
            while (isFound) {
                System.out.println(matcher.group(1));
                isFound = matcher.find();
            }
        }
    }
}