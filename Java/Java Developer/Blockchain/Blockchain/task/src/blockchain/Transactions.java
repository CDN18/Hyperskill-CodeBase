package blockchain;

import java.io.Serializable;
import java.security.PublicKey;

import static blockchain.Config.*;

public class Transactions implements Serializable {
    private static final long serialVersionUID = GENERAL_SERIAL_UID;
    Account from;
    Account to;
    final long identifier;
    int amount;
    byte[] signature;
    PublicKey publicKey;

    public Transactions(Account from, Account to, long identifier, int amount) {
        this.from = from;
        this.to = to;
        this.identifier = identifier;
        this.amount = amount;
        this.signature = null;
    }

    public long getIdentifier() {
        return identifier;
    }

    public int getAmount() {
        return amount;
    }

    public boolean updateSignature(byte[] signature, PublicKey publicKey) {
        if (this.signature == null) {
            this.signature = signature;
            this.publicKey = publicKey;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return from.getName() + " sent " + amount + " VC to " + to.getName();
    }
}
