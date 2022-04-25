import java.io.InputStream;

class Main {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = System.in;
        byte[] input = inputStream.readAllBytes();
        for (byte b : input) {
            System.out.print(b);
        }
    }
}