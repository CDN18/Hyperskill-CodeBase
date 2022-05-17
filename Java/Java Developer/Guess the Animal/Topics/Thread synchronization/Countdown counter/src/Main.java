class CountDownCounter {

    int count;

    public CountDownCounter(int initial) {
        this.count = initial;
    }

    synchronized public void decrement() {
        count--;
    }
}