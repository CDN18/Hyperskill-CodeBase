import java.util.Comparator;
import java.util.List;

class Utils {

    public static void sortStrings(List<String> strings) {
        // your code here
        strings.sort(Comparator.reverseOrder());
    }
}