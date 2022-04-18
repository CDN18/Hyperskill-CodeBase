import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        scanner.nextLine();
        Set<String> dictionary = new HashSet<>(num);
        for (int i = 0; i < num; i++) {
            dictionary.add(scanner.nextLine().toLowerCase());
        }
        scanner.nextLine();
        Set<String> errorWord = new HashSet<>();
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (!dictionary.contains(word.toLowerCase())) {
                errorWord.add(word);
            }
        }
        for (String word : errorWord) {
            System.out.println(word);
        }
    }
}