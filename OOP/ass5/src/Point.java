package geometryPrimitive;

/**
 * Class Point is responsible for defining and managing a point.
 * @author Gleb Shvartser 346832892
 */
public class Point implements Comparable<Point> {

    private double x;
    private double y;

    private final double epsilon = 0.00001;

    /**
     * Returns the distance between this and another point.
     *
     * @param x point x
     * @param y point y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the distance between this and another point.
     *
     * @param other the other point
     * @return      the distance between the two points
     */
    public double distance(Point other) {
        return Math.sqrt(
            Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2)
        );
    }

    /**
     * Returns whether or not two points are equal.
     *
     * @param obj Other object to compare to. if not Point - return false
     * @return true if the points are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Point other = (Point) obj;
        return (
            Math.abs(this.x - other.x) < epsilon
         && Math.abs(this.y - other.y) < epsilon
        );
    }

    @Override
    public int hashCode() {
        double roundedX = Math.round(this.x / epsilon) * epsilon;
        double roundedY = Math.round(this.y / epsilon) * epsilon;

        int result = Double.hashCode(roundedX);
        result = 31 * result + Double.hashCode(roundedY);

        return result;
    }

    /**
     * Comparison method override implementation for SortedSet.
     *
     * @param other other point to compare to
     * @return comparison result
     */
    @Override
    public int compareTo(Point other) {
        if (this.x < other.x || (this.x == other.x && this.y < other.y)) {
            return -1;
        }
        return this.x == other.x && this.y == other.y ? 0 : 1;
    }

    /**
     * Returns a new point as a result of subtracting another point.
     *
     * @param other the other point
     * @return      the new point
     */
    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    /**
     * Returns a new point as a result of adding another point.
     *
     * @param other the other point
     * @return      the new point
     */
    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    /**
     * Returns the angle between this point and two other points.
     *
     * @param a the first point
     * @param c the second point
     * @return  the angle between the points
     */
    public double angleBetween(Point a, Point c) {
        Point b = this; // for readability
        double ab = b.distance(a);
        double bc = b.distance(c);
        double ac = a.distance(c);
        // Law of Cosines
        return Math.acos((ab * ab + bc * bc - ac * ac) / (2 * ab * bc)) * (180 / Math.PI);
    }

    /**
     * .toString overriding to pretty print Points.
     *
     * @return the pretty String representation of this Point
     */
    public String toString() {
        return "Point(" + String.valueOf(x) + "," + String.valueOf(y) + ")";
    }

    /**
     * X Getter.
     *
     * @return you'll never guess what...
     */
    public double getX() {
        return this.x;
    }

    /**
     * X Setter.
     *
     * @param newX the new X coordinate
     */
    public void setX(double newX) {
        this.x = newX;
    }

    /**
     * Y Setter.
     *
     * @param newY the new Y coordinate
     */
    public void setY(double newY) {
        this.y = newY;
    }

    /**
     * Y Getter.
     *
     * @return such a mystery
     */
    public double getY() {
        return this.y;
    }
}
