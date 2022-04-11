import java.util.Scanner;

abstract class Customer {

    public void createAccount() {
        // write your code here ...
        verifyIdentity();
        generateCustomerID();
        sayThankYou();
        sendGift();
    }

    public abstract void generateCustomerID();

    public abstract void sendGift();

    public void verifyIdentity() {
        System.out.println("Verify your identity");
    }

    public void sayThankYou() {
        System.out.println("Thank you for creating a new customer account!");
    }
}

class Premium extends Customer {
    // write your code here ...

    @Override
    public void generateCustomerID() {
        System.out.println("Your premium account identification number: PA-01");
    }

    @Override
    public void sendGift() {
        System.out.println("You received 100 Gems");
    }
}

class Standard extends Customer {
    // write your code here ...

    @Override
    public void generateCustomerID() {
        System.out.println("Your standard account identification number: ST-01");
    }

    @Override
    public void sendGift() {
        System.out.println("You received 50 Gems");
    }
}

//Do not change the code below
class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String type = scanner.nextLine();
        scanner.close();
        Customer customer = null;
        if ("premium".equalsIgnoreCase(type)) {
            customer = new Premium();
        } else if ("standard".equalsIgnoreCase(type)) {
            customer = new Standard();
        } else {
            System.out.println("Error!");
            System.exit(0);
        }
        customer.createAccount();
    }
}