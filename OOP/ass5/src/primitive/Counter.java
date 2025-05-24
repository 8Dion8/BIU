package primitive;

/**
 * A simple Counter class that allows incrementing, decrementing,
 * and retrieving a count value.
 *
 * @author Gleb Shvartser 346832892
 */
public class Counter {

    private int thisCouldHaveBeenAnInt;

    /**
     * Constructs a Counter object with an initial count of 0.
     */
    public Counter() {
        this.thisCouldHaveBeenAnInt = 0;
    }

    /**
     * Increases the current count by the specified number.
     *
     * @param number the value to add to the current count
     */
    public void increase(int number) {
        this.thisCouldHaveBeenAnInt += number;
    }

    /**
     * Decreases the current count by the specified number.
     *
     * @param number the value to subtract from the current count
     */
    public void decrease(int number) {
        this.thisCouldHaveBeenAnInt -= number;
    }

    /**
     * Retrieves the current count value.
     *
     * @return the current count
     */
    public int getValue() {
        return this.thisCouldHaveBeenAnInt;
    }
}
