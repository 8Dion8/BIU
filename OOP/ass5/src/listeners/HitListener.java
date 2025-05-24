package listeners;

import sprites.Ball;
import sprites.Block;

/**
 * The HitListener interface should be implemented by any class that wants to be notified
 * of hit events in the game. Classes implementing this interface can define specific
 * behavior to execute when a Block is hit by a Ball.
 * @author Gleb Shvartser 346832892
 */
public interface HitListener {
    /**
    * This method is called whenever the beingHit object is hit.
    *
    * @param beingHit the Block that is being hit
    * @param hitter the Ball that is doing the hitting
    */
    void hitEvent(Block beingHit, Ball hitter);
}
