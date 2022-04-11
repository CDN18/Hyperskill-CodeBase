import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int choice = scanner.nextInt();
        String shape = "";
        switch(choice) {
            case 1:
                shape = "square";
                break;
            case 2:
                shape = "circle";
                break;
            case 3:
                shape = "triangle";
                break;
            case 4:
                shape = "rhombus";
                break;
            default:
                //
        }
        if ("".equals(shape)) {
            System.out.println("There is no such shape!");
        } else {
            System.out.printf("You have chosen a %s", shape);
        }
    }
}