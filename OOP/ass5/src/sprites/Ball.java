package sprites;

import collision.CollisionInfo;
import core.Game;
import core.GameEnvironment;
import primitive.Line;
import primitive.Point;
import primitive.Velocity;
import biuoop.DrawSurface;


/**
 * Represents a ball in a 2D space.
 * @author Gleb Shvartser 346832892
 */
public class Ball implements Sprite {

    private Point center;
    private int radius;
    private java.awt.Color color;
    private Velocity vel;
    private GameEnvironment gameEnvironment;

    private final double collisionOffset = 1;

    /**
     * Constructs a ball with the given center, radius, and color.
     * @param center the center of the ball.
     * @param r the radius of the ball.
     * @param color the color of the ball.
     * @param gameEnvironment the game environment.
     */
    public Ball(Point center, int r, java.awt.Color color, GameEnvironment gameEnvironment) {
        this.center = center;
        this.radius = r;
        this.color = color;
        this.vel = new Velocity(0.0, 0.0);
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Constructs a ball with the given center, radius, and color.
     * @param x the x-coordinate of the ball's center.
     * @param y the y-coordinate of the ball's center.
     * @param r the radius of the ball.
     * @param color the color of the ball.
     * @param gameEnvironment the game environment.
     */
    public Ball(double x, double y, int r, java.awt.Color color, GameEnvironment gameEnvironment) {
        this.center = new Point(x, y);
        this.radius = r;
        this.color = color;
        this.vel = new Velocity(0.0, 0.0);
        this.gameEnvironment = gameEnvironment;
    }

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
     * Sets the color of the ball.
     * @param color the color of the ball.
     */
    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    /**
     * Gets the velocity of the ball.
     * @return the velocity of the ball.
     */
    public Velocity getVelocity() {
        return this.vel;
    }

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
     * Gets the game environment of the ball.
     * @return the game environment.
     */
    public GameEnvironment getGameEnvironment() {
        return this.gameEnvironment;
    }

    /**
     * Draws the ball on the given DrawSurface.
     * @param surface the DrawSurface to draw on.
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.radius);
    }

    /**
     * Moves the ball one step according to its velocity.
     */
    public void moveOneStep() {
        //check collisions
        Line trajectory = new Line(this.center, this.getVelocity().applyToPoint(this.center));
        CollisionInfo collision = this.getGameEnvironment().getClosestCollision(trajectory);
        this.center = this.getVelocity().applyToPoint(this.center);
        if (collision != null) {
            //calculate new velocity and where we need to offset the ball
            Line collisionLine = collision.collisionObject().getCollisionRectangle().getClosestEdge(trajectory);
            this.vel = collision.collisionObject().hit(this, collision.collisionPoint(), this.getVelocity());
            this.center = collision.collisionPoint();
            //move ball slightly away from the border we collided with
            if (collisionLine.isHorizontal()) {
                if (collisionLine.start().getY() - trajectory.start().getY() > 0) {
                    this.center.setY(this.center.getY() - collisionOffset);
                } else {
                    this.center.setY(this.center.getY() + collisionOffset);
                }
            } else {
                if (collisionLine.start().getX() - trajectory.start().getX() > 0) {
                    this.center.setX(this.center.getX() - collisionOffset);
                } else {
                    this.center.setX(this.center.getX() + collisionOffset);
                }
            }
        }
    }

    /**
     * Updates the ball's position based on its velocity.
     */
    public void timePassed() {
        this.moveOneStep();
    }

    /**
     * Adds the ball to the game.
     * @param game The game to add the ball to.
     */
    public void addToGame(Game game) {
        game.addSprite(this);
    }

    public void removeFromGame(Game game) {
        game.removeSprite(this);
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
