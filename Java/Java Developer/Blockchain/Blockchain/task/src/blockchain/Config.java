package blockchain;

public class Config {
    /* General Config */

    static final long PRODUCTION_SERIAL_UID = 6L;
    // static final long DEBUG_SERIAL_UID = 1003L;
    static final long GENERAL_SERIAL_UID = PRODUCTION_SERIAL_UID;
    static final String DATA_PATH = "blockchain.data";
    static final int BLOCK_NUM = 15;
    static final int PACKAGE_INTERVAL = 1000; // Unit: ms
    static final int BLOCK_DATA_LIMIT = 3;
    static final int ZERO_INCREASE_THRESHOLD = 0;
    static final int ZERO_DECREASE_THRESHOLD = 100;
    static final int BLOCK_AWARD = 100;
    static final int INITIAL_BALANCE = 100;
    static final int MINING_THREAD_NUM = 8;
    static final int USER_THREAD_NUM = 8;

    /* Keypair Management */
    static final String KEYPAIR_ALGORITHM = "RSA";
    static final int KEY_SIZE = 1024;
    static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
}
