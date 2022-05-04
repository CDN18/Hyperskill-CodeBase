import java.util.*;

class Originator {
    private List<Integer> numbers = new ArrayList<>();

    public void addNumber(Integer number) {
        numbers.add(number);
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public Memento getMemento() {
        return new Memento(numbers);
    }

    public void setMemento(Memento memento) {
        this.numbers = new ArrayList<>(memento.numbers);
    }

    static class Memento {
        private final List<Integer> numbers;

        public Memento(List<Integer> numbers) {
            this.numbers = new ArrayList<>(numbers);
        }
    }
}

class Caretaker {
    private Originator originator;
    private Originator.Memento snapshot = null;

    Caretaker(Originator originator) {
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