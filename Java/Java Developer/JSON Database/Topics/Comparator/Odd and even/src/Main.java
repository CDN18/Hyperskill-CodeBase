import java.util.Collections;
import java.util.List;

class Utils {

    public static List<Integer> sortOddEven(List<Integer> numbers) {
        List<Integer> odd = numbers.stream()
            .filter(n -> n % 2 == 1 || n % 2 == -1)
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        List<Integer> even = numbers.stream()
            .filter(n -> n % 2 == 0)
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        Collections.reverse(even);
        odd.addAll(even);
        Collections.copy(numbers, odd);
        return numbers;
    }
}