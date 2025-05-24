package sprites;
import core.Game;
import biuoop.DrawSurface;

/**
 * Represents a sprite that can be drawn on a DrawSurface and notified of time passing.
 * @author Gleb Shvartser 346832892
 */
public interface Sprite {
   /**
    * Draws the sprite on the given DrawSurface.
    * @param d the DrawSurface to draw on
    */
   void drawOn(DrawSurface d);
   /**
    * Notifies the sprite that time has passed.
    */
   void timePassed();
   /**
    * Adds the sprite to the given game.
    * @param game the game to add the sprite to
    */
   void addToGame(Game game);
}
