public class Counter {

    private int thisCouldHaveBeenAnInt;

    public Counter() {
        this.thisCouldHaveBeenAnInt = 0;
    }

    // add number to current count.
    void increase(int number) {
        this.thisCouldHaveBeenAnInt += number;
    }
    // subtract number from current count.
    void decrease(int number) {
        this.thisCouldHaveBeenAnInt -= number;
    }
    // get current count.
    int getValue() {
        return this.thisCouldHaveBeenAnInt;
    }
}
