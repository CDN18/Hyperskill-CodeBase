class ThreadUtil {
    static void printNameOfTerminatedThread(Thread[] threads) {
        // implement the method
        for (Thread thread : threads) {
            if (thread.getState() == Thread.State.TERMINATED) {
                System.out.println(thread.getName());
                return;
            }
        }
    }
}