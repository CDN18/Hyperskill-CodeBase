import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        Deque<Integer> stack = new ArrayDeque<>(num);
        Deque<Integer> max = new ArrayDeque<>(num);
        int currentMax = Integer.MIN_VALUE;
        max.push(currentMax);
        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "push":
                    stack.push(scanner.nextInt());
                    if (stack.peek() > currentMax) {
                        currentMax = stack.peek();
                    }
                    max.push(currentMax);
                    break;
                case "pop":
                    if (!stack.isEmpty()) {
                        stack.pop();
                        max.pop();
                        currentMax = max.peek();
                    }
                    break;
                case "max":
                    System.out.println(max.peek());
                    break;
                default:
            }
        }
    }
}