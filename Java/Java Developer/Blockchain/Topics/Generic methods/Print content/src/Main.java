// do not remove imports
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

class ArrayUtils {
    // define info method here
    public static <T> String info(T[] array) {
        StringBuilder output = new StringBuilder("[");
        for (T element : array) {
            output.append(element.toString()).append(", ");
        }
        if (output.length() > 1) {
            output.delete(output.length() - 2, output.length()).append("]");
        } else {
            output.append("]");
        }
        return output.toString();
    }
}