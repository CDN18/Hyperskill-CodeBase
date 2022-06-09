package budget;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class AccountBook implements Serializable {
    private static final long serialVersionUID = 1L;
    double balance;
    ArrayList<Transaction> transactions;
    Currency currency;

    public AccountBook() {
        balance = 0;
        transactions = new ArrayList<>();
        currency = Currency.USD;
    }

    public void add(Transaction transaction) {
        transactions.add(transaction);
        balance += transaction.getAmount();
    }

    public Transaction[] getPurchaseList(Category category) {
        Stream<Transaction> purchaseList = transactions.stream().filter(t -> t.getAmount() < 0)
                .map(t -> new Transaction(-t.getAmount(), t.getName(), t.category));
        if (category != Category.ALL && category != null) {
            purchaseList = purchaseList.filter(t -> t.category == category);
        }
        return purchaseList.toArray(Transaction[]::new);
    }

    static void save(AccountBook accountBook, String fileName) {
        File file = new File(fileName);
        try(PrintWriter writer = new PrintWriter(file)) {
            writer.printf("version: %s\n", serialVersionUID);
            writer.printf("balance: %.2f\n", accountBook.balance);
            writer.printf("currency: %s\n", accountBook.currency);
            writer.println("transactions:");
            for (Transaction transaction : accountBook.transactions) {
                writer.printf("Name: %s\n", transaction.getName());
                writer.printf("Amount: %.2f\n", transaction.getAmount());
                writer.printf("Category: %s\n", transaction.category);
            }
        } catch (Exception e) {
            System.err.println("Error while saving data");
        }
    }

    static AccountBook load(String fileName) {
        File file = new File(fileName);
        AccountBook accountBook = new AccountBook();
        try(Scanner reader = new Scanner(file)) {
            long version = Long.parseLong(reader.nextLine().split(": ")[1]);
            if (version != serialVersionUID) {
                System.err.println("Incorrect version of file");
                return accountBook;
            }
            double allegedBalance = Double.parseDouble(reader.nextLine().split(": ")[1]);
            accountBook.currency = Currency.parse(reader.nextLine().split(": ")[1]);
            reader.nextLine(); // skip transactions header
            while (reader.hasNextLine()) {
                String name = reader.nextLine().substring(6);
                double amount = Double.parseDouble(reader.nextLine().substring(8));
                Category category = Category.parse(reader.nextLine().substring(10));
                accountBook.add(new Transaction(amount, name, category));
                // System.out.println("[DEBUG] added: " + name + ": " + amount + " " + category);
            }
            if (accountBook.balance != allegedBalance) {
                System.err.println("Balance is inconsistent with transactions, taking the balance from transactions");
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found. " + fileName);
        } catch (Exception e) {
            System.err.println("Error while loading data. " + e.getMessage());
        }
        return accountBook;
    }


    public void analyze() {
        double totalSum = Arrays.stream(getPurchaseList(Category.ALL)).mapToDouble(Transaction::getAmount).sum();
        double foodSum = Arrays.stream(getPurchaseList(Category.FOOD)).mapToDouble(Transaction::getAmount).sum();
        double clothesSum = Arrays.stream(getPurchaseList(Category.CLOTHES)).mapToDouble(Transaction::getAmount).sum();
        double entertainmentSum = Arrays.stream(getPurchaseList(Category.ENTERTAINMENT)).mapToDouble(Transaction::getAmount).sum();
        double otherSum = Arrays.stream(getPurchaseList(Category.OTHER)).mapToDouble(Transaction::getAmount).sum();
        Map<Category, Double> sum = new LinkedHashMap<>(4);
        sum.put(Category.FOOD, foodSum);
        sum.put(Category.CLOTHES, clothesSum);
        sum.put(Category.ENTERTAINMENT, entertainmentSum);
        sum.put(Category.OTHER, otherSum);
        sum.entrySet().stream().sorted(Map.Entry.<Category, Double>comparingByValue().reversed()).forEach(e -> System.out.printf("%s - %s%.2f\n", e.getKey(), currency, e.getValue()));
        System.out.printf("Total: %s%.2f\n", currency, totalSum);
    }
    public void analyze(Category category) {
        Transaction[] purchaseList = getPurchaseList(category);
        if (purchaseList.length == 0) {
            System.out.println("The purchase list is empty!");
            return;
        }
        System.out.println(category + ":");
        Arrays.stream(purchaseList).sorted(Comparator.comparing(Transaction::getAmount).reversed()).forEach(t -> System.out.printf("%s %s%.2f\n", t.getName(), currency, t.getAmount()));
    }
}

enum Currency {
    USD("$"),
    CNY("¥"),
    HKD("HK$"),
    EUR("€"),
    JPY("JP¥"),
    RUB("₽");

    final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static Currency parse(String currency) {
        switch (currency) {
            case "$":
                return Currency.USD;
            case "¥":
                return Currency.CNY;
            case "HK$":
                return Currency.HKD;
            case "€":
                return Currency.EUR;
            case "JP¥":
                return Currency.JPY;
            case "₽":
                return Currency.RUB;
            default:
                return null;
        }
    }
}