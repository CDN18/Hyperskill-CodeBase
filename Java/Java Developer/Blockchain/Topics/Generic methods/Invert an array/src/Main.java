// do not remove imports
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

class ArrayUtils {
    // define invert method here
    public static <T> T[] invert(T[] array) {
        ArrayList<T> result = new ArrayList<>();
        for (int i = array.length - 1; i >= 0; i--) {
            result.add(array[i]);
        }
        return result.toArray(array.clone());
    }
}