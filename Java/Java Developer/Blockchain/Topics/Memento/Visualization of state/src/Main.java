import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface Algorithm<S extends AlgorithmState> {

    boolean hasNextStep();

    void nextStep();

    S getState();

    void setState(S state);
}

interface AlgorithmState { }

class SelectionSort<T extends Comparable<T>> implements Algorithm<SelectionSort.SortState<T>> {
    private T[] array;
    private int currentIndex = 0;
    private int comparedIndex = 0;
    private int currentMinIndex = 0;

    SelectionSort(T[] array) {
        this.array = array.clone();
    }

    @Override
    public void nextStep() {
        if (comparedIndex == array.length - 1 && currentIndex != currentMinIndex) {
            T tmp = array[currentIndex];
            array[currentIndex] = array[currentMinIndex];
            array[currentMinIndex] = tmp;
            currentMinIndex = currentIndex;
        } else if (comparedIndex == array.length - 1) {
            currentIndex++;
            if (currentIndex < array.length - 1) {
                comparedIndex = currentIndex + 1;
            }
            currentMinIndex = array[currentIndex]
                    .compareTo(array[comparedIndex]) > 0 ? comparedIndex : currentIndex;
        } else {
            comparedIndex++;
        }

        if (array[comparedIndex].compareTo(array[currentMinIndex]) < 0) {
            currentMinIndex = comparedIndex;
        }
    }

    @Override
    public boolean hasNextStep() {
        return currentIndex < array.length - 1;
    }

    @Override
    public String toString() {
        return IntStream.range(0, array.length).mapToObj(i -> {
                    String s = String.valueOf(array[i]);
                    if (i == currentIndex) {
                        s = "{" + s + "}"; // final place for min item in range
                    }
                    if (i == comparedIndex) {
                        s = "[" + s + "]"; // candidate for min item
                    }
                    if (i == currentMinIndex) {
                        s = "(" + s + ")"; // current min item in range
                    }
                    return s;
                }).collect(Collectors.joining(" "));
    }

    @Override
    public void setState(SortState<T> state) {
        // TODO implement this method
        this.array = state.array.clone();
        this.currentIndex = state.currentIndex;
        this.comparedIndex = state.comparedIndex;
        this.currentMinIndex = state.currentMinIndex;
    }

    @Override
    public SortState<T> getState() {
        // TODO implement this method
        return new SortState<T>(array.clone(), currentIndex, comparedIndex, currentMinIndex);
    }

    static class SortState<T> implements AlgorithmState {
        // TODO implement this class
        private final T[] array;
        private final int currentIndex;
        private final int comparedIndex;
        private final int currentMinIndex;

        public SortState(T[] array, int currentIndex, int comparedIndex, int currentMinIndex) {
            this.array = array.clone();
            this.currentIndex = currentIndex;
            this.comparedIndex = comparedIndex;
            this.currentMinIndex = currentMinIndex;
        }
    }
}

class AlgorithmVisualizer<T extends AlgorithmState> {
    private final Algorithm<T> algorithm;
    private final Deque<T> states = new ArrayDeque<>();

    AlgorithmVisualizer(Algorithm<T> algorithm) {
        this.algorithm = algorithm;
    }

    public void nextStep() {
        if (algorithm.hasNextStep()) {
            states.push(algorithm.getState());
            algorithm.nextStep();
        }
    }

    public void prevStep() {
        if (!states.isEmpty()) {
            algorithm.setState(states.pop());
        }
    }

    public void showCurrentStep() {
        System.out.println(algorithm);
    }
}