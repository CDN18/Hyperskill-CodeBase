// do not remove imports
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

class ArrayUtils {
    // define getFirst method here
    static <T> T getFirst(T[] array) {
        return array.length > 0 ? array[0] : null;
    }
}