import java.util.function.*;

class FunctionUtils {

    public static Supplier<Integer> getInfiniteRange() {
        // write your code here
        var counter = new Object() {
            int counter = 0;
        };
        return () -> counter.counter++;
    }

}