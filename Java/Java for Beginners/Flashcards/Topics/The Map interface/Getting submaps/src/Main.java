import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        SortedMap<Integer, String> map = new TreeMap<>();
        int from = scanner.nextInt();
        int to = scanner.nextInt() + 1;
        scanner.next();
        while (scanner.hasNextLine()) {
            map.put(scanner.nextInt(), scanner.next());
        }
        SortedMap<Integer, String> subMap = map.subMap(from, to);
        for (var entry : subMap.entrySet()) {
            System.out.println(entry.getKey() +  " " + entry.getValue());
        }
    }
}