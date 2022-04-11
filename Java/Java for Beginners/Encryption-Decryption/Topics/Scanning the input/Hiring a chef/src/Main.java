import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        String firstName = scanner.nextLine();
        int exp = scanner.nextInt();
        String cuisine = scanner.next();
        System.out.println("The form for " + firstName + " is completed. We will contact you "
        	+ "if we need a chef who cooks " + cuisine + " dishes and has "
            + exp + " years of experience.");
    }
}
