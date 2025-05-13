/**
 * Class Border represents a border which balls bounce off.
 * Keeps track of what Line it represents and whether it is horizontal/vertical.
 * @author Gleb Shvartser 346832892
 */
public class Border {
    private Line line;
    private boolean isHorizontal;

    /**
     * Constructor for Border class.
     * @param line The line that represents the border.
     * @param isHorizontal Whether the border is horizontal or vertical.
     */
    public Border(Line line, boolean isHorizontal) {
        this.line = line;
        this.isHorizontal = isHorizontal;
    }

    /**
     * Getter for the line that represents the border.
     * @return The line that represents the border.
     */
    public Line getLine() {
        return line;
    }

    /**
     * Getter for whether the border is horizontal or vertical.
     * @return True if the border is horizontal, false if it is vertical.
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }
}
