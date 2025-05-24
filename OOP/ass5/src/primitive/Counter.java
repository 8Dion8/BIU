package primitive;
public class Counter {

    private int thisCouldHaveBeenAnInt;

    public Counter() {
        this.thisCouldHaveBeenAnInt = 0;
    }

    // add number to current count.
    public void increase(int number) {
        this.thisCouldHaveBeenAnInt += number;
    }
    // subtract number from current count.
    public void decrease(int number) {
        this.thisCouldHaveBeenAnInt -= number;
    }
    // get current count.
    public int getValue() {
        return this.thisCouldHaveBeenAnInt;
    }
}
