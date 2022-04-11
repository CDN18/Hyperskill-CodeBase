import java.util.List;

class Counter {

    public static boolean checkTheSameNumberOfTimes(int elem, List<Integer> list1, List<Integer> list2) {
        // implement the method
        int counter1 = 0;
        int counter2 = 0;
        for (Integer num : list1) {
            if (elem == num) {
                counter1++;
            }
        }
        for (Integer num : list2) {
            if (elem == num) {
                counter2++;
            }
        }
        return counter1 == counter2;
    }
}