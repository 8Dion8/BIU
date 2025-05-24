package primitive;

/**
 * Represents a rectangle with a location and width/height.
 * @author Gleb Shvartser 346832892
 */
public class Rectangle {
    private double width;
    private double height;

    private Point upperLeft;
    private Point upperRight;
    private Point lowerLeft;
    private Point lowerRight;

    private Line upperLine;
    private Line lowerLine;
    private Line leftLine;
    private Line rightLine;

    /**
     * Create a new rectangle with location and width/height.
     * @param upperLeft the upper left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.width = width;
        this.height = height;

        this.upperLeft = upperLeft;
        this.upperRight = new Point(upperLeft.getX() + width, upperLeft.getY());
        this.lowerLeft = new Point(upperLeft.getX(), upperLeft.getY() + height);
        this.lowerRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);

        upperLine = new Line(upperLeft, upperRight);
        lowerLine = new Line(lowerLeft, lowerRight);
        leftLine = new Line(upperLeft, lowerLeft);
        rightLine = new Line(upperRight, lowerRight);
    }

    /**
    * Create a new rectangle with location and width/height.
    * @param x the x-coordinate of the upper left corner of the rectangle
    * @param y the y-coordinate of the upper left corner of the rectangle
    * @param width the width of the rectangle
    * @param height the height of the rectangle
    */
    public Rectangle(int x, int y, int width, int height) {
        this(new Point(x, y), width, height);
    }

    /**
    * Return a (possibly empty) List of intersection points
    * with the specified line.
    * @param line the line to find intersection points with
    * @return a list of intersection points
    */
    public java.util.List<Point> intersectionPoints(Line line) {
        java.util.List<Point> intersections = new java.util.ArrayList<>();
        intersections.add(upperLine.intersectionWith(line));
        intersections.add(lowerLine.intersectionWith(line));
        intersections.add(leftLine.intersectionWith(line));
        intersections.add(rightLine.intersectionWith(line));
        return intersections;
    }

    /**
    * Return the width of the rectangle.
    * @return the width of the rectangle
    */
    public double getWidth() {
        return width;
    }

    /**
    * Return the height of the rectangle.
    * @return the height of the rectangle
    */
    public double getHeight() {
        return height;
    }

    /**
    * Return the upper-left point of the rectangle.
    * @return the upper-left point of the rectangle
    */
    public Point getUpperLeft() {
        return upperLeft;
    }

    /**
    * Return the lower-right point of the rectangle.
    * @return the lower-right point of the rectangle
    */
    public Point getLowerRight() {
        return lowerRight;
    }

    /**
    * Return the vertices of the rectangle.
    * @return the vertices of the rectangle
    */
    public Point[] getVertices() {
        return new Point[]{upperLeft, upperRight, lowerRight, lowerLeft};
    }

    /**
    * Return the edges of the rectangle.
    * @return the edges of the rectangle
    */
    public Line[] getEdges() {
        return new Line[]{upperLine, lowerLine, leftLine, rightLine};
    }

    /**
    * Return the closest edge of the rectangle to the given line.
    * @param line the line to find the closest edge to
    * @return the closest edge of the rectangle to the given line
    */
    public Line getClosestEdge(Line line) {
        Line closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Line edge : getEdges()) {
            double distance = edge.distanceToPoint(line.start());
            if (distance < minDistance) {
                closest = edge;
                minDistance = distance;
            }
        }
        return closest;
    }

    /**
    * Move the rectangle horizontally.
    * @param dx the amount to move the rectangle horizontally
    */
    public void moveX(double dx) {
        this.upperLeft.setX(this.upperLeft.getX() + dx);
        this.upperRight.setX(this.upperRight.getX() + dx);
        this.lowerLeft.setX(this.lowerLeft.getX() + dx);
        this.lowerRight.setX(this.lowerRight.getX() + dx);
    }

    /**
    * Move the rectangle vertically.
    * @param dy the amount to move the rectangle vertically
    */
    public void moveY(double dy) {
        this.upperLeft.setY(this.upperLeft.getY() + dy);
        this.upperRight.setY(this.upperRight.getY() + dy);
        this.lowerLeft.setY(this.lowerLeft.getY() + dy);
        this.lowerRight.setY(this.lowerRight.getY() + dy);
    }
}
