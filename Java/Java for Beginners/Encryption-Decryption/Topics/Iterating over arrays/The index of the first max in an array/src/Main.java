import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        scanner.nextInt();
        ArrayList<Integer> array = new ArrayList<>();
        while (scanner.hasNext()) {
            array.add(scanner.nextInt());
        }
        Integer max = array.get(0);
        for (Integer element : array) {
            if (element > max) {
                max = element;
            }
        }
        System.out.println(array.indexOf(max));
    }
}