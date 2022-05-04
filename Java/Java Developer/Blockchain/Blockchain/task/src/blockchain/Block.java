package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static blockchain.Utils.Sha256;
import static blockchain.Config.*;

public class Block implements Serializable {
    private static final long serialVersionUID = GENERAL_SERIAL_UID;
    private int minerId;
    private long id;
    private long timestamp;
    private int magicNumber;
    private int timeConsumed;
    private int zeros;
    private ArrayList<Transactions> data;
    private String previousHash;
    private String hash;

    public Block(int minerId, long id, long timestamp, int zeros,
                 ArrayList<Transactions> data, String previousHash, String hash, int timeConsumed) {
        this.minerId = minerId;
        this.id = id;
        this.timestamp = timestamp;
        this.zeros = zeros;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
        this.timeConsumed = timeConsumed;
    }


    public Block() {
        this.minerId = -1;
        this.id = -1;
        this.timestamp = -1;
        this.zeros = -1;
        this.data = new ArrayList<>();
        this.previousHash = null;
        this.hash = null;
        this.timeConsumed = -1;
    }

    public long getId() {
        return id;
    }

    public int getMinerId() {
        return minerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    public int getZeros() {
        return zeros;
    }

    public ArrayList<Transactions> getData() {
        return data;
    }

    public String getDataString() {
        return String.join("\n", data.stream().map(Transactions::toString).toArray(String[]::new));
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public String toString() {
        return "Block:\n" +
                "Created by: miner" + minerId + "\n" +
                "miner" + minerId + " gets " + BLOCK_AWARD + " VC" + "\n" +
                "Id: " + this.getId() + "\n" +
                "Timestamp: " + this.getTimestamp() + "\n" +
                "Magic number: " + this.getMagicNumber() + "\n" +
                "Hash of the previous block:\n" + this.getPreviousHash() + "\n" +
                "Hash of the block:\n" + this.getHash() + "\n" +
                "Block data: " + ("".equals(this.getDataString()) ? "no transactions" : ("\n" + this.getDataString())) + "\n" +
                "Block was generating for " + this.getTimeConsumed() + " seconds";
    }

    public long getTimeConsumed() {
        return timeConsumed;
    }

    public long getMinIdentifier() {
        return this.data.stream().mapToLong(Transactions::getIdentifier).min().orElse(-1L);
    }

    public long getMaxIdentifier() {
        return this.data.stream().mapToLong(Transactions::getIdentifier).max().orElse(-1L);
    }

    public String calculateHash() {
        return Sha256(id + String.valueOf(timestamp) +
                magicNumber + previousHash + getDataString());
    }

    public boolean isValid() {
        return hash.equals(Sha256(id + String.valueOf(timestamp) +
                magicNumber + getDataString() + previousHash));
    }

    public void setMinerId(int minerId) {
        this.minerId = minerId;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setTimeConsumed(int timeConsumed) {
        this.timeConsumed = timeConsumed;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setZeros(int zeros) {
        this.zeros = zeros;
    }

    public void addData(List<Transactions> currentData) {
        data.addAll(currentData);
    }

    public void updateData(List<Transactions> currentPackagedData) {
        if (currentPackagedData.size() == data.size() || currentPackagedData.isEmpty()) {
            return;
        } else {
            if (currentPackagedData.size() < data.size() - 1) {
                data.clear();
                data.addAll(currentPackagedData);
            }
            data.addAll(currentPackagedData.subList(Math.max(data.size() - 1, 0),
                    currentPackagedData.size()));
        }
    }
}
