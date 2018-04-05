package examples;

import javafx.scene.paint.Color;
import net.stemkoski.bagel.*;

public class RectangleDestroyer extends Game
{
	 // need to launch from here so that @Override methods activate.
    public static void main(String[] args) 
    {
        try
        {
            launch();
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }

    // declare class variables

    Sprite paddle;
    Sprite ball;
    Group  wallGroup;
    Group  brickGroup;

    Label messageLabel;
    int score;
    Label scoreLabel;

    @Override
    public void create()
    {
        Sprite background = new Sprite();
        background.setTexture( Texture.load("assets/rectangle-destroyer/background.png") );
        background.setPosition(400,300);
        this.group.addEntity(background);

        this.paddle = new Sprite();
        this.paddle.setTexture( Texture.load("assets/rectangle-destroyer/paddle.png") );
        this.paddle.setPosition(400, 550);
        this.group.addEntity( this.paddle );

        this.ball = new Sprite();
        this.ball.setTexture( Texture.load("assets/rectangle-destroyer/ball.png") );
        this.ball.setPosition(400, 525);
        this.ball.setPhysics(0,1000,0);
        this.group.addEntity( this.ball );

        this.wallGroup = new Group();
        this.group.addEntity( this.wallGroup );
        Texture wallSideTexture = Texture.load("assets/rectangle-destroyer/wall-side.png"); // 20x600
        Texture wallTopTexture = Texture.load("assets/rectangle-destroyer/wall-top.png");   // 800x60
        Sprite leftWall = new Sprite();
        leftWall.setTexture(wallSideTexture);
        leftWall.setPosition(10,300);
        Sprite rightWall = new Sprite();
        rightWall.setTexture(wallSideTexture);
        rightWall.setPosition(790,300);
        Sprite topWall = new Sprite();
        topWall.setTexture(wallTopTexture);
        topWall.setPosition(400,30);
        this.wallGroup.addEntity(leftWall);
        this.wallGroup.addEntity(rightWall);
        this.wallGroup.addEntity(topWall);

        this.brickGroup = new Group();
        this.group.addEntity( this.brickGroup );
        Texture brickTexture = Texture.load("assets/rectangle-destroyer/brick.png");

        for (int col = 0; col < 11; col++)
        {
            for (int row = 0; row < 8; row++)
            {
                Sprite brick = new Sprite();
                brick.setTexture( brickTexture );

                brick.setPosition( 80 + 64 * col, 120 + row * 32 );
                this.brickGroup.addEntity(brick);
            }
        }

        this.messageLabel = new Label();
        this.messageLabel.loadFontFromFile("assets/starfish-collector/OpenSans.ttf", 48);
        this.messageLabel.fontColor = Color.GRAY;
        this.messageLabel.text = "click to start";
        this.messageLabel.setPosition(400, 500);
        this.messageLabel.alignment = "CENTER";
        this.messageLabel.borderDraw = true;
        this.messageLabel.borderSize = 2;
        this.messageLabel.borderColor = Color.BLACK;
        this.group.addEntity(this.messageLabel);

        this.score = 0;
        this.scoreLabel = new Label();
        this.scoreLabel.loadFontFromFile("assets/starfish-collector/OpenSans.ttf", 36);
        this.scoreLabel.fontColor = Color.YELLOW;
        this.scoreLabel.text = "Score: " + score;
        this.scoreLabel.setPosition(400, 40);
        this.scoreLabel.alignment = "CENTER";
        this.scoreLabel.borderDraw = true;
        this.scoreLabel.borderSize = 2;
        this.scoreLabel.borderColor = Color.BLACK;
        this.group.addEntity(this.scoreLabel);
    }

    @Override
    public void update(double dt)
    {
        Vector2 position = this.input.getMousePosition();
        this.paddle.x = position.x;
        this.paddle.boundToScreen(800, 600);

        if (this.messageLabel.visible && this.messageLabel.text.equals("click to start") )
        {
            this.ball.x = this.paddle.x;

            if (this.input.isMouseButtonDown())
            {
                this.ball.physics.setSpeed(250);
                this.ball.physics.setMotionAngle(270);
                this.messageLabel.visible = false;
            }
        }

        for (Entity wallEntity : this.wallGroup.list())
        {
            Sprite wall = (Sprite)wallEntity;
            if (this.ball.isOverlapping(wall))
            {
                this.ball.bounceAgainst(wall);
            }
        }

        if ( this.ball.isOverlapping(this.paddle) )
        {
            this.ball.preventOverlap( this.paddle );
            double paddlePercent = percent( ball.x, paddle.x - paddle.width/2, paddle.x + paddle.width/2);
            double reboundAngle  = lerp( paddlePercent, 225, 315 ) + randomBetween(-2,2);
            this.ball.physics.setMotionAngle( reboundAngle );
            this.ball.physics.setSpeed( this.ball.physics.getSpeed() + 2 );
            this.score += 1;
        } 

        for (Entity brickEntity : this.brickGroup.list())
        {
            Sprite brick = (Sprite)brickEntity;
            if (this.ball.isOverlapping(brick))
            {
                this.ball.bounceAgainst(brick);
                brick.remove();
                score += 100;
            }
        }

        this.scoreLabel.text = "Score: " + score;

        if ( !this.messageLabel.visible && this.ball.y > 700 ) // && this.ballReserve > 0
        {
            this.ball.physics.setSpeed(0);
            this.ball.setPosition( this.paddle.x, this.paddle.y - 20 );
            this.messageLabel.visible = true;
            this.messageLabel.text = "click to start";
        }
        
        if ( this.brickGroup.count == 0 )
        {
            this.messageLabel.visible = true;
            this.messageLabel.setPosition(400,300);
            this.messageLabel.fontColor = Color.GREEN;
            this.messageLabel.text = "You Win!";
        }
    }

    double lerp(double percent, double min, double max)
    {  return min + percent * (max - min);  }

    double percent(double value, double min, double max)
    {  return (value - min) / (max - min);  }

    double randomBetween(double min, double max)
    {  return min + Math.random() * (max - min);  }
}
