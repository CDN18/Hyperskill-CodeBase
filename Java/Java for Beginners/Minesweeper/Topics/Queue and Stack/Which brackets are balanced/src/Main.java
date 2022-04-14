import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(isBalanced(input));
    }

    public static boolean isBalanced(String input) {
        char[] inputChars = input.toCharArray();
        Deque<Character> brackets = new ArrayDeque<>(input.length());
        for (char inputChar : inputChars) {
            switch (inputChar) {
                case '(':
                case '[':
                case '{':
                    brackets.push(inputChar);
                    break;
                case ')':
                    if (brackets.isEmpty() || brackets.peek() != '(') {
                        return false;
                    } else {
                        brackets.pop();
                        break;
                    }
                case ']':
                    if (brackets.isEmpty() || brackets.peek() != '[') {
                        return false;
                    } else {
                        brackets.pop();
                        break;
                    }
                case '}':
                    if (brackets.isEmpty() || brackets.peek() != '{') {
                        return false;
                    } else {
                        brackets.pop();
                        break;
                    }
                default:
            }
        }
        return brackets.isEmpty();
    }
}