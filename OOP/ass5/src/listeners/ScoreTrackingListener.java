package listeners;

import primitive.Counter;
import sprites.Ball;
import sprites.Block;

/**
* The ScoreTrackingListener class is responsible for tracking the score in the game.
* It listens for hit events and updates the score accordingly.
* @author Gleb Shvartser 346832892
*/
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
    * Constructs a ScoreTrackingListener with the given score counter.
    *
    * @param scoreCounter the Counter object used to track the current score
    */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }


    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        this.currentScore.increase(5);
    }
}
