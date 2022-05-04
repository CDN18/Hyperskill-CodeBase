import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

class TransactionCollector {

    public static Map<String, Long> getAccount2TransSum(List<Transaction> trans) {
        return trans.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getAccount().getNumber(),
                        Collectors.summingLong(Transaction::getSum)));
    }

    static class Transaction {

        private final String uuid;
        private final Long sum;
        private final Account account;

        public Transaction(String uuid, Long sum, Account account) {
            this.uuid = uuid;
            this.sum = sum;
            this.account = account;
        }

        public String getUuid() {
            return uuid;
        }

        public Long getSum() {
            return sum;
        }

        public Account getAccount() {
            return account;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "uuid='" + uuid + '\'' +
                    ", sum=" + sum +
                    '}';
        }
    }

    static class Account {
        private final String number;
        private final Long balance;

        public Account(String number, Long balance) {
            this.number = number;
            this.balance = balance;
        }

        public String getNumber() {
            return number;
        }

        public Long getBalance() {
            return balance;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "number='" + number + '\'' +
                    ", balance=" + balance +
                    '}';
        }
    }

}