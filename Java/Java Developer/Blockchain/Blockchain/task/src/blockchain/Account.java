package blockchain;

public interface Account {
    int isMiner();
    String getName();
    int getBalance();
    Transactions signTransaction(Account to, int amount);
    boolean submitTransaction(Account to, int amount);
}
