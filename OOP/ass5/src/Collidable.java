package gameCollision;

/**
 * Represents a collidable object in the game.
 * @author Gleb Shvartser 346832892
 */
public interface Collidable {
   /**
    * Return the rectangle representing the collision of the object.
    *
    * @return the rectangle representing the collision of the object
    */
    Rectangle getCollisionRectangle();

    /**
    * Notify the object that a collision occurred at the specified collisionPoint
    * with a given velocity.
    *
    * @param collisionPoint the point at which the collision occurred
    * @param currentVelocity the velocity of the object before the collision
    * @return the new velocity expected after the hit, based on the force
    *         the object inflicted during the collision
    */
    Velocity hit(Point collisionPoint, Velocity currentVelocity);
}
