import biuoop.DrawSurface;
import java.awt.Color;

public class ScoreIndicator implements Sprite {
    private Counter counter;
    private int height;
    private int width;
    private Color bgColor;
    private Color textColor;

    public ScoreIndicator(Counter counter, int height, int width, Color bgColor, Color textColor) {
        this.counter = counter;
        this.height = height;
        this.width = width;
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

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

    public void timePassed() { }

    public void addToGame(Game game) {
        game.addSprite(this);
    }
}
