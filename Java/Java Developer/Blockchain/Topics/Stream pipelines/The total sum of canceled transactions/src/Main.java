import java.util.*;

class TransactionExample {

    /**
     * Calculates the general sum of canceled transactions for all non empty accounts in the list
     */
    public static long calcSumOfCanceledTransOnNonEmptyAccounts(List<Account> accounts) {
        // write your code here
        return accounts.stream().filter(acc -> acc.getBalance() > 0)
                .flatMap(acc -> acc.getTransactions().stream())
                .filter(transaction -> transaction.getState() == State.CANCELED)
                .map(Transaction::getSum).mapToLong(Long::longValue).sum();
    }

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int numberOfAccounts = Integer.parseInt(scanner.nextLine());
        List<Account> accounts = new ArrayList<>();

        for (int i = 0; i < numberOfAccounts; i++) {
            String[] accDesc = scanner.nextLine().split(" ");
            String number = accDesc[0];
            Long balance = Long.parseLong(accDesc[1]);

            int numberOfTransByAccount = Integer.parseInt(scanner.nextLine());
            List<Transaction> trans = new ArrayList<>();

            for (int j = 0; j < numberOfTransByAccount; j++) {
                String[] transDesc = scanner.nextLine().split(" ");
                String uuid = transDesc[0];
                State state = convertStringToState(transDesc[1]);
                Long sum = Long.parseLong(transDesc[2]);
                trans.add(new Transaction(uuid, state, sum));
            }

            accounts.add(new Account(number, balance, trans));
        }

        System.out.println(calcSumOfCanceledTransOnNonEmptyAccounts(accounts));
    }

    private static State convertStringToState(String state) {
        switch (state) {
            case "c": return State.CANCELED;
            case "f": return State.FINISHED;
            case "p": return State.PROCESSING;
            default: throw new IllegalArgumentException("Unknown account state");
        }
    }

    enum State {
        FINISHED, CANCELED, PROCESSING
    }

    static class Transaction {

        private final String uuid;
        private final State state;
        private final Long sum;

        public Transaction(String uuid, State state, Long sum) {
            this.uuid = uuid;
            this.state = state;
            this.sum = sum;
        }

        public State getState() {
            return state;
        }

        public Long getSum() {
            return sum;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "uuid='" + uuid + '\'' +
                    ", state=" + state +
                    ", sum=" + sum +
                    '}';
        }
    }

    static class Account {

        private final String number;
        private final Long balance;
        private final List<Transaction> transactions;

        public Account(String number, Long balance, List<Transaction> transactions) {
            this.number = number;
            this.balance = balance;
            this.transactions = transactions;
        }

        public Long getBalance() {
            return balance;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "number='" + number + '\'' +
                    ", balance=" + balance +
                    ", transactions=" + transactions +
                    '}';
        }
    }
}