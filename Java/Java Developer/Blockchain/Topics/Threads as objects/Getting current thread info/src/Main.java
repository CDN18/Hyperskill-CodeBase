class Info {

    public static void printCurrentThreadInfo() {
        // get the thread and print its info
        Thread t = Thread.currentThread();
        System.out.println("name: " + t.getName());
        System.out.println("priority: " + t.getPriority());
    }
}