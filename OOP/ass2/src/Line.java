import java.util.SortedSet;
import java.util.TreeSet;

import biuoop.DrawSurface;


/**
 * Class Line is responsible for defining and managing a line.
 * @author Gleb Shvartser 346832892
 */
public class Line {

    private Point point1;
    private Point point2;
    private SortedSet<Point> eventPoints;

    private final double epsilon = 0.00001;

    /**
     * Line constructor, defined by two Points.
     *
     * @param start first point
     * @param end   second point
     */
    public Line(Point start, Point end) {
        this.point1 = start;
        this.point2 = end;
        this.eventPoints = new TreeSet<Point>();
        addEventPoint(start);
        addEventPoint(end);
    }

    /**
     * Returns the distance between this and another point.
     *
     * @param x1 first point x-coordinate
     * @param y1 second point y-coordinate
     * @param x2 first point x-coordinate
     * @param y2 second point y-coordinate
     */
    public Line(double x1, double y1, double x2, double y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    /**
     * Returns the length of this line.
     *
     * @return the length of this line
     */
    public double length() {
        return this.point1.distance(this.point2);
    }

    /**
     * Returns the midpoint of this line.
     *
     * @return the midpoint of this line
     */
    public Point middle() {
        double deltaX = this.point2.getX() - this.point1.getX();
        double deltaY = this.point2.getY() - this.point1.getY();

        return new Point(
            this.point1.getX() + (deltaX / 2),
            this.point1.getY() + (deltaY / 2)
        );
    }

    /**
     * Point1 getter.
     *
     * @return the start of this line
     */
    public Point start() {
        return this.point1;
    }

    /**
     * Point2 getter.
     *
     * @return the end of this line
     */
    public Point end() {
        return this.point2;
    }

    static double crossProduct(Point a, Point b) {
        return a.getX() * b.getY() - b.getX() * a.getY();
    }

    private double[] calculateDirectionVectors(Line line) {
        double dx = line.end().getX() - line.start().getX();
        double dy = line.end().getY() - line.start().getY();
        return new double[] {dx, dy};
    }

    /**
     * Check whether or not two Lines are intersecting.
     * https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
     * @param other the line to check intersection with
     * @return true if the Lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other) {
        double[] thisVectors = calculateDirectionVectors(this);
        double[] otherVectors = calculateDirectionVectors(other);

        double dx1 = thisVectors[0];
        double dy1 = thisVectors[1];
        double dx2 = otherVectors[0];
        double dy2 = otherVectors[1];

        double det = dx1 * dy2 - dy1 * dx2;

        /* where on the lines do they intersect; if t or u are less than 0 or bigger than 1,
         * then the lines of segments do intersect, but not the segments themselves;
         * otherwise, segments intersect
         */
        double t =
            ((other.start().getX() - this.start().getX()) * dy2
           - (other.start().getY() - this.start().getY()) * dx2) / det;
        double u =
            ((other.start().getX() - this.start().getX()) * dy1
           - (other.start().getY() - this.start().getY()) * dx1) / det;

        return t >= -epsilon && t <= 1 + epsilon && u >= -epsilon && u <= 1 + epsilon;
    }

    /**
     * Check whether or not two Lines intersect this one.
     * @param other1 the first line to check intersection with
     * @param other2 the second line to check intersection with
     * @return true if the Lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other1, Line other2) {
        return isIntersecting(other1) && isIntersecting(other2);
    }

    /**
     * Check where two Lines intersect.
     * https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
     * @param other the line to check intersection with
     * @return Point where lines intersect, or null if they don't
     */
    public Point intersectionWith(Line other) {
        if (!isIntersecting(other)) {
            return null;
        }

        double[] thisVectors = calculateDirectionVectors(this);
        double[] otherVectors = calculateDirectionVectors(other);

        double dx1 = thisVectors[0];
        double dy1 = thisVectors[1];
        double dx2 = otherVectors[0];
        double dy2 = otherVectors[1];

        double det = dx1 * dy2 - dy1 * dx2;

        // are lines colinear?
        if (det < epsilon && det > -epsilon) {
            return null;
        }

        double t =
            ((other.start().getX() - this.start().getX()) * dy2
           - (other.start().getY() - this.start().getY()) * dx2) / det;

        double intersectionX = this.start().getX() + t * dx1;
        double intersectionY = this.start().getY() + t * dy1;

        return new Point(intersectionX, intersectionY);
    }

    /**
     * Check if two Lines are equal.
     * @param obj Other object to compare to. if not Line - return false
     * @return true if Lines are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Line other = (Line) obj;
        return (
            (this.start() == other.start() && this.end() == other.end())
            || (this.start() == other.end() && this.end() == other.start())
        );
    }

    @Override
    public int hashCode() {
        double roundedStartX = Math.round(start().getX() / epsilon) * epsilon;
        double roundedStartY = Math.round(start().getY() / epsilon) * epsilon;
        double roundedEndX = Math.round(end().getX() / epsilon) * epsilon;
        double roundedEndY = Math.round(end().getY() / epsilon) * epsilon;

        int result = Double.hashCode(roundedStartX);
        result = 31 * result + Double.hashCode(roundedStartY);
        result = 31 * result + Double.hashCode(roundedEndX);
        result = 31 * result + Double.hashCode(roundedEndY);

        return result;
    }

    /**
     * Checks if another line is contained within this line.
     * @param other The line to check.
     * @return True if the other line is contained within this line, false otherwise.
     */
    public boolean contains(Line other) {
        // Check if the lines are collinear
        if (!areCollinear(other)) {
            return false;
        }

        // Check if both endpoints of the other line lie within this line
        return (
            isPointOnLineSegment(other.start())
         && isPointOnLineSegment(other.end())
        );
    }

    /**
     * Helper method to check if two lines are collinear.
     * @param other The other line.
     * @return True if the lines are collinear, false otherwise.
     */
    private boolean areCollinear(Line other) {
        return (
            Math.abs(
                crossProduct(
                    new Point(
                        this.end().getX() - this.start().getX(),
                        this.end().getY() - this.start().getY()
                    ),
                    new Point(
                        other.end().getX() - other.start().getX(),
                        other.end().getY() - other.start().getY()
                    )
                )
            ) < epsilon
        && Math.abs(
                crossProduct(
                    new Point(
                        this.start().getX() - other.start().getX(),
                        this.start().getY() - other.start().getY()
                    ),
                    new Point(
                        this.end().getX() - other.start().getX(),
                        this.end().getY() - other.start().getY()
                    )
                )
            ) < epsilon
        );
    }

    /**
     * Helper method to check if a point lies on this line segment.
     * @param point The point to check.
     * @return True if the point lies on this line segment, false otherwise.
     */
    private boolean isPointOnLineSegment(Point point) {
        // Check if the point is within the bounding box of the line segment
        double minX = Math.min(this.start().getX(), this.end().getX());
        double maxX = Math.max(this.start().getX(), this.end().getX());
        double minY = Math.min(this.start().getY(), this.end().getY());
        double maxY = Math.max(this.start().getY(), this.end().getY());

        if (
            point.getX() < minX - epsilon
         || point.getX() > maxX + epsilon
         || point.getY() < minY - epsilon
         || point.getY() > maxY + epsilon
        ) {
            return false;
        }

        // Check if the point is collinear with the line segment
        return (
            Math.abs(
                crossProduct(
                    new Point(
                        point.getX() - this.start().getX(),
                        point.getY() - this.start().getY()
                    ),
                    new Point(
                        this.end().getX() - this.start().getX(),
                        this.end().getY() - this.start().getY()
                    )
                )
            ) < epsilon
        );
    }

    /**
     * Calculate the shortest distance from this line to a point.
     * Alternatively, the height of the triangle ABC where AB is
     * this Line forming the base and C is the point using Heron's formula.
     *
     * @param point Point to calculate distance to
     * @return the distance from this line to the point
     */
    public double distanceToPoint(Point point) {
        if (this.start().angleBetween(this.end(), point) < 90
        && this.end().angleBetween(this.start(), point) < 90) {
            double ab = this.length();
            double ac = this.start().distance(point);
            double bc = this.end().distance(point);

            double s = (ab + ac + bc) / 2;
            double area = Math.sqrt(s * (s - ab) * (s - ac) * (s - bc));
            return (2 * area) / ab;
        } else {
            return Math.min(
                point.distance(this.start()),
                point.distance(this.end())
            );
        }
    }

    /**
     * Draws the line on the given surface.
     *
     * @param surface The surface to draw the line on.
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(java.awt.Color.BLUE);
        surface.drawLine(
            (int) this.start().getX(),
            (int) this.start().getY(),
            (int) this.end().getX(),
            (int) this.end().getY()
        );
    }

    /**
     * Add point to eventPoints.
     *
     * @param point Point to add
    */
    public void addEventPoint(Point point) {
        this.eventPoints.add(point);
    }
    /**
     * eventPoints getter.
     *
     * @return eventPoints
    */
    public SortedSet<Point> getEventPoints() {
        return this.eventPoints;
    }

    /**
     * .toString overriding to pretty print Lines.
     *
     * @return the pretty String representation of this Line
     */
    public String toString() {
        return "Line (" + point1.toString() + ", " + point2.toString() + ")";
    }
}
