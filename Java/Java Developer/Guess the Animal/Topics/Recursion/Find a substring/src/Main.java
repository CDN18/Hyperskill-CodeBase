class Util {
    public static int indexOf(String src, String tgt) {
        // your code here
        if (!src.contains(tgt)) {
            return -1;
        }
        if (src.startsWith(tgt)) {
            return 0;
        }
        return 1 + indexOf(src.substring(1), tgt);
    }
}