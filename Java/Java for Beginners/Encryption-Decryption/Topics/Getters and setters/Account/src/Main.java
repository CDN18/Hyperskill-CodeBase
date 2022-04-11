class Account {

    private long balance;
    private String ownerName;
    private boolean locked;

    public long getBalance() {
        return this.balance;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}