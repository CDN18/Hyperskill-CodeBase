class Util {
    // correct this method to avoid NPE
    public static void printLength(String name) {
        System.out.println(name != null ? name.length() : 0);
    }
}