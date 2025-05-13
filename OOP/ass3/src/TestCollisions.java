import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;
import java.awt.Color;

/**
 * TestCollisions class for testing collisions between a ball and various blocks.
 * @author Gleb Shvartser 346832892
 */
public class TestCollisions {
    /**
     * Main method for running the TestCollisions program.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Test Collisions", 1000, 1000);
        Sleeper sleeper = new Sleeper();

        Ball ball = new Ball(200, 300, 10, Color.RED, new GameEnvironment());
        ball.setVelocity(50, 20);

        Block leftWall = new Block(0, 0, 10, 600, Color.BLACK, Color.BLACK);
        Block rightWall = new Block(790, 0, 10, 600, Color.BLACK, Color.BLACK);
        Block topWall = new Block(0, 0, 800, 10, Color.BLACK, Color.BLACK);
        Block bottomWall = new Block(0, 590, 800, 10, Color.BLACK, Color.BLACK);
        Block middleWall = new Block(390, 100, 20, 400, Color.BLACK, Color.BLACK);

        ball.getGameEnvironment().addCollidable(leftWall);
        ball.getGameEnvironment().addCollidable(rightWall);
        ball.getGameEnvironment().addCollidable(topWall);
        ball.getGameEnvironment().addCollidable(bottomWall);
        ball.getGameEnvironment().addCollidable(middleWall);

        while (true) {
            DrawSurface surface = gui.getDrawSurface();
            ball.moveOneStep();
            leftWall.drawOn(surface);
            rightWall.drawOn(surface);
            topWall.drawOn(surface);
            bottomWall.drawOn(surface);
            middleWall.drawOn(surface);
            ball.drawOn(surface);
            gui.show(surface);
            sleeper.sleepFor(10);
        }
    }
}
