
class Clock {

    int hours = 12;
    int minutes = 0;

    void next() {
        // implement me
        this.minutes++;
        if (this.minutes == 60) {
            this.hours++;
            if (this.hours > 12) {
                this.hours %= 12;
            }
            minutes %= 60;
        }
    }
}