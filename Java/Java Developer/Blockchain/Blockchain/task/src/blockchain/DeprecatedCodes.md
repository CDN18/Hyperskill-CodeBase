# Deprecated Codes

## `Main.java`
```java
/*  Deprecated by Stage 3
*   Set and update zeros of the blockchain
        System.out.print("Enter how many zeros the hash must start with: ");
        int zeros = parseInt(scanner.nextLine());
        System.out.println("");
        if (blockChain == null) {
            blockChain = new BlockChain(zeros);
        } else {
            blockChain.setZeros(zeros);
        }
         */

/* Deprecated by Stage 4
* Start Mining via Main Thread
        ExecutorService miners = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 8; i++) {
            miners.submit(new Miner(i + 1, blockChain));
        }
        boolean completed = false;
        while (!completed) {
            completed = blockChain.getChainSize() < 5;
        }
        try {
            miners.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */

/* Deprecated by Stage 3
* Start Mining via GenerateBlock() in blockChain
        for (int i = blockChain.getChainSize(); i < 5; i++) {
            blockChain.generateBlock();
            blockChain.saveChain(DATA_PATH);
            System.out.println(blockChain.getBlock(blockChain.getChainSize() - 1));
        }
        // blockChain.printChain();
         */
```

## `Block.java`

```java
    // Deprecated by Stage 4
    @Deprecated
    public String generateHash() {
        String calculatedHash;
        do {
            magicNumber = Math.abs(generator.nextInt());
            calculatedHash = calculateHash();
        } while (!calculatedHash.substring(0, zeros).equals("0".repeat(zeros)));
        return calculatedHash;
    }
     */
```

## `BlockChain.java`

```java
    // Deprecates

    // Deprecated by Stage 4
    @Deprecated
    public void setZeros(int zeros) {
        this.zeros = zeros;
        if (chain.size() > 0) {
            chain.get(0).setZeros(zeros);
            chain.get(0).setHash(chain.get(0).generateHash());
            for (int i = 1; i < chain.size(); i++) {
                chain.get(i).setZeros(zeros);
                chain.get(i).setPreviousHash(chain.get(i - 1).getHash());
                chain.get(i).setHash(chain.get(i).generateHash());
            }
        }
    }

    // Deprecated by Stage 4
    @Deprecated
    public List<String> getCurrentData() {
        // return Collections.unmodifiableList(this.currentData);
        return Collections.emptyList();
    }
```

## `Miner.java`

```java
    @Deprecated
    public void updateBlock() {
        miningBlock.setTimestamp(getTimeStamp());
    }
```

