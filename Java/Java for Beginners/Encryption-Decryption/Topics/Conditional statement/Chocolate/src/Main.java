import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int length = scanner.nextInt();
        int width = scanner.nextInt();
        int segment = scanner.nextInt();
        if (segment <= length * width && segment > 0) {
            if (segment % length == 0 || segment % width == 0) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        } else {
            System.out.println("NO");
        }
    }
}