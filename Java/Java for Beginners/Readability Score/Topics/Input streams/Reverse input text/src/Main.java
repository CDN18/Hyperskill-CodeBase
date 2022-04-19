import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        StringBuilder input = new StringBuilder();
        int ch = reader.read();
        while (ch != -1) {
            input.append((char) ch);
            ch = reader.read();
        }
        System.out.println(input.reverse());
        reader.close();
    }
}