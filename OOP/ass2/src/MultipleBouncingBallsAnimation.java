import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
import java.util.ArrayList;

/**
 * Task3.
 * @author Gleb Shvartser 346832892
 */
public class MultipleBouncingBallsAnimation {

    private static final int GUI_WIDTH = 800;
    private static final int GUI_HEIGHT = 600;

    /**
     * Main method to set up and run the animation.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Create a GUI window
        GUI gui = new GUI("Bouncing Ball Animation", GUI_WIDTH, GUI_HEIGHT);

        ArrayList<Renderable> renderTargets = new ArrayList<Renderable>();

        ArrayList<Ball> balls = new ArrayList<Ball>();

        Rectangle[] borders = new Rectangle[1];
        Rectangle screenBorders = new Rectangle(0, 0, GUI_WIDTH, GUI_HEIGHT);
        borders[0] = screenBorders;

        for (String arg : args) {
            try {
                int radius = Integer.parseInt(arg);
                int x = (int) (Math.random() * (GUI_WIDTH - 2 * radius)) + radius;
                int y = (int) (Math.random() * (GUI_HEIGHT - 2 * radius)) + radius;
                int dx = (radius > 50) ? 1 : (int) (Math.random() * 5) + 1;
                int dy = (radius > 50) ? 1 : (int) (Math.random() * 5) + 1;

                Ball ball = new Ball(x, y, radius, java.awt.Color.BLACK);
                ball.setVelocity(dx, dy);
                ball.addBorders(borders[0]);
                balls.add(ball);
                renderTargets.add(ball);

            } catch (NumberFormatException e) {
                System.out.println("Invalid argument: " + arg);
            }
        }

        drawAnimation(gui, balls, renderTargets);
    }

    private static void drawAnimation(GUI gui, ArrayList<Ball> balls, ArrayList<Renderable> renderTargets) {
        Sleeper sleeper = new Sleeper();
        while (true) {
            for (Ball ball : balls) {
                ball.moveOneStep();
            }

            DrawSurface d = gui.getDrawSurface();
            Renderable.drawRenderTargets(renderTargets, d);
            gui.show(d);

            sleeper.sleepFor(50); // wait for 50 milliseconds.
        }
    }
}
