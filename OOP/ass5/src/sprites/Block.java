package sprites;

import java.awt.Color;
import java.util.ArrayList;

import collision.Collidable;
import core.Game;
import listeners.HitListener;
import listeners.HitNotifier;
import primitive.Line;
import primitive.Point;
import primitive.Rectangle;
import primitive.Velocity;
import biuoop.DrawSurface;

/**
 * A class representing a block in a game.
 * @author Gleb Shvartser 346832892
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private Rectangle rectangle;
    private Color color;
    private Color borderColor;
    private ArrayList<HitListener> hitListeners;

    private static final int BORDER_WIDTH = 1;

    /**
    * Construct a block from a rectangle and a color.
    *
    * @param rectangle the rectangle representing the block
    * @param color the color of the block
    * @param borderColor the color of the border of the block
    */
    public Block(Rectangle rectangle, Color color, Color borderColor) {
        this.rectangle = rectangle;
        this.color = color;
        this.borderColor = borderColor;
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
    * Construct a block from its position and dimensions.
    *
    * @param x the x-coordinate of the top-left corner of the block
    * @param y the y-coordinate of the top-left corner of the block
    * @param width the width of the block
    * @param height the height of the block
    * @param color the color of the block
    * @param borderColor the color of the border of the block
    */
    public Block(int x, int y, int width, int height, Color color, Color borderColor) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.color = color;
        this.borderColor = borderColor;
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
    * Construct a block from an array of integers representing its position and dimensions.
    *
    * @param data an array of four integers representing the x and y coordinates of the top-left corner,
    *             and the width and height of the block
    * @param color the color of the block
    * @param borderColor the color of the border of the block
    */
    public Block(int[] data, Color color, Color borderColor) {
        if (data.length != 4) {
            throw new IllegalArgumentException("Invalid data array length");
        }

        this.rectangle = new Rectangle(data[0], data[1], data[2], data[3]);
        this.color = color;
        this.borderColor = borderColor;
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
    * Return the "collision shape" of the object.
    *
    * @return the collision rectangle of the block
    */
    public Rectangle getCollisionRectangle() {
        return rectangle;
    }

    public Color getColor() {
        return this.color;
    }

    /**
    * Notify the object that a collision occurred at the specified collision point with
    * a given velocity. This method calculates and returns the new velocity expected
    * after the collision, based on the force the object inflicted.
    *
    * @param collisionPoint the point at which the collision occurred
    * @param currentVelocity the velocity of the object before the collision
    * @return the new velocity of the object after the collision
    */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        if (!ballColorMatch(hitter)) {
            this.notifyHit(hitter);
        }

        Velocity newVelocity = currentVelocity;
        for (Line edge : rectangle.getEdges()) {
            if (edge.isPointOnLineSegment(collisionPoint)) {
                if (edge.isHorizontal()) {
                    newVelocity.flipY();
                } else if (edge.isVertical()) {
                    newVelocity.flipX();
                }
                return newVelocity;
            }
        }

        return newVelocity;
    }

    public boolean ballColorMatch(Ball ball) {
        return ball.getColor() == this.color;
    }

    /**
    * ok but WHY do we need a javadoc here? I refuse.
    */
    public void timePassed() {
        // No action needed for this block
    }

    /**
    * Draw the block on the given surface.
    *
    * @param surface the surface to draw the block on
    */
    public void drawOn(DrawSurface surface) {

        surface.setColor(this.borderColor);
        surface.fillRectangle(
            (int) this.rectangle.getUpperLeft().getX(),
            (int) this.rectangle.getUpperLeft().getY(),
            (int) this.rectangle.getWidth(),
            (int) this.rectangle.getHeight());

        surface.setColor(color);
        surface.fillRectangle(
            (int) this.rectangle.getUpperLeft().getX() + BORDER_WIDTH,
            (int) this.rectangle.getUpperLeft().getY() + BORDER_WIDTH,
            (int) this.rectangle.getWidth() - 2 * BORDER_WIDTH,
            (int) this.rectangle.getHeight() - 2 * BORDER_WIDTH);
    }

    /**
    * Add the block to the game.
    *
    * @param game the game to add the block to
    */
    public void addToGame(Game game) {
        game.addCollidable(this);
        game.addSprite(this);
    }

    public void removeFromGame(Game game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    private void notifyHit(Ball hitter) {
        ArrayList<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }
}
