package examples;

import net.stemkoski.bagel.*;

public class StarfishCollector_Basic extends Game 
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

	@Override
	public void create()
	{
		Sprite background = new Sprite();
		background.setPosition(400,300); 
		background.setTexture( Texture.load("assets/starfish-collector/water.png") );
		this.group.addEntity(background);

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
			this.starfishGroup.addEntity(starfish);
		}

		this.turtle = new Sprite(); 
		this.turtle.setPosition(100,100);
		this.turtle.setTexture( Texture.load("assets/starfish-collector/turtle-down.png") );
		this.group.addEntity(this.turtle);
		
		this.winMessage = new Sprite(); 
		this.winMessage.setPosition(400,300);
		this.winMessage.setTexture( Texture.load("assets/starfish-collector/win-message.png") );
		this.winMessage.visible = false;
		this.group.addEntity(this.winMessage);
		
	}

	@Override
	public void update(double dt)
	{
		if ( this.input.isKeyPressed("RIGHT") )
            this.turtle.moveBy(2,0);
        if ( this.input.isKeyPressed("LEFT") )
        	this.turtle.moveBy(-2,0);
        if ( this.input.isKeyPressed("DOWN") )
        	this.turtle.moveBy(0,2);
        if ( this.input.isKeyPressed("UP") )
        	this.turtle.moveBy(0,-2);
        
        for ( Entity starfishEntity : this.starfishGroup.list() )
        {
        	Sprite starfish = (Sprite)starfishEntity;
            if ( this.turtle.isOverlapping(starfish) )
                starfish.remove();
        }

        if (this.starfishGroup.count == 0)
        	winMessage.visible = true;
	}
}
