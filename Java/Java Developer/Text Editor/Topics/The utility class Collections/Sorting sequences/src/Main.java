import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void sortInDescendingOrder(List<Integer> sequence) {
        // write your code here
        sequence.sort(Collections.reverseOrder());
    }

    /* Do not change code below */
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final List<Integer> seq = Arrays.stream(scanner.nextLine().split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        sortInDescendingOrder(seq);
        seq.forEach(e -> System.out.print(e + " "));
    }
}