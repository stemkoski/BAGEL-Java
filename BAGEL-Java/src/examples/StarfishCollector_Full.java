package examples;

import javafx.scene.paint.Color;
import net.stemkoski.bagel.*;

public class StarfishCollector_Full extends Game 
{
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

	Sprite turtle;
	Group  starfishGroup;
	Sprite winMessage;

	Label  starfishLabel;
	Sprite rock;
	Audio  dropSound;
	Audio  waves;

	@Override
	public void create()
	{
		Sprite background = new Sprite();
		background.setPosition(400,300); 
		background.setTexture( Texture.load("assets/starfish-collector/water.png") );
		this.group.addEntity(background);

		this.rock = new Sprite();
        this.rock.setPosition(400,300);
        this.rock.setTexture( Texture.load("assets/starfish-collector/rock.png") );
        this.group.addEntity(this.rock);
        
		this.starfishGroup = new Group();
		this.group.addEntity(this.starfishGroup);
		Texture starfishTexture = Texture.load("assets/starfish-collector/starfish.png");
		for (int n = 0; n < 10; n++)
		{
			Sprite starfish = new Sprite();
			double x = Math.random() * 800;
			double y = Math.random() * 600;
			starfish.setPosition(x,y); 
			starfish.setTexture( starfishTexture );
			starfish.boundToScreen(800, 600);
			
			this.starfishGroup.addEntity(starfish);

			double rotateAmount = Math.random() * 30 + 30;

			starfish.addAction( 
					ActionFactory.forever(
							ActionFactory.rotateBy(rotateAmount, 1) 
							)
					);
		}

		this.turtle = new Sprite(); 
		this.turtle.setPosition(100,300);
		this.turtle.setTexture( Texture.load("assets/starfish-collector/turtle.png") );
		this.turtle.addAction( ActionFactory.boundToScreen(800, 600));
		this.turtle.setPhysics(200, 100, 200);
		this.group.addEntity(this.turtle);

		Animation fishAnimation = 
				Animation.load("assets/starfish-collector/fish.png", 8,1, 0.15, true);
		Sprite fish = new Sprite();
		fish.setPosition(750, 550);
		fish.setAnimation(fishAnimation);
		fish.angle = -90;
		fish.addAction(
				ActionFactory.forever(
						ActionFactory.sequence(
								ActionFactory.moveBy(0,-500,5),
								ActionFactory.rotateBy(-180, 3),
								ActionFactory.moveBy(0,500,5),
								ActionFactory.rotateBy(180,3)
								)
						)
				);
		this.group.addEntity(fish);

		Sprite foreground = new Sprite();
		foreground.setPosition(400,300); 
		foreground.setTexture( Texture.load("assets/starfish-collector/water.jpg") );
		foreground.opacity = 0.25;
		this.group.addEntity(foreground);

		this.starfishLabel = new Label();
		this.starfishLabel.loadFontFromFile("assets/starfish-collector/OpenSans.ttf", 48);
		this.starfishLabel.fontColor = Color.WHITE;
		this.starfishLabel.text = "Starfish Left: 10";
		this.starfishLabel.setPosition(400, 50);
		this.starfishLabel.alignment = "CENTER";
		this.starfishLabel.borderDraw = true;
		this.starfishLabel.borderSize = 2;
		this.starfishLabel.borderColor = Color.NAVY;
		this.group.addEntity(this.starfishLabel);

		this.winMessage = new Sprite(); 
		this.winMessage.setPosition(400,300);
		this.winMessage.setTexture( Texture.load("assets/starfish-collector/win-message.png") );
		this.winMessage.visible = false;
		this.group.addEntity(this.winMessage);

		this.dropSound  = Audio.loadSound("assets/starfish-collector/Water-Drop.wav");
		this.waves = Audio.loadMusic("assets/starfish-collector/Ocean-Waves.mp3");
		this.waves.setLoop(true);
		this.waves.setVolume(0.50);
		this.waves.play();
	}

	@Override
	public void update(double dt)
	{		
		if ( this.input.isKeyPressed("LEFT") )
			turtle.rotateBy(-2);
		if ( this.input.isKeyPressed("RIGHT") )
			turtle.rotateBy(2);
		if ( this.input.isKeyPressed("UP") )
			turtle.physics.accelerateAtAngle( turtle.angle );

		this.turtle.preventOverlap(this.rock);
		
		for ( Entity starfishEntity : this.starfishGroup.list() )
		{
			Sprite starfish = (Sprite)starfishEntity;
			if ( this.turtle.isOverlapping(starfish) && starfish.opacity == 1.00)
			{
				starfish.addAction(  
						ActionFactory.sequence(
								ActionFactory.fadeOut(1),
								ActionFactory.remove()
								)
						);
				this.dropSound.play();
			}
		}

		int count = this.starfishGroup.count;

		this.starfishLabel.text = "Starfish left: " + count;

		if (count == 0)
			winMessage.visible = true;
	}
}

