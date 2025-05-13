import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Task 4.
 * @author Gleb Shvartser 346832892
 */
public class MultipleFramesBouncingBallsAnimation {

    private static final int GUI_WIDTH = 800;
    private static final int GUI_HEIGHT = 600;

    /**
     * Main method to set up and run the animation.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Create a GUI window
        GUI gui = new GUI("Bouncing Ball Animation With Frames", GUI_WIDTH, GUI_HEIGHT);

        ArrayList<Renderable> renderTargets = new ArrayList<Renderable>();

        ArrayList<Ball> balls = new ArrayList<Ball>();

        Rectangle[] borders = new Rectangle[3];

        Rectangle screenBorders = new Rectangle(0, 0, GUI_WIDTH, GUI_HEIGHT);

        Rectangle grayFrame = new Rectangle(50, 50, 500, 500);
        grayFrame.setRenderColor(java.awt.Color.GRAY);
        grayFrame.setZIndex(0);

        Rectangle yellowFrame = new Rectangle(450, 450, 600, 600);
        yellowFrame.setRenderColor(java.awt.Color.YELLOW);
        yellowFrame.setZIndex(2);

        borders[0] = screenBorders;
        borders[1] = grayFrame;
        borders[2] = yellowFrame;

        renderTargets.add(grayFrame);
        renderTargets.add(yellowFrame);

        Random rand = new Random();
        Rectangle[] exclude;
        Ball ball;
        for (int i = 0; i < args.length; i++) {
            int radius = Integer.parseInt(args[i]);
            if (i >= args.length / 2) {
                exclude = new Rectangle[]{grayFrame, yellowFrame};
                ball = tryPlacementInFrameUntilSuccessful(borders, screenBorders, exclude, radius);
            } else {
                exclude = new Rectangle[]{yellowFrame};
                ball = tryPlacementInFrameUntilSuccessful(borders, grayFrame, exclude, radius);
            }
            int angle = rand.nextInt(361);
            int speed = (100 + rand.nextInt(200)) / (2 * radius);
            Velocity velocity = Velocity.fromAngleAndSpeed(angle, speed);
            ball.setVelocity(velocity);
            balls.add(ball);
            renderTargets.add(ball);
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

    private static Ball tryPlacementInFrameUntilSuccessful(
        Rectangle[] borders,
        Rectangle frame,
        Rectangle[] exclude,
        int radius
    ) {
        int diameter = radius * 2;
        int[] frameCoords = frame.getCoordinates();
        if (diameter >= frameCoords[2] - frameCoords[0] || diameter >= frameCoords[3] - frameCoords[1]) {
            return null;
        }

        int x = frameCoords[0] + (int) (Math.random() * (frameCoords[2] - frameCoords[0] - diameter + 1));
        int y = frameCoords[1] + (int) (Math.random() * (frameCoords[3] - frameCoords[1] - diameter + 1));

        for (Rectangle excludeCheck: exclude) {
            int[] excludeCoordinates = excludeCheck.getCoordinates();
            if (x >= excludeCoordinates[0] && x <= excludeCoordinates[2]
             && y >= excludeCoordinates[1] && y <= excludeCoordinates[3]) {
                return tryPlacementInFrameUntilSuccessful(borders, frame, exclude, radius);
            }
        }

        Ball ball = new Ball(x, y, radius, java.awt.Color.BLACK);

        ball.addBorders(borders[0]);
        ball.addBorders(borders[1]);
        ball.addBorders(borders[2]);

        if (ball.getCollisions().size() > 0) {
            ball = null;
            return tryPlacementInFrameUntilSuccessful(borders, frame, exclude, radius);
        }

        return ball;
    }
}
