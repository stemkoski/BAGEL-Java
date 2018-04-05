package net.stemkoski.bagel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;  
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

/**
 *  Main class to be extended for game projects.
 *  Creates the window, {@link Input} and {@link Group} objects, 
 *    and manages the life cycle of the game (initialization and game loop). 
 */
public class Game extends Application 
{
	/**
	 * default width of game canvas. may change if desired.
	 */
	public static int windowWidth  = 800;
	
	/**
	 * default height of game canvas. may change if desired.
	 */
	public static int windowHeight = 600;
	
	/**
	 * area where game graphics are displayed
	 */
	Canvas canvas;
	
	/**
	 * object with methods to draw game entities on canvas
	 */
	GraphicsContext context;

	/**
	 * Used to store and update the state of the keyboard and mouse.
	 */
	public Input input;
	
	/**
	 * The root collection for all {@link Entity} objects in this game.
	 */
	public Group group;

    /**
     * timestamp for start of previous game loop; used to calculate {@link #deltaTime}
     */
	long previousTime;

	/**
	 * amount of time that has passed since the last iteration of the game loop
	 */
	public double deltaTime; 

    /**
     * Initialize objects used in this game.
     * This method should be overridden by the specific game extending this class.
     */
	public void create()
    {    }

	/**
	 * Update objects used in this game.
	 * Called 60 times per second when possible.
	 * This method should be overridden by the specific game extending this class. 
	 * @param dt amount of time that has passed since the last iteration of the game loop
	 */
    // override by extending class
	public void update(double dt)
    {    }
    
	/**
	 *  Initializes the window, Input and Group objects, 
	 *  and manages the life cycle of the game (initialization and game loop).
	 */
    public void start(Stage mainStage) 
    {
        // self-reference, needed for correct (game) context
        Game self = this;

        mainStage.setTitle("Game");
        mainStage.setResizable(false);

        Pane root = new Pane();
        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);
        mainStage.sizeToScene();

        this.canvas = new Canvas(Game.windowWidth, Game.windowHeight);
        this.context = this.canvas.getGraphicsContext2D();
        root.getChildren().add(this.canvas);

        this.input = new Input(mainScene);
        this.group = new Group();

        AnimationTimer gameloop = new AnimationTimer()
            {
                public void handle(long currentTime)
                {
                    self.deltaTime = (currentTime - self.previousTime) / 1000000000.0;
                    self.previousTime = currentTime;

                    // process input
                    self.input.update();

                    // update game state
                    self.group.act(self.deltaTime);
                    update(self.deltaTime);

                    // render objects to screen
                    context.setFill(Color.GRAY);
                    context.fillRect(0,0, 800,600);
                    self.group.draw(self.context);
                }

            };

        mainStage.show();

        this.create();
        this.previousTime = System.nanoTime();
        gameloop.start();
    }
}
