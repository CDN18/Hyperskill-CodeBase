class ConsoleWriter implements Printer, Notifier {
    // override greeting method according to Printer default implementation
    @Override
    public void greeting() {
        Printer.super.greeting();
    }
}

interface Printer {
    default void greeting() {
        System.out.println("Printer is ready");
    }
}

interface Notifier {
    default void greeting() {
        System.out.println("Notifier is ready");
    }
}