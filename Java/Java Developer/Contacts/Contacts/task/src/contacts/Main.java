package contacts;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static PhoneBook phoneBook;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        phoneBook = loadPhoneBook(args);
        String command;
        while (true) {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            command = scanner.nextLine();
            switch (command) {
                case "add":
                    addMenu();
                    break;
                case "list":
                    phoneBook.list();
                    System.out.println();
                    listMenu();
                    break;
                case "search":
                    search();
                    break;
                case "count":
                    System.out.println("The Phone Book has " + phoneBook.size() + " records.");
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Invalid command!");
            }
            if (command.equals("exit")) {
                break;
            }
            savePhoneBook(args);
            System.out.println();
        }
    }

    private static PhoneBook loadPhoneBook(String[] args) {
        PhoneBook loadedPhoneBook;
        if (args.length == 0) {
            return new PhoneBook();
        } else {
            try (
                    FileInputStream fis = new FileInputStream(args[0]);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    ObjectInputStream ois = new ObjectInputStream(bis)
            ) {
                loadedPhoneBook = (PhoneBook) ois.readObject();
            } catch (Exception e) {
                return new PhoneBook();
            }
        }
        return loadedPhoneBook;
    }

    private static void savePhoneBook(String[] args) {
        if (args.length == 0) {
            return;
        }
        try (
                FileOutputStream fos = new FileOutputStream(args[0]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(phoneBook);
        } catch (Exception e) {
            System.out.println("Error while saving the Phone Book." + e.getMessage());
        }
    }

    public static void addMenu() {
        System.out.print("Enter the type (person, organization): ");
        switch (scanner.nextLine()) {
            case "person":
                phoneBook.addPerson();
                break;
            case "organization":
                phoneBook.addOrganization();
                break;
            default:
                System.out.println("Bad type!");
                break;
        }
        System.out.println("The record added.");
    }

    public static void listMenu() {
        System.out.print("[list] Enter action ([number], back): ");
        String action = scanner.nextLine();
        if ("back".equals(action)) {
            return;
        }
        int index;
        try {
            index = Integer.parseInt(action) - 1;
            if (index < 0 || index > phoneBook.size()) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.out.println("Invalid number!");
            return;
        }
        System.out.println(phoneBook.get(index));
        System.out.println();
        recordMenu(index);
    }

    public static void recordMenu(int index) {
        String command = "";
        while (true) {
            System.out.print("[record] Enter action (edit, delete, menu): ");
            command = scanner.nextLine();
            switch (command) {
                case "edit":
                    phoneBook.edit(index);
                    System.out.println("Saved");
                    System.out.println(phoneBook.get(index));
                    break;
                case "delete":
                    phoneBook.remove(index);
                    System.out.println("The record deleted!");
                    break;
                case "menu":
                    return;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
            System.out.println();
        }
    }

    private static void recordMenu(Contact contact) {
        String command = "";
        while (!"menu".equals(command)) {
            System.out.print("[record] Enter action (edit, delete, menu): ");
            command = scanner.nextLine();
            switch (command) {
                case "edit":
                    phoneBook.edit(contact);
                    System.out.println("Saved");
                    System.out.println(contact);
                    break;
                case "delete":
                    phoneBook.remove(contact);
                    System.out.println("The record deleted!");
                    break;
                case "menu":
                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
            System.out.println();
        }
    }

    public static void search() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        ArrayList<Contact> result = new ArrayList<>();
        for (int i = 0; i < phoneBook.size(); i++) {
            if (phoneBook.get(i).matches(query)) {
                result.add(phoneBook.get(i));
            }
        }
        if (result.size() == 0) {
            System.out.println("No matches found!");
        } else {
            System.out.println("Found " + result.size() + " result" + (result.size() > 1 ? "s" : "") + ": ");
            for (int i = 0; i < result.size(); i++) {
                System.out.println((i + 1) + "." + result.get(i).getBaseInfo());
            }
        }
        searchMenu(result);
    }

    public static void searchMenu(ArrayList<Contact> result) {
        System.out.print("[search] Enter action ([number], back, again): ");
        String command = scanner.nextLine();
        try {
            int index = Integer.parseInt(command) - 1;
            if (index < 0 || index >= result.size()) {
                throw new IllegalArgumentException();
            }
            System.out.println(result.get(index));
            recordMenu(result.get(index));
        } catch (Exception e) {
            if (command.equals("back")) {
                return; // Can't delete
            } else if (command.equals("again")) {
                search();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }
}