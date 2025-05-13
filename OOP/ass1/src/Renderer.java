import biuoop.DrawSurface;
import biuoop.GUI;
import java.awt.Color;

/**
 * Class AbstractArtDrawing is responsible for the rendering of elements.
 * @author Gleb Shvartser 346832892
 */
public class Renderer {

    private DrawSurface ds;
    private GUI gui;

    private final int pointRadius = 3;

    /**
     * Renderer constructor.
     *
     * @param displayWidth the width of the display
     * @param displayHeight the height of the display
     */
    public Renderer(int displayWidth, int displayHeight) {
        this.gui = new GUI("ass1", displayWidth, displayHeight);
        this.ds = this.gui.getDrawSurface();
    }

    /**
     * Draw a single line.
     *
     * @param line the Line to draw
     * @param color the color to draw the line with
     */
    public void renderLine(Line line, Color color) {
        this.ds.setColor(color);
        this.ds.drawLine(
                (int) line.start().getX(),
                (int) line.start().getY(),
                (int) line.end().getX(),
                (int) line.end().getY()
            );
    }

    /**
     * Draw an array of lines.
     *
     * @param lines the Lines to draw, generic Iterable to account for either ArrayList or HashSet
     * @param color the color to draw the lines with
     */
    public void renderLines(Iterable<Line> lines, Color color) {
        for (Line line : lines) {
            renderLine(line, color);
        }
    }

    /**
     * Draw a single point.
     *
     * @param point the Point to draw
     * @param color the color to draw the point with
     */
    public void renderPoint(Point point, Color color) {
        this.ds.setColor(color);
        this.ds.fillCircle((int) point.getX(), (int) point.getY(), pointRadius);
    }

    /**
     * Draw an array of points.
     *
     * @param points the Points to draw, generic Iterable to account for either ArrayList or HashSet
     * @param color the color to draw the points with
     */
    public void renderPoints(Iterable<Point> points, Color color) {
        for (Point point : points) {
            renderPoint(point, color);
        }
    }

    /**
     * Display the final GUI.
     */
    public void displayGUI() {
        this.gui.show(this.ds);
    }
}
