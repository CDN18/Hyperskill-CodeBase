package blockchain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static blockchain.Utils.*;
import static blockchain.Config.*;

public class Miner implements Account, Runnable, Serializable {
    private static final long serialVersionUID = GENERAL_SERIAL_UID;
    int minerId;
    BlockChain chain;
    static Random generator = new Random();

    private KeyPair keyPair;
    transient Signature signer;
    private Block miningBlock;
    long startTime;


    public Miner(int minerId, BlockChain chain) {
        this.minerId = minerId;
        this.chain = chain;
        this.keyPair = Utils.keyPairAgent.generateKeyPair();
        this.signer = Utils.getSigner(SIGNATURE_ALGORITHM);
        try {
            this.signer.initSign(keyPair.getPrivate());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        initializeBlock();
    }

    @Override
    public int isMiner() {
        return minerId;
    }

    @Override
    public String getName() {
        return "miner" + minerId;
    }

    @Override
    public int getBalance() {
        return chain.getBalance(this);
    }

    @Override
    public Transactions signTransaction(Account to, int amount) {
        Transactions signedTransactions = new Transactions(this, to, chain.getTransactionIdentifier(), amount);
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
        return chain.addTransactions(signedTransactions);
    }

    public boolean haveNewBlock() {
        return miningBlock.getId() != chain.getChainSize() + 1;
    }

    public void initializeBlock() {
        this.startTime = getTimeStamp();
        this.miningBlock = new Block();
        miningBlock.setMinerId(minerId);
        miningBlock.setId(chain.getChainSize() + 1);
        miningBlock.setTimestamp(getTimeStamp());
        miningBlock.setMagicNumber(Math.abs(generator.nextInt()));
        miningBlock.setZeros(chain.zeros);
        // Transactions awardTransaction = new Transactions(null, this, chain.getTransactionIdentifier(), BLOCK_AWARD);
        // miningBlock.addData(List.of(awardTransaction));
        if (chain.getChainSize() > 0) {
            miningBlock.addData(chain.getCurrentPackagedData());
            while (chain.getBlock(chain.getChainSize() - 1).getDataString().equals(miningBlock.getDataString())) {
                chain.packageData();
                miningBlock.updateData(chain.getCurrentPackagedData());
            }
        }
        if (miningBlock.getId() > 1) {
            miningBlock.setPreviousHash(chain.getBlock(chain.getChainSize() - 1).getHash());
        } else {
            miningBlock.setPreviousHash("0");
        }
    }

    public boolean mineBlock() {
        if (haveNewBlock()) {
            initializeBlock();
        } else {
            if (chain.getChainSize() > 0) {
                while (chain.getCurrentPackagedData().isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                miningBlock.updateData(chain.getCurrentPackagedData());
            }
        }
        miningBlock.setMagicNumber(Math.abs(generator.nextInt()));
        miningBlock.setHash(miningBlock.calculateHash());
        return miningBlock.getHash().substring(0, miningBlock.getZeros())
                .equals("0".repeat(miningBlock.getZeros()));
    }

    @Override
    public void run() {
        List<Account> accounts;
        while (chain.getChainSize() < BLOCK_NUM) {
            if (chain.getChainSize() > 0 && getBalance() > 1 && generator.nextInt() % 3 == 0) {
                accounts = chain.getAccounts();
                Account selectedAccount;
                do {
                    selectedAccount = accounts.get(generator.nextInt(accounts.size()));
                } while (selectedAccount == null || selectedAccount.equals(this));
                submitTransaction(selectedAccount, generator.nextInt(getBalance() / 2) + 1);
            }
            if (mineBlock()) {
                miningBlock.setTimeConsumed((int) ((getTimeStamp() - startTime) / 1000));
                chain.addBlock(miningBlock);
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
