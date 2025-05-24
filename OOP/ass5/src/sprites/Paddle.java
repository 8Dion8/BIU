package sprites;

import collision.Collidable;
import core.Game;
import primitive.Line;
import primitive.Point;
import primitive.Rectangle;
import primitive.Velocity;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import java.awt.Color;

/**
 * Paddle class represents a paddle that can be controlled by the user using the keyboard.
 * It implements the Sprite and Collidable interfaces.
 * @author Gleb Shvartser 346832892
 */
public class Paddle implements Sprite, Collidable {

    private biuoop.KeyboardSensor keyboard;
    private int speed;
    private int width;
    private int height;
    private int x;
    private int y;
    private int constraintLeft;
    private int constraintRight;
    private Rectangle collisionRectangle;
    private Rectangle[] collisionSegments;
    private Color color;

    /**
    * Constructs a new Paddle object with the given parameters.
    * @param keyboard the keyboard sensor to control the paddle
    * @param x the x-coordinate of the paddle's top-left corner
    * @param y the y-coordinate of the paddle's top-left corner
    * @param speed the speed at which the paddle moves
    * @param width the width of the paddle
    * @param height the height of the paddle
    * @param color the color of the paddle
    */
    public Paddle(biuoop.KeyboardSensor keyboard, int x, int y, int speed, int width, int height, Color color) {
        this.keyboard = keyboard;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
        this.collisionRectangle = new Rectangle(new Point(x, y), width, height);
        //divide collisionRectangle into 5 segments
        this.collisionSegments = new Rectangle[5];
        int segmentWidth = width / 5;
        for (int i = 0; i < 5; i++) {
            collisionSegments[i] = new Rectangle(new Point(x + i * segmentWidth, y), segmentWidth, height);
        }
    }

    /**
    * Sets the constraints for the paddle's movement.
    * @param constraintLeft the left constraint of the paddle's movement
    * @param constraintRight the right constraint of the paddle's movement
    */
    public void setConstraints(int constraintLeft, int constraintRight) {
        this.constraintLeft = constraintLeft;
        this.constraintRight = constraintRight;
    }

    /**
    * Moves the paddle to the left.
    */
    public void moveLeft() {
        move(-speed);
    }

    /**
    * Moves the paddle to the right.
    */
    public void moveRight() {
        move(speed);
    }

    /**
    * Move the paddle while keeping in mind the Constraints.
    *
    * @param dx amount to move paddle by
    */
    public void move(int dx) {
        if (x + dx > this.constraintRight) {
            dx = this.constraintLeft - x;
        } else if (x + dx < this.constraintLeft) {
            dx = this.constraintRight - x;
        }

        x += dx;
        this.collisionRectangle.moveX(dx);
        for (Rectangle segment : collisionSegments) {
            segment.moveX(dx);
        }
    }

    @Override
    public void timePassed() {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        }
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle(x, y, width, height);
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }


    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        Rectangle segmentCollidedWith = null;
        int segmentCollidedWithIndex = -1;
        for (int i = 0; i < collisionSegments.length; i++) {
            Rectangle segment = collisionSegments[i];
            for (Line border: segment.getEdges()) {
                if (border.isPointOnLineSegment(collisionPoint)) {
                    segmentCollidedWith = segment;
                    segmentCollidedWithIndex = i;
                    break;
                }
            }
            if (segmentCollidedWith != null) {
                break;
            }
        }
        // 360 is NOT up.
        switch (segmentCollidedWithIndex) {
            case 0:
                return Velocity.fromAngleAndSpeed(240, currentVelocity.getSpeed());
            case 1:
                return Velocity.fromAngleAndSpeed(210, currentVelocity.getSpeed());
            case 3:
                return Velocity.fromAngleAndSpeed(150, currentVelocity.getSpeed());
            case 4:
                return Velocity.fromAngleAndSpeed(120, currentVelocity.getSpeed());
            case 2:
                currentVelocity.flipY();
                return currentVelocity;
            default:
                currentVelocity.flipX();
                return currentVelocity;
        }
    }

    @Override
    public void addToGame(Game game) {
        game.addCollidable(this);
        game.addSprite(this);
    }

    @Override
    public void removeFromGame(Game game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }
}
