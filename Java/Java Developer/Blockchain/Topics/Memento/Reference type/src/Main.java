import java.util.*;

class Originator {
    private int[][] array;

    public Originator(int[][] array) {
        this.array = array.clone();
    }

    public void setNumber(int position, int number) {
        array[position / array[0].length][position % array[0].length] = number;
    }

    public int[][] getArray() {
        return array.clone();
    }

    public Memento getMemento() {
        // TODO implement this method
        return new Memento(array);
    }

    public void setMemento(Memento memento) {
        // TODO implement this method
        array = new int[memento.array.length][memento.array[0].length];
        for (int i = 0; i < memento.array.length; i++) {
            System.arraycopy(memento.array[i], 0, array[i], 0, memento.array[0].length);
        }
    }

    static class Memento {
        // TODO implement this class
        private final int[][] array;

        public Memento(int[][] array) {
            int[][] newArray = new int[array.length][array[0].length];
            for (int i = 0; i < array.length; i++) {
                System.arraycopy(array[i], 0, newArray[i], 0, array[0].length);
            }
            this.array = newArray;
        }
    }
}

class Caretaker {
    private final Originator originator;
    private Originator.Memento snapshot = null;

    public Caretaker(Originator originator) {
        this.originator = originator;
    }

    public void save() {
        snapshot = originator.getMemento();
    }

    public void restore() {
        if (snapshot != null) {
            originator.setMemento(snapshot);
        }
    }
}