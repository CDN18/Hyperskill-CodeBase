package budget;

public class Transaction {
    String name;
    double amount;
    Category category;

    public Transaction(double amount, String name, Category category) {
        this.amount = amount;
        this.name = name;
        this.category = category;
    }

    public Transaction(double amount) {
        this(amount, "", null);
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}

enum Category {
    FOOD("Food"),
    CLOTHES("Clothes"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other"),
    ALL("All"); // ALL is a special category that represents all categories

    final String name;

    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Category parse(String category) {
        switch (category) {
            case "Food":
                return Category.FOOD;
            case "Clothes":
                return Category.CLOTHES;
            case "Entertainment":
                return Category.ENTERTAINMENT;
            case "Other":
                return Category.OTHER;
            case "All":
                return Category.ALL;
            default:
                return null;
        }
    }
}
