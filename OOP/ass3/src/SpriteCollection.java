import biuoop.DrawSurface;
import java.util.HashSet;
import java.util.Set;

/**
 * SpriteCollection class represents a collection of sprites.
 * @author Gleb Shvartser 346832892
 */
public class SpriteCollection {
    private Set<Sprite> collection;

    /**
     * Constructs a new SpriteCollection.
     */
    public SpriteCollection() {
        this.collection = new HashSet<Sprite>();
    }

    /**
     * Adds a sprite to the collection.
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.collection.add(s);
    }

    /**
     * Notifies all sprites in the collection that time has passed.
     */
    public void notifyAllTimePassed() {
        for (Sprite sprite : this.collection) {
            sprite.timePassed();
        }
    }

    /**
     * Draws all sprites in the collection on the given DrawSurface.
     * @param d the DrawSurface to draw on
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite sprite : this.collection) {
            sprite.drawOn(d);
        }
    }
}
