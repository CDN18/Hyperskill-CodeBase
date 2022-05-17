import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        String ip = scanner.nextLine();
        System.out.println(ip.matches("((\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}((\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5]))") ? "YES" : "NO");
        String reg = "\\d{1,3}|1\\d{2}|2[0-4]\\d|25[0-5]";
    }
}