import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        ArrayList<String> input = new ArrayList<>(Arrays.asList(reader.readLine().split(" ")));
        reader.close();
        input.removeIf(""::equals);
        System.out.println(input.size());
    }
}