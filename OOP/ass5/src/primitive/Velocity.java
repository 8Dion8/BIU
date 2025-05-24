package primitive;

/**
 * Class Velocity is responsible for defining and managing a velocity.
 * @author Gleb Shvartser 346832892
 */
public class Velocity {

    private double dx;
    private double dy;

    /**
     * Constructs a Velocity object with the specified dx and dy components.
     *
     * @param dx the change in x-axis (horizontal velocity)
     * @param dy the change in y-axis (vertical velocity)
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Constructs a Velocity object from an angle and speed.
     *
     * @param angle the angle in degrees
     * @param speed the speed
     * @return a Velocity object
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double angleRadians = Math.toRadians(-angle + 90);
        double dx = speed * Math.cos(angleRadians);
        double dy = speed * Math.sin(angleRadians);
        return new Velocity(dx, dy);
    }

    /**
     * Applies the velocity to a point and returns a new point.
     *
     * @param p the point to apply the velocity to
     * @return a new point with the updated position
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }

    /**
     * Flips the velocity horizontally.
     */
    public void flipX() {
        this.dx = -this.dx;
    }

    /**
     * Flips the velocity vertically.
     */
    public void flipY() {
        this.dy = -this.dy;
    }

    /**
     * Returns the speed of the velocity.
     *
     * @return the speed of the velocity
     */
    public double getSpeed() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "(" + dx + ", " + dy + ")";
    }

}
