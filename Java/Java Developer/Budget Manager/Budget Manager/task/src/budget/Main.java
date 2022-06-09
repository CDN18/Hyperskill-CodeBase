package budget;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String dataFile = "purchases.txt";

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        AccountBook accountBook = new AccountBook();
        String selection = "";
        while (!"0".equals(selection)) {
            showMenu();
            selection = scanner.nextLine();
            System.out.println();
            switch (selection) {
                case "1": // Add Income
                    addIncome(scanner, accountBook);
                    break;
                case "2": // Add Purchase
                    addPurchase(scanner, accountBook);
                    break;
                case "3": // Show List of Purchases
                    showPurchaseList(scanner, accountBook);
                    break;
                case "4": // Balance
                    System.out.println("Balance: " + accountBook.currency + accountBook.balance);
                    break;
                case "5": // Save
                    AccountBook.save(accountBook, dataFile);
                    System.out.println("Saved!");
                    break;
                case "6": // Load
                    accountBook = AccountBook.load(dataFile);
                    System.out.println("Purchases were loaded!");
                    break;
                case "7": // Analyze (Sort)
                    analyze(scanner, accountBook);
                    break;
                case "0":
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Incorrect input!");
            }
            System.out.println();
            // TODO: save data
        }
    }

    private static void showMenu() {
        System.out.println("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
    }

    private static void addIncome(Scanner scanner, AccountBook accountBook) {
        System.out.println("Enter income:");
        try {
            double income = Double.parseDouble(scanner.nextLine());
            accountBook.add(new Transaction(income));
            System.out.println("Income was added!");
        } catch (NumberFormatException e) {
            System.err.println("Income should be a number!");
        }
    }

    private static void addPurchase(Scanner scanner, AccountBook accountBook) {
        Category category = null;
        String input = "";
        while (!"5".equals(input)) {
            System.out.println("Choose type of purchase\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other\n" +
                    "5) Back");
            input = scanner.nextLine();
            System.out.println();
            switch (input) {
                case "1":
                    category = Category.FOOD;
                    break;
                case "2":
                    category = Category.CLOTHES;
                    break;
                case "3":
                    category = Category.ENTERTAINMENT;
                    break;
                case "4":
                    category = Category.OTHER;
                    break;
                case "5":
                    continue;
                default:
                    System.out.println("Incorrect input!");
            }
            System.out.println("Enter purchase name:");
            String name = scanner.nextLine();
            System.out.println("Enter its price:");
            try {
                double price = Double.parseDouble(scanner.nextLine());
                accountBook.add(new Transaction(-price, name, category));
                System.out.println("Purchase was added!");
            } catch (NumberFormatException e) {
                System.err.println("Price should be a number!");
            }
            System.out.println();
        }
    }

    private static void showPurchaseList(Scanner scanner, AccountBook accountBook) {
        String input = "";
        Category category = null;
        while (!"6".equals(input)) {
            System.out.println("Choose type of purchase\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other\n" +
                    "5) All\n" +
                    "6) Back");
            input = scanner.nextLine();
            System.out.println();
            switch (input) {
                case "1":
                    category = Category.FOOD;
                    break;
                case "2":
                    category = Category.CLOTHES;
                    break;
                case "3":
                    category = Category.ENTERTAINMENT;
                    break;
                case "4":
                    category = Category.OTHER;
                    break;
                case "5":
                    category = Category.ALL;
                    break;
                case "6":
                    continue;
                default:
                    System.out.println("Incorrect input!");
            }
            Transaction[] purchaseList = accountBook.getPurchaseList(category);
            if (purchaseList == null || purchaseList.length == 0) {
                System.out.println("The purchase list is empty");
            } else {
                assert category != null;
                System.out.println(category.name + ":");
                for (Transaction transaction : purchaseList) {
                    System.out.printf("%s %s%.2f\n", transaction.getName(), accountBook.currency, transaction.getAmount());
                }
                System.out.println("Total sum: " + accountBook.currency + Arrays.stream(purchaseList).mapToDouble(Transaction::getAmount).sum());
                System.out.println();
            }
        }
    }

    private static void analyze(Scanner scanner, AccountBook accountBook) {
        String input = "";
        while (!"4".equals(input)) {
            System.out.println("How do you want to sort?\n" +
                    "1) Sort all purchases\n" +
                    "2) Sort by type\n" +
                    "3) Sort certain type\n" +
                    "4) Back");
            input = scanner.nextLine();
            System.out.println();
            switch (input) {
                case "1":
                    accountBook.analyze(Category.ALL);
                    break;
                case "2":
                    accountBook.analyze();
                    break;
                case "3":
                    Category category;
                    System.out.println("Choose the type of purchase\n" +
                            "1) Food\n" +
                            "2) Clothes\n" +
                            "3) Entertainment\n" +
                            "4) Other");
                    String categoryInput = scanner.nextLine();
                    System.out.println();
                    switch (categoryInput) {
                        case "1":
                            category = Category.FOOD;
                            break;
                        case "2":
                            category = Category.CLOTHES;
                            break;
                        case "3":
                            category = Category.ENTERTAINMENT;
                            break;
                        case "4":
                            category = Category.OTHER;
                            break;
                        default:
                            System.out.println("Incorrect input!");
                            continue;
                    }
                    accountBook.analyze(category);
                    break;
                case "4":
                    continue;
                default:
                    System.out.println("Incorrect input!");
            }
            System.out.println();
        }
    }
}
