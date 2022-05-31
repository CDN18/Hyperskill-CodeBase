import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        switch (scanner.nextLine()) {
            case "triangle":
                double a = Double.parseDouble(scanner.nextLine());
                double b = Double.parseDouble(scanner.nextLine());
                double c = Double.parseDouble(scanner.nextLine());
                double p = (a + b + c) / 2;
                System.out.println(Math.sqrt(p * (p - a) * (p - b) * (p - c)));
                break;
            case "rectangle" :
                double a1 = Double.parseDouble(scanner.nextLine());
                double b1 = Double.parseDouble(scanner.nextLine());
                System.out.println(a1 * b1);
                break;
            case "circle" :
                double r = Double.parseDouble(scanner.nextLine());
                System.out.println(3.14 * r * r);
                break;
        }
    }
}