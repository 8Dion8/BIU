import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * Task 2.
 * @author Gleb Shvartser 346832892
 */
public class BouncingBallAnimation {

    private static final int GUI_WIDTH = 800;
    private static final int GUI_HEIGHT = 600;

    /**
     * Main method to set up and run the animation.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        drawAnimation(
            new Point(Double.parseDouble(args[0]),
            Double.parseDouble(args[1])),
            Double.parseDouble(args[2]),
            Double.parseDouble(args[3])
        );
    }

    private static void drawAnimation(Point start, double dx, double dy) {

        GUI gui = new GUI("title", GUI_WIDTH, GUI_HEIGHT);

        Rectangle screenBorders = new Rectangle(0, 0, GUI_WIDTH, GUI_HEIGHT);

        Sleeper sleeper = new Sleeper();
        Ball ball = new Ball(start.getX(), start.getY(), 30, java.awt.Color.BLACK);
        ball.addBorders(screenBorders);
        ball.setVelocity(dx, dy);
        while (true) {
           ball.moveOneStep();
           DrawSurface d = gui.getDrawSurface();
           ball.drawOn(d);
           gui.show(d);
           sleeper.sleepFor(50);  // wait for 50 milliseconds.
        }
     }
}
