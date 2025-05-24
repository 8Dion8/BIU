package core;

import collision.Collidable;
import collision.CollisionInfo;
import primitive.Line;
import primitive.Point;
import primitive.Rectangle;

import java.util.ArrayList;

/**
 * GameEnvironment class represents the environment of the ball.
 * @author Gleb Shvartser 346832892
 */
public class GameEnvironment {

    private ArrayList<Collidable> collidables;

    /**
     * Constructor for GameEnvironment.
     */
    public GameEnvironment() {
        this.collidables = new ArrayList<Collidable>();
    }

    /**
     * Adds the given collidable to the environment.
     *
     * @param c The collidable to be added.
     */
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }

    /**
     * Removes the given collidable from the environment.
     *
     * @param c The collidable to be removed.
     */
    public void removeCollidable(Collidable c) {
        this.collidables.remove(c);
    }

    /**
     * Finds the closest collision between the trajectory and the collidables in the environment.
     *
     * @param trajectory The trajectory of the ball.
     * @return The closest collision information.
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
       double minDistance = Double.MAX_VALUE;
       CollisionInfo closestCollision = null;

       for (Collidable collidable : this.collidables) {
           Rectangle hitBox = collidable.getCollisionRectangle();

           Point collisionPoint = trajectory.closestIntersectionToStartOfLine(hitBox);
           if (collisionPoint != null) {
               double distance = trajectory.start().distance(collisionPoint);
               if (distance < minDistance) {
                   minDistance = distance;
                   closestCollision = new CollisionInfo(collisionPoint, collidable);
               }
           }
       }

       return closestCollision;
   }

}
