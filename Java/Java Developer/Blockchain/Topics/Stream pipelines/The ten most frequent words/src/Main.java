import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(scanner.nextLine());
        }
        List<String> words = Stream.of(input.toString().toLowerCase().split("\\W+"))
                .filter(word -> word.length() > 0).collect(Collectors.toList());
        words.stream().distinct().sorted(Comparator.naturalOrder())
                .sorted(Comparator.comparingInt(w -> Collections.frequency(words, w)).reversed())
                .limit(10).forEach(System.out::println);
    }
}