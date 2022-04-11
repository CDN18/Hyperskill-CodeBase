import java.util.*;

public class Main {
    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        String vowels = "aeiouy";
        int counter = 0;
        int vowCounter = 0;
        int conCounter = 0;
        for (int i = 0; i < word.length(); i++) {
            if (vowels.contains(String.valueOf(word.charAt(i)))) {
                conCounter = 0;
                vowCounter++;
                if (vowCounter == 3) {
                    counter++;
                    vowCounter = 1;
                }
            } else {
                vowCounter = 0;
                conCounter++;
                if (conCounter == 3) {
                    counter++;
                    conCounter = 1;
                }
            }
        }
        System.out.println(counter);
    }
}