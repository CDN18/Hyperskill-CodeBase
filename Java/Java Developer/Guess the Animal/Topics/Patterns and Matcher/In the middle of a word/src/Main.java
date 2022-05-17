import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();

        // write your code here
        Pattern pattern = Pattern.compile("\\w+" + part + "\\w+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        System.out.println(matcher.find() ? "YES" : "NO");
    }
}