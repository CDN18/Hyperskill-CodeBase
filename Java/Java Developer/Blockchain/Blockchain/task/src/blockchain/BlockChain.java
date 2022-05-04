package blockchain;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static blockchain.Config.*;

public class BlockChain implements Serializable {
    private static final long serialVersionUID = GENERAL_SERIAL_UID;
    private ArrayList<Block> chain;
    volatile int zeros;
    private ArrayDeque<Transactions> currentData;
    private List<Transactions> currentPackagedData;
    private volatile int transactionIdentifier;
    private ArrayList<User> users;
    private ArrayList<Miner> miners;
    transient ExecutorService minerService;
    transient ExecutorService userService;

    public BlockChain(int zeros) {
        this.chain = new ArrayList<>();
        this.zeros = zeros;
        this.currentData = new ArrayDeque<>();
        this.currentPackagedData = Collections.emptyList();
        this.transactionIdentifier = 0;
        this.miners = new ArrayList<>();
        for (int i = 0; i < MINING_THREAD_NUM; i++) {
            miners.add(new Miner(i + 1, this));
        }
        minerService = Executors.newFixedThreadPool(MINING_THREAD_NUM);
        this.users = new ArrayList<>();
        for (int i = 0; i < USER_THREAD_NUM; i++) {
            users.add(new User("User " + (i + 1), this));
        }
        userService = Executors.newFixedThreadPool(USER_THREAD_NUM);
    }

    // Class-wide getters

    public void printChain() {
        for (Block block : chain) {
            System.out.println(block.toString());
            if (block.getZeros() < ZERO_INCREASE_THRESHOLD) {
                System.out.println("N was increased to" + block.getZeros() + 1);
            } else if (block.getZeros() > ZERO_DECREASE_THRESHOLD && block.getZeros() > 1) {
                System.out.println("N was decreased to" + (block.getZeros() - 1));
            } else {
                System.out.println("N stays the same");
            }
            System.out.println();
        }
    }

    public Block getBlock(int index) {
        if (index < 0 || index >= chain.size()) {
            return null;
        }
        return chain.get(index);
    }

    public int getChainSize() {
        return chain.size();
    }

    public List<Transactions> getCurrentPackagedData() {
        return Collections.unmodifiableList(currentPackagedData);
    }

    synchronized public long getTransactionIdentifier() {
        return ++transactionIdentifier;
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.addAll(users);
        accounts.addAll(miners);
        return Collections.unmodifiableList(accounts);
    }

    public int getBalance(Account account) {
        int received = chain.stream().flatMap(block -> block.getData().stream())
                .filter(transaction -> transaction.to.equals(account)).mapToInt(Transactions::getAmount).sum();
        received += currentData.stream().filter(transaction -> transaction.to.equals(account)).mapToInt(Transactions::getAmount).sum();
        if (account.isMiner() > 0) {
            received += chain.stream().mapToInt(Block::getMinerId).filter(i -> i == account.isMiner()).count() * BLOCK_AWARD;
        }
        int sent = chain.stream().flatMap(block -> block.getData().stream())
                .filter(transaction -> transaction.from.equals(account)).mapToInt(Transactions::getAmount).sum();
        sent += currentData.stream().filter(transaction -> transaction.from.equals(account)).mapToInt(Transactions::getAmount).sum();
        return received - sent + INITIAL_BALANCE;
    }

    public boolean isValid() {
        boolean valid = true;
        if (chain.size() > 0) {
            Block firstBlock = chain.get(0);
            boolean hashCheck = firstBlock.getHash().equals(firstBlock.calculateHash());
            boolean zeroCheck = firstBlock.getHash().substring(0, firstBlock.getZeros())
                    .equals("0".repeat(firstBlock.getZeros()));
            valid = hashCheck && zeroCheck;
        }
        if (chain.size() > 1) {
            for (int i = 1; i < chain.size(); i++) {
                Block b = chain.get(i);
                Block previous = chain.get(i - 1);
                boolean hashCheck = b.getPreviousHash().equals(previous.getHash()) &&
                        b.getHash().equals(b.calculateHash());
                boolean zeroCheck = b.getHash().substring(0, b.getZeros())
                        .equals("0".repeat(b.getZeros()));
                boolean identifierCheck = true;
                for (Transactions tr : b.getData()) {
                    if (chain.stream().filter(blk -> blk.getId() != b.getId()).map(Block::getData).flatMap(List::stream).anyMatch(tr::equals)) {
                        identifierCheck = false;
                        break;
                    }
                }
                valid = valid && hashCheck && zeroCheck && identifierCheck;
                if (!valid) {
                    break;
                }
            }
        }
        return valid;
    }

    public long getMinID() {
        if (chain.size() < 1) {
            return 0;
        }
        return chain.get(chain.size() - 1).getMaxIdentifier() + 1;
    }

    // Class-wide utils

    public boolean verifyBlock (Block b) {
        if (chain.size() == 0) {
            boolean hashCheck = b.getHash().equals(b.calculateHash());
            boolean zeroCheck = b.getHash().substring(0, b.getZeros()).equals("0".repeat(b.getZeros()));
            return hashCheck && zeroCheck;
        } else {
            Block previous = chain.get(chain.size() - 1);
            boolean hashCheck = b.getPreviousHash().equals(previous.getHash()) &&
                    b.getHash().equals(b.calculateHash());
            boolean zeroCheck = b.getHash().substring(0, b.getZeros())
                    .equals("0".repeat(b.getZeros()));
            // boolean identityCheck = b.getMinIdentifier() > previous.getMaxIdentifier();
            boolean identifierCheck = true;
            for (Transactions tr : b.getData()) {
                if (chain.stream().map(Block::getData).flatMap(List::stream).anyMatch(tr::equals)) {
                    identifierCheck = false;
                    break;
                }
            }
            return hashCheck && zeroCheck && identifierCheck;
        }
    }

    public void shutDown() {
        minerService.shutdownNow();
        userService.shutdownNow();
        saveChain(DATA_PATH);
    }

    // Class-wide setters
    public static BlockChain loadChain(String dataPath) {
        BlockChain blockChain = null;
        try {
            FileInputStream fis = new FileInputStream(dataPath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            blockChain = (BlockChain) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (blockChain != null && blockChain.isValid()) {
            return blockChain;
        }
        return null;
    }

    synchronized public void saveChain(String dataPath) {
        try {
            FileOutputStream fos = new FileOutputStream(dataPath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public boolean addBlock(Block b) {
        if (verifyBlock(b) && chain.size() < BLOCK_NUM) {
            chain.add(b);
            System.out.println(b);
            updateZeros();
            if (chain.size() < BLOCK_NUM) {
                packageData();
            }
            saveChain(DATA_PATH);
            return true;
        }
        return false;
    }

    synchronized public void updateZeros() {
        if (chain.size() == 0) {
            return;
        }
        long timeConsumed = chain.get(chain.size() - 1).getTimeConsumed();
        final int step = 1;
        if (timeConsumed < ZERO_INCREASE_THRESHOLD) {
            zeros += step;
            System.out.println("N was increased by " + step);
        } else if (timeConsumed > ZERO_DECREASE_THRESHOLD) {
            zeros -= step;
            if (zeros < 0) {
                zeros = 0;
            }
            System.out.println("N was decreased by " + step);
        } else {
            System.out.println("N stays the same");
        }
        System.out.println();
    }

    synchronized public boolean addTransactions(Transactions transactions) {
        if (verifyTransactions(transactions)) {
            currentData.add(transactions);
            return true;
        }
        return false;
    }

    private boolean verifyTransactions(Transactions transactions) {
        boolean integrity;
        try {
            Utils.verifier.initVerify(transactions.publicKey);
            Utils.verifier.update((transactions.from.getName() + transactions.to.getName() +
                    transactions.amount + transactions.identifier).getBytes());
            integrity = Utils.verifier.verify(transactions.signature);
        } catch (InvalidKeyException e) {
            System.err.println("Error in verifying transaction: Invalid key");
            return false;
        } catch (SignatureException e) {
            System.err.println("Error in verifying transaction: Invalid signature");
            return false;
        }
        boolean identifier = transactions.identifier >= getMinID() &&
                !currentData.stream().map(Transactions::getIdentifier)
                        .collect(Collectors.toList()).contains(transactions.identifier);
        return integrity && identifier;
    }

    synchronized public void packageData() {
        if (chain.size() == 0 || currentData.size() == 0) {
            return;
        }
        ArrayList<Transactions> packagedData = new ArrayList<>();
        for (int i = 0; i < BLOCK_DATA_LIMIT && !currentData.isEmpty(); i++) {
            packagedData.add(currentData.pop());
        }
        currentPackagedData = Collections.unmodifiableList(packagedData);
    }
    public void generateBlock() {
        activateUsers();
        if (miners.size() < MINING_THREAD_NUM) {
            for (int i = miners.size(); i < MINING_THREAD_NUM; i++) {
                miners.add(new Miner(i + 1, this));
            }
        }
        for (int i = 0; i < MINING_THREAD_NUM; i++) {
            minerService.submit(new Miner(i + 1, this));
        }
    }

    private void activateUsers() {
        if (users.size() < USER_THREAD_NUM) {
            for (int i = users.size(); i < USER_THREAD_NUM; i++) {
                users.add(new User("User " + (i + 1), this));
            }
        }
        for (int i = 0; i < USER_THREAD_NUM; i++) {
            userService.submit(users.get(i));
        }
    }


    // Overrides
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.minerService = Executors.newFixedThreadPool(MINING_THREAD_NUM);
        this.userService = Executors.newFixedThreadPool(USER_THREAD_NUM);
    }
}
