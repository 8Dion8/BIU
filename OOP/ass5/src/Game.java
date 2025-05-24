import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
import biuoop.KeyboardSensor;
import java.awt.Color;

/**
 * Game class represents the game.
 */
public class Game {
   private SpriteCollection sprites;
   private GameEnvironment environment;
   private GUI gui;
   private Sleeper sleeper;
   private KeyboardSensor keyboard;
   private Ball ball1;
   private Ball ball2;
   private Paddle paddle;
   private Counter blockCounter;
   private HitListener listener;


   private static final int SCREEN_WIDTH = 800;
   private static final int SCREEN_HEIGHT = 600;

   private static final int BLOCKS_ROWS = 5;
   private static final int TOP_BLOCK_ROW_BLOCK_COUNT = 12;

   private static final int WALL_THICKNESS = 10;
   private static final int[] LEFT_WALL_DATA = {0, 0, WALL_THICKNESS, SCREEN_HEIGHT};
   private static final int[] RIGHT_WALL_DATA = {SCREEN_WIDTH - WALL_THICKNESS, 0, WALL_THICKNESS, SCREEN_HEIGHT};
   private static final int[] TOP_WALL_DATA = {0, 0, SCREEN_WIDTH, WALL_THICKNESS};
   private static final int[] BOTTOM_WALL_DATA = {0, SCREEN_HEIGHT - WALL_THICKNESS, SCREEN_WIDTH, WALL_THICKNESS};

   private static final int GENERIC_BLOCK_WIDTH = 40;
   private static final int GENERIC_BLOCK_HEIGHT = 20;
   private static final int GENERIC_BLOCK_ROW_OFFSET_X = SCREEN_WIDTH
                                                       - (TOP_BLOCK_ROW_BLOCK_COUNT * GENERIC_BLOCK_WIDTH)
                                                       - WALL_THICKNESS;
   private static final int GENERIC_BLOCK_ROW_OFFSET_Y = 100;

   private static final int BALL_STARTING_X = SCREEN_WIDTH / 2;
   private static final int BALL_STARTING_Y = SCREEN_HEIGHT - 250;
   private static final int BALL_RADIUS = 5;
   private static final int BALL_STARTING_SPEED_X = 3;
   private static final int BALL_STARTING_SPEED_Y = -3;

   private static final int PADDLE_STARTING_X = SCREEN_WIDTH / 2;
   private static final int PADDLE_STARTING_Y = SCREEN_HEIGHT - 200;
   private static final int PADDLE_WIDTH = 100;
   private static final int PADDLE_HEIGHT = 20;
   private static final int PADDLE_SPEED = 5;
   private static final int PADDLE_LEFT_CONSTRAINT = WALL_THICKNESS;
   private static final int PADDLE_RIGHT_CONSTRAINT = SCREEN_WIDTH - WALL_THICKNESS - PADDLE_WIDTH;

   private static final Color BACKGROUND_COLOR = ColorschemeNord.DARK0;
   private static final Color WALL_COLOR = ColorschemeNord.DARK1;
   private static final Color PADDLE_COLOR = ColorschemeNord.LIGHT0;
   private static final Color BALL_COLOR = ColorschemeNord.ACCENT0;
   private static final Color BLOCK_BORDER_COLOR = ColorschemeNord.DARK1;
   private static final Color[] BLOCK_COLORS = {
       ColorschemeNord.RED,
       ColorschemeNord.ORANGE,
       ColorschemeNord.YELLOW,
       ColorschemeNord.GREEN,
       ColorschemeNord.PURPLE
   };

   private static final int TARGET_FPS = 60;
   private static final int FRAMETIME = 1000 / TARGET_FPS;

   /**
    * Constructs a new Game object.
    */
   public Game() {
       this.sprites = new SpriteCollection();
       this.environment = new GameEnvironment();
       this.sleeper = new Sleeper();
       this.blockCounter = new Counter();
   }

   /**
    * Add a collidable to the game environment.
    *
    * @param c The collidable to add to the game environment.
    */
   public void addCollidable(Collidable c) {
       this.environment.addCollidable(c);
   }

   /**
    * Add a sprite to the sprite collection.
    *
    * @param s The sprite to add to the sprite collection.
    */
   public void addSprite(Sprite s) {
       this.sprites.addSprite(s);
   }

    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

   /**
    * Add a sprite to the game.
    * If the sprite is a ball, add it to the sprite collection.
    * If the sprite is a block, add it to the sprite collection and the game environment.
    * If the sprite is a paddle, add it to the sprite collection and the game environment.
    *
    * @param s The sprite to add to the game.
    */
   public void addToGame(Sprite s) {
       if (s instanceof Ball) {
           this.addSprite((Ball) s);
       } else if (s instanceof Block) {
           this.addSprite((Block) s);
           this.addCollidable((Block) s);
       } else if (s instanceof Paddle) {
           this.addSprite((Paddle) s);
           this.addCollidable((Paddle) s);
       }
   }

   /**
    * Initialize a new game: create the Blocks and Ball (and Paddle)
    * and add them to the game.
    */
   public void initialize() {
       this.gui = new GUI("Arkanoid but the budget got cut halfway through", SCREEN_WIDTH, SCREEN_HEIGHT);
       this.keyboard = gui.getKeyboardSensor();

       this.ball1 = new Ball(
           BALL_STARTING_X,
           BALL_STARTING_Y,
           BALL_RADIUS,
           BALL_COLOR,
           this.environment
       );
       this.ball1.setVelocity(BALL_STARTING_SPEED_X, BALL_STARTING_SPEED_Y);
       this.ball1.addToGame(this);

       this.ball2 = new Ball(
           BALL_STARTING_X,
           BALL_STARTING_Y,
           BALL_RADIUS,
           BALL_COLOR,
           this.environment
       );
       this.ball2.setVelocity(-BALL_STARTING_SPEED_X, BALL_STARTING_SPEED_Y);
       this.ball2.addToGame(this);

       this.paddle = new Paddle(
           this.keyboard,
           PADDLE_STARTING_X,
           PADDLE_STARTING_Y,
           PADDLE_SPEED,
           PADDLE_WIDTH,
           PADDLE_HEIGHT,
           PADDLE_COLOR
       );
       this.paddle.setConstraints(PADDLE_LEFT_CONSTRAINT, PADDLE_RIGHT_CONSTRAINT);
       this.paddle.addToGame(this);

       new Block(LEFT_WALL_DATA, WALL_COLOR, WALL_COLOR).addToGame(this);
       new Block(RIGHT_WALL_DATA, WALL_COLOR, WALL_COLOR).addToGame(this);
       new Block(TOP_WALL_DATA, WALL_COLOR, WALL_COLOR).addToGame(this);
       new Block(BOTTOM_WALL_DATA, WALL_COLOR, WALL_COLOR).addToGame(this);

       this.listener = new BlockRemover(this, this.blockCounter);

       for (int i = 0; i < BLOCKS_ROWS; i++) {
           for (int j = i; j < TOP_BLOCK_ROW_BLOCK_COUNT; j++) {
               int x = GENERIC_BLOCK_ROW_OFFSET_X + j * GENERIC_BLOCK_WIDTH;
               int y = GENERIC_BLOCK_ROW_OFFSET_Y + i * GENERIC_BLOCK_HEIGHT;
               Block block = new Block(
                   new Rectangle(x, y, GENERIC_BLOCK_WIDTH, GENERIC_BLOCK_HEIGHT),
                   BLOCK_COLORS[i],
                   BLOCK_BORDER_COLOR
               );
               block.addHitListener(this.listener);
               block.addToGame(this);
           }
       }
   }

   /**
    * Run the game -- start the animation loop.
    */
   public void run() {
        while (true) {
            long startTime = System.currentTimeMillis(); // timing

            DrawSurface d = this.gui.getDrawSurface();
            d.setColor(BACKGROUND_COLOR);
            d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
            this.sprites.drawAllOn(d);
            this.gui.show(d);
            this.sprites.notifyAllTimePassed();

            // timing
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = FRAMETIME - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}
