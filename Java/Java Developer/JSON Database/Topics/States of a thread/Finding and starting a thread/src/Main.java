class ThreadProcessor {
    public static void findAndStartThread(Thread... threads) throws InterruptedException {
        // implement this method
        for (Thread thread : threads) {
            if (thread.getState() == Thread.State.NEW) {
                thread.start();
                thread.join();
            }
        }
    }
}