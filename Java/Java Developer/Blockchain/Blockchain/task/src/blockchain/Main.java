package blockchain;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static blockchain.Config.*;
import static blockchain.Config.BLOCK_NUM;
import static blockchain.Config.DATA_PATH;

public class Main {
    public static void main(String[] args) {
        File data = new File(DATA_PATH);
        BlockChain blockChain = null;
        /* Stage 6 requires generating a new block chain every time
        if (data.exists() && data.length() > 0) {
            blockChain = BlockChain.loadChain(DATA_PATH);
        }
        */
        blockChain = new BlockChain(0);
        // blockChain.printChain();
        blockChain.generateBlock();
/*
        // DEBUG: Publicly invoke ActivateUser()
        User testUser = new User("testUser", blockChain);
        // DEBUG: Activate a test Miner
        // ExecutorService miner = Executors.newFixedThreadPool(1);
        // miner.submit(new Miner(233, blockChain));
        Miner testMiner = new Miner(233, blockChain);
        ExecutorService miner = Executors.newFixedThreadPool(1);
        miner.submit(testMiner);
        testUser.run();
*/
        while (blockChain.getChainSize() < BLOCK_NUM) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        blockChain.shutDown();
    }

}
