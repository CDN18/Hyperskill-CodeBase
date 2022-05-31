import java.util.*;

public class Main {

    public static void processIterator(String[] array) {
        // write your code here
        ListIterator<String> listIterator = new ArrayList<>(Arrays.asList(array)).listIterator();
        while (listIterator.hasNext()) {
            String next = listIterator.next();
            if (!next.startsWith("J")) {
                listIterator.remove();
            } else {
                listIterator.set(next.substring(1));
            }
        }
        while (listIterator.hasPrevious()) {
            System.out.println(listIterator.previous());
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processIterator(scanner.nextLine().split(" "));
    }
}