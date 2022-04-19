import java.io.CharArrayWriter;
import java.io.IOException;

class Converter {
    public static char[] convert(String[] words) throws IOException {
        // implement the method
        CharArrayWriter buffer = new CharArrayWriter();
        for (String string : words) {
            buffer.write(string);
        }
        return buffer.toCharArray();
    }
}