import biuoop.DrawSurface;
import java.util.ArrayList;

/**
 * Represents a ball in a 2D space.
 * @author Gleb Shvartser 346832892
 */
public class Ball extends Renderable {

    private Point center;
    private int radius;
    private java.awt.Color color;
    private Velocity vel;
    private ArrayList<Rectangle> borders;

    private final double epsilon = 0.00001;

    // Constructors
    /**
     * Constructs a ball with the given center, radius, and color.
     * @param center the center of the ball.
     * @param r the radius of the ball.
     * @param color the color of the ball.
     */
    public Ball(Point center, int r, java.awt.Color color) {
        this.center = center;
        this.radius = r;
        this.color = color;
        this.vel = new Velocity(0.0, 0.0);
        this.borders = new ArrayList<Rectangle>();
        this.setZIndex(1);
    }

    /**
     * Constructs a ball with the given center, radius, and color.
     * @param x the x-coordinate of the ball's center.
     * @param y the y-coordinate of the ball's center.
     * @param r the radius of the ball.
     * @param color the color of the ball.
     */
    public Ball(double x, double y, int r, java.awt.Color color) {
        this.center = new Point(x, y);
        this.radius = r;
        this.color = color;
        this.vel = new Velocity(0.0, 0.0);
        this.borders = new ArrayList<Rectangle>();
        this.setZIndex(1);
    }

    // Accessors
    /**
     * Gets the x-coordinate of the ball's center.
     * @return the x-coordinate of the ball's center.
     */
    public int getX() {
        return (int) this.center.getX();
    }

    /**
     * Gets the y-coordinate of the ball's center.
     * @return the y-coordinate of the ball's center.
     */
    public int getY() {
        return (int) this.center.getY();
    }

    /**
     * Gets the size (radius) of the ball.
     * @return the radius of the ball.
     */
    public int getSize() {
        return this.radius;
    }

    /**
     * Gets the color of the ball.
     * @return the color of the ball.
     */
    public java.awt.Color getColor() {
        return this.color;
    }

    /**
     * Gets the velocity of the ball.
     * @return the velocity of the ball.
     */
    public Velocity getVelocity() {
        return this.vel;
    }

    // Mutators
    /**
     * Sets the velocity of the ball.
     * @param v the new velocity.
     */
    public void setVelocity(Velocity v) {
        this.vel = v;
    }

    /**
     * Sets the velocity of the ball.
     * @param dx the change in x direction.
     * @param dy the change in y direction.
     */
    public void setVelocity(double dx, double dy) {
        this.vel = new Velocity(dx, dy);
    }

    /**
     * Adds a border to the ball's list of borders.
     * @param rect the rectangle representing the border.
     */
    public void addBorders(Rectangle rect) {
        this.borders.add(rect);
    }

    // Drawing
    /**
     * Draws the ball on the given DrawSurface.
     * @param surface the DrawSurface to draw on.
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.radius);
    }

    // Movement
    /**
     * Moves the ball one step according to its velocity.
     */
    public void moveOneStep() {
        this.center = this.getVelocity().applyToPoint(this.center);
        this.handleCollisions();
    }

    // Collision Handling
    /**
        * Checks if the ball collides with a given border.
        * @param border the border to check collision with.
        * @return true if the ball collides with the border, false otherwise.
        */
    public boolean collidesWith(Border border) {
        Line directionVector = new Line(
            this.center,
            this.getVelocity().applyToPoint(this.center)
        );
        double dvDx = directionVector.end().getX() - directionVector.start().getX();
        double dvDy = directionVector.end().getY() - directionVector.start().getY();
        double scale = this.radius / directionVector.length();
        double extendedX = directionVector.end().getX() + (dvDx * scale);
        double extendedY = directionVector.end().getY() + (dvDy * scale);

        Line dvCollisionLine = new Line(
            this.center,
            new Point(extendedX, extendedY)
        );

        // CASE 1: Ball is heading towards border and collides at normal speed
        // check if it will collide on next frame
        if (border.getLine().distanceToPoint(directionVector.end()) < this.radius + epsilon) {
             return true;
        }

        // CASE 2: Ball is heading towards border and will collide, but is moving too fast
        // and next frame will "skip over"; check if trajectory intersects with border
        if (dvCollisionLine.isIntersecting(border.getLine())) {
             return true;
        }

        // CASE 3: Ball is heading next to the edge of a border and will collide on the side,
        // but trajectory of center doesn't intersect;
        // In this case, essentially draw a triangle where trajectory is the base and edge border is the peak;
        // check if height of triangle (distance from ball to edge) is less than radius and that we are actually
        // near the edge by checking angles from base
        if (dvCollisionLine.distanceToPoint(border.getLine().start()) < this.radius + epsilon) {
            return true;
        }
        if (dvCollisionLine.distanceToPoint(border.getLine().end()) < this.radius + epsilon) {
            return true;
        }

        return false;
    }

    /**
     * Gets a list of borders that the ball is colliding with.
     * @return a list of borders that the ball is colliding with.
     */
    public ArrayList<Border> getCollisions() {
        ArrayList<Border> collisions = new ArrayList<Border>();

        for (Rectangle rect: this.borders) {
            for (Border border: rect.getBorders()) {
                if (this.collidesWith(border)) {
                    collisions.add(border);
                }
            }
        }
        return collisions;
    }

    /**
     * Handles collisions by adjusting the ball's velocity.
     */
    public void handleCollisions() {
        ArrayList<Border> collisions = this.getCollisions();
        for (Border border: collisions) {
            if (border.isHorizontal()) {
                this.flipYVel();
            } else {
                this.flipXVel();
            }
        }
    }

    // Velocity Flipping
    /**
     * Flips the ball's velocity in the x direction.
     */
    public void flipXVel() {
        this.vel.flipX();
    }

    /**
     * Flips the ball's velocity in the y direction.
     */
    public void flipYVel() {
        this.vel.flipY();
    }
}
