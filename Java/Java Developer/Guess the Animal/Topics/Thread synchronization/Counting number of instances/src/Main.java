class ClassCountingInstances {

    private static long numberOfInstances;

    public ClassCountingInstances() {
        // write the increment here
    }

    public static synchronized long getNumberOfInstances() {
        return numberOfInstances;
    }
}