package blockchain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static blockchain.Config.*;

public class User implements Account, Runnable, Serializable {
    private static final long serialVersionUID = GENERAL_SERIAL_UID;
    static Random generator = new Random();
    String username;
    BlockChain blockChain;
    KeyPair keyPair;
    ArrayList<Account> accounts;
    transient Signature signer;

    public User(String username, BlockChain blockChain) {
        this.username = username;
        this.blockChain = blockChain;
        this.keyPair = Utils.keyPairAgent.generateKeyPair();
        this.signer = Utils.getSigner(SIGNATURE_ALGORITHM);
        try {
            this.signer.initSign(keyPair.getPrivate());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int isMiner() {
        return -1;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public int getBalance() {
        return blockChain.getBalance(this);
    }

    @Override
    public Transactions signTransaction(Account to, int amount) {
        Transactions signedTransactions = new Transactions(this, to, blockChain.getTransactionIdentifier(), amount);
        try {
            signer.update((this.getName() + to.getName() + amount + signedTransactions.identifier).getBytes());
        } catch (SignatureException e) {
            System.err.println("Error in getting transaction: SignatureException: " + e.getMessage());
            System.err.println("Returning null");
            return null;
        }
        byte[] signature;
        try {
            signature = signer.sign();
        } catch (SignatureException e) {
            System.err.println("Error in signing transaction: SignatureException: " + e.getMessage());
            System.err.println("Returning null");
            return null;
        }
        if (!signedTransactions.updateSignature(signature, keyPair.getPublic())) {
            System.err.println("Error in updating signature: Signature is already set");
            System.err.println("Returning null");
            return null;
        }
        return signedTransactions;
    }
    @Override
    public boolean submitTransaction(Account to, int amount) {
        if (amount <= 0) {
            System.err.println("Error in submitting transaction: Amount is less than or equal to 0");
            return false;
        }
        Transactions signedTransactions = signTransaction(to, amount);
        boolean result = blockChain.addTransactions(signedTransactions);
        return result;
    }

    @Override
    public void run(){
        List<Account> accounts;
        while(blockChain.getChainSize() < BLOCK_NUM){
            if (getBalance() > 1) {
                accounts = blockChain.getAccounts();
                Account selectedAccount;
                do {
                    selectedAccount = accounts.get(generator.nextInt(accounts.size()));
                } while (selectedAccount == null || selectedAccount.equals(this));
                submitTransaction(selectedAccount, generator.nextInt(getBalance() / 2) + 1);
            }
            try {
                Thread.sleep(Math.round(Math.random() * 100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.signer = Utils.getSigner(SIGNATURE_ALGORITHM);
        try {
            this.signer.initSign(keyPair.getPrivate());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}

