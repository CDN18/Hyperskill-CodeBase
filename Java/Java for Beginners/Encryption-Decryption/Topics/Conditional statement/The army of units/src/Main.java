import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int armyUnits = scanner.nextInt();
        if (armyUnits < 1) {
            System.out.println("no army");
        } else if (armyUnits >= 1 && armyUnits <= 19) {
            System.out.println("pack");
        } else if (armyUnits >= 20 && armyUnits <= 249) {
            System.out.println("throng");
        } else if (armyUnits >= 250 && armyUnits <= 999) {
            System.out.println("zounds");
        } else {
            System.out.println("legion");
        }
    }
}