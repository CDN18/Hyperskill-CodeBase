import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        switch (scanner.nextLine()) {
            case "gryffindor":
                System.out.println("bravery");
                break;
            case "hufflepuff":
                System.out.println("loyalty");
                break;
            case "ravenclaw":
                System.out.println("intellect");
                break;
            case "slytherin":
                System.out.println("cunning");
                break;
            default:
                System.out.println("not a valid house");
                break;
        }
    }
}