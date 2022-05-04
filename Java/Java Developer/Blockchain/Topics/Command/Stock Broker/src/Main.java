public class Main {

    public static void main(String[] args) {

        /* write your code here */
        Broker broker = new Broker();
        Stock stock = new Stock();

        Command buyCommand = new BuyCommand(stock);
        Command sellCommand = new SellCommand(stock);


        broker.setCommand(buyCommand);
        broker.executeCommand();

        broker.setCommand(sellCommand);
        broker.executeCommand();
    }
}


class Stock {

    public void buy() {
        System.out.println("Stock was bought");
    }

    public void sell() {
        System.out.println("Stock was sold");
    }
}

interface Command {
    /* write your code here */
    void execute();
}

class BuyCommand implements Command {
    private Stock stock;

    public BuyCommand(Stock stock) {
        this.stock = stock;
    }

    /* write your code here */
    @Override
    public void execute() {
        stock.buy();
    }
}

class SellCommand implements Command {
    private Stock stock;

    public SellCommand(Stock stock) {
        this.stock = stock;
    }

    /* write your code here */
    @Override
    public void execute() {
        stock.sell();
    }
}

class Broker {
    private Command command;
    public void setCommand(Command command) {
        this.command = command;
    }
    public void executeCommand() {
        /* write your code here */
        command.execute();
    }
}