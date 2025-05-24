package listeners;

import core.Game;
import primitive.Counter;
import sprites.Ball;
import sprites.Block;

/**
 * The BallRemover class is responsible for removing balls from the game
 * when they hit a specific block and updating the count of remaining balls.
 * @author Gleb Shvartser 346832892
 */
public class BallRemover implements HitListener {
    private Game game;
    private Counter remainingBalls;

    /**
     * Constructs a BallRemover.
     *
     * @param game the game instance from which balls will be removed
     * @param remainingBalls a counter tracking the number of balls remaining in the game
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(this.game);
        this.remainingBalls.decrease(1);
    }
}
