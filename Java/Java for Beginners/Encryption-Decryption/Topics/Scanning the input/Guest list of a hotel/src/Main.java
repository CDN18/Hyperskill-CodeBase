import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        ArrayList<String> guests = new ArrayList<>();
        while (scanner.hasNext()) {
            guests.add(scanner.next());
        }
        for (int i = guests.size() - 1; i >= 0; i--) {
            System.out.println(guests.get(i));
        }
    }
}