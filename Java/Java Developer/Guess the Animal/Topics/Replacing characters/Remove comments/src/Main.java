import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String codeWithComments = scanner.nextLine();

        // write your code here

        String codeWithoutComments = codeWithComments.replaceAll("/\\*.*?\\*/", "")
                .replaceAll("//.*$", "");
        System.out.println(codeWithoutComments);
    }
}