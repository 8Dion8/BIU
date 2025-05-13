import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;

/**
 * Class AbstractArtDrawing is responsible for the main loop of the task.
 * @author Gleb Shvartser 346832892
 */
public class AbstractArtDrawing {

    private final int displayHeight = 600;
    private final int displayWidth = 800;
    private final int nLinesToDraw = 10;

    private Random rand;
    private Renderer renderer;

    private ArrayList<Line> lines = new ArrayList<Line>();

    // arrays/sets holding the final render targets
    private ArrayList<Point> midpoints = new ArrayList<Point>();
    private ArrayList<Point> intersections = new ArrayList<Point>();
    // Lines are stored in a HashSet to prevent drawing multiple segments over each other
    private HashSet<Line> triangleSegments = new HashSet<Line>();
    private HashSet<Line> regularSegments = new HashSet<Line>();

    /**
     * AbstractArtDrawing constructor.
     */
    public AbstractArtDrawing() {
        this.rand = new Random();
        this.renderer = new Renderer(displayWidth, displayHeight);
    }

    /**
     * Main loop of task.
     */
    public void runTask() {
        // generate 10 lines, calculate their midpoints and save them to an array
        for (int i = 0; i < nLinesToDraw; i++) {
            Line newLine = generateRandomLine();
            lines.add(newLine);
            Point midpoint = newLine.middle();
            midpoints.add(midpoint);
        }

        // Iterate over all triplets of Lines.
        // Save any intersections to the event points of lines.
        // For Lines that create triangles, save the triangle segments.
        int i = 1;
        for (Line line1 : lines) {
            int j = i + 1;
            for (Line line2 : lines.subList(i, nLinesToDraw)) {
                Point intersection1 = line1.intersectionWith(line2);
                if (intersection1 != null) {
                    intersections.add(intersection1);
                    line1.addEventPoint(intersection1);
                    line2.addEventPoint(intersection1);
                    for (Line line3 : lines.subList(j, nLinesToDraw)) {
                        // do lines create a triangle?
                        if (line3.isIntersecting(line1, line2)) {
                            Point intersection2 = line2.intersectionWith(line3);
                            Point intersection3 = line3.intersectionWith(line1);

                            triangleSegments.add(
                                new Line(intersection1, intersection2)
                            );
                            triangleSegments.add(
                                new Line(intersection2, intersection3)
                            );
                            triangleSegments.add(
                                new Line(intersection3, intersection1)
                            );
                        }
                    }
                }
                j++;
            }
            i++;
        }
        // Iterate over all event points of lines, and create new segments from each consecutive pair;
        // Essentially, split all lines by any intersections, while filtering any segments that make triangles
        SortedSet<Point> eventPoints;
        Point point1, point2;
        Iterator<Point> eventPointIterator;
        for (Line line : lines) {
            eventPoints = line.getEventPoints();
            eventPointIterator = eventPoints.iterator();

            point1 = eventPointIterator.next();
            while (eventPointIterator.hasNext()) {
                point2 = eventPointIterator.next();
                Line newSegment = new Line(point1, point2);

                boolean isNewSegmentInTriangle = false;
                for (Line triangleSegment : triangleSegments) {
                    if (triangleSegment.contains(newSegment)) {
                        isNewSegmentInTriangle = true;
                        break;
                    }
                }
                if (!isNewSegmentInTriangle) {
                    regularSegments.add(newSegment);
                }
                point1 = point2;
            }
        }

        // render all elements
        renderer.renderLines(triangleSegments, Color.GREEN);
        renderer.renderLines(regularSegments, Color.BLACK);
        renderer.renderPoints(midpoints, Color.BLUE);
        renderer.renderPoints(intersections, Color.RED);

        renderer.displayGUI();
    }

    /**
     * Generates a random point confined by the screen size.
     *
     * @return the generated Point
     */
    public Point generateRandomPoint() {
        int x = rand.nextInt(displayWidth) + 1;
        int y = rand.nextInt(displayHeight) + 1;

        return new Point(x, y);
    }

    /**
     * Generates a random Line confined by the screen size.
     *
     * @return the generated Line
     */
    public Line generateRandomLine() {
        return new Line(generateRandomPoint(), generateRandomPoint());
    }

    /**
     * Main.
     *
     * @param args cli arguments
     */
    public static void main(String[] args) {
        AbstractArtDrawing drawing = new AbstractArtDrawing();
        drawing.runTask();
    }
}
