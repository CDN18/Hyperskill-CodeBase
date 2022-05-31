import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void removeElementsGreaterThanValue(Iterator<Long> iterator, Long val) {
        // write your code here
        while (iterator.hasNext()) {
            if (iterator.next() > val) {
                iterator.remove();
            }
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final List<Long> list = Arrays.stream(scanner.nextLine().split("\\s+"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        final Long edge = Long.parseLong(scanner.nextLine());
        removeElementsGreaterThanValue(list.iterator(), edge);
        list.forEach(e -> System.out.print(e + " "));
    }
}