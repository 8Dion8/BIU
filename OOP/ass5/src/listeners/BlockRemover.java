package listeners;

import core.Game;
import primitive.Counter;
import sprites.Ball;
import sprites.Block;

/**
 * A BlockRemover is responsible for removing blocks from the game and keeping track
 * of the number of blocks that remain.
 * @author Gleb Shvartser 346832892
 */
public class BlockRemover implements HitListener {
    private Game game;
    private Counter remainingBlocks;

    /**
     * Constructs a BlockRemover.
     *
     * @param game the game instance from which blocks will be removed
     * @param remainingBlocks a counter to keep track of the remaining blocks
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.setColor(beingHit.getColor());
        beingHit.removeFromGame(this.game);
        this.remainingBlocks.decrease(1);
    }
}
