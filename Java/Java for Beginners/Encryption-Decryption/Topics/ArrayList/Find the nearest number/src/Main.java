import java.util.*;

public class Main {
    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> array = new ArrayList<>();
        while (scanner.hasNext()) {
            array.add(scanner.nextInt());
        }
        int n = array.get(array.size() - 1);
        array.remove(array.size() - 1);
        int distance = 0;
        int index1;
        int index2;
        while (true) {
            if (array.contains(n + distance) || array.contains(n - distance)) {
                index1 = array.indexOf(n - distance);
                index2 = array.indexOf(n + distance);
                break;
            }
            distance++;
        }
        String output = "";
        if (index1 != -1) {
            for (Integer num : array) {
                if (num.equals(array.get(index1))) {
                    output += num + " ";
                }
            }
        }
        if (index2 != -1 && index1 != index2) {
            for (Integer num : array) {
                if (num.equals(array.get(index2))) {
                    output += num + " ";
                }
            }
        }
        System.out.println(output.trim());
    }
}