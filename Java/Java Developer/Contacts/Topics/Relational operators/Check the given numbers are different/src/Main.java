import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        ArrayList<Integer> numbers = new ArrayList<>();
        while (scanner.hasNextInt()) {
            numbers.add(scanner.nextInt());
        }
        System.out.println(numbers.stream().distinct().count() == numbers.size());
    }
}