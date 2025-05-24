package sprites;

import core.Game;
import primitive.Counter;

import biuoop.DrawSurface;
import java.awt.Color;

/**
 * The ScoreIndicator class is responsible for displaying the current score
 * on the screen. It implements the Sprite interface, allowing it to be drawn
 * and updated as part of the game.
 * @author Gleb Shvartser 346832892
 */
public class ScoreIndicator implements Sprite {

    private Counter counter;
    private int height;
    private int width;
    private Color bgColor;
    private Color textColor;

    /**
     * Constructs a ScoreIndicator object.
     *
     * @param counter   the Counter object that holds the current score
     * @param height    the height of the score indicator display
     * @param width     the width of the score indicator display
     * @param bgColor   the background color of the score indicator
     * @param textColor the text color used to display the score
     */
    public ScoreIndicator(Counter counter, int height, int width, Color bgColor, Color textColor) {
        this.counter = counter;
        this.height = height;
        this.width = width;
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

    @Override
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.bgColor);
        surface.fillRectangle(
            0,
            0,
            this.width,
            this.height
        );

        surface.setColor(this.textColor);
        surface.drawText(
            (int) (this.width / 2),
            this.height - 5,
            "Score: " + this.counter.getValue(),
            this.height
        );
    }

    @Override
    public void timePassed() { }

    @Override
    public void addToGame(Game game) {
        game.addSprite(this);
    }

    @Override
    public void removeFromGame(Game game) {
        game.removeSprite(this);
    }
}
