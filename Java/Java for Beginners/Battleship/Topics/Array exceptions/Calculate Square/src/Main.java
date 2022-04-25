class FixingExceptions {

    public static void calculateSquare(int[] array, int index) {
        // write your code here
        if (array == null || index < 0 || index >= array.length) {
            System.out.println("Exception!");
        } else {
            System.out.println(array[index] * array[index]);
        }
    }
}