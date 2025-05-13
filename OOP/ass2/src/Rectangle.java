import biuoop.DrawSurface;

/**
 * Rectangle class represents a rectangle shape with borders and a render color.
 * @author Gleb Shvartser 346832892
 */
public class Rectangle extends Renderable {
    private Border[] borders;
    private final int x1, y1, x2, y2, width, height;

    private java.awt.Color renderColor;

    /**
     * Constructor for Rectangle class.
     * @param x1 The x-coordinate of the top-left corner.
     * @param y1 The y-coordinate of the top-left corner.
     * @param x2 The x-coordinate of the bottom-right corner.
     * @param y2 The y-coordinate of the bottom-right corner.
     */
    public Rectangle(int x1, int y1, int x2, int y2) {
        this.borders = new Border[4];
        this.borders[0] = new Border(new Line(x1, y1, x1, y2), false);
        this.borders[1] = new Border(new Line(x1, y2, x2, y2), true);
        this.borders[2] = new Border(new Line(x2, y2, x2, y1), false);
        this.borders[3] = new Border(new Line(x2, y1, x1, y1), true);

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.width = x2 - x1;
        this.height = y2 - y1;

        this.renderColor = null;

        this.setZIndex(0);
    }

    /**
     * Getter for the borders of the rectangle.
     * @return An array of Border objects representing the borders of the rectangle.
     */
    public Border[] getBorders() {
        return this.borders;
    }

    /**
     * Setter for the render color of the rectangle.
     * @param renderColor The color to be used for rendering the rectangle.
     */
    public void setRenderColor(java.awt.Color renderColor) {
        this.renderColor = renderColor;
    }

    /**
     * Draws the rectangle on the given DrawSurface.
     * @param surface The DrawSurface on which to draw the rectangle.
     */
    public void drawOn(DrawSurface surface) {
        if (this.renderColor != null) {
            surface.setColor(this.renderColor);
            surface.fillRectangle(this.x1, this.y1, this.width, this.height);
        }
    }

    /**
     * Getter for the coordinates of the rectangle.
     * @return An array of integers representing the coordinates of the rectangle.
     */
    public int[] getCoordinates() {
        int[] coordinates = {x1, y1, x2, y2};
        return coordinates;
    }
}
