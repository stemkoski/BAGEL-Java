package examples;

import net.stemkoski.bagel.*;

public class SpaceRocks extends Game
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
    Sprite spaceship;
    Group rockGroup;
    Group laserGroup;
    Texture laserTexture;
    Animation explosionAnimation;
    Sprite messageWin;

    @Override
    public void create()
    {
        Sprite space = new Sprite();
        space.setTexture( Texture.load("assets/space-rocks/space.png") );
        space.setPosition(400,300);
        group.addEntity(space);

        spaceship = new Sprite();
        spaceship.setTexture( Texture.load("assets/space-rocks/spaceship.png") );
        spaceship.setPosition(100,300);
        spaceship.setPhysics(100, 200, 10);
        spaceship.addAction( ActionFactory.wrapToScreen(800,600) );
        group.addEntity(spaceship);

        rockGroup = new Group();
        group.addEntity(rockGroup);
        laserGroup = new Group();
        group.addEntity(laserGroup);

        // avoid reloading texture
        Texture rockTexture = Texture.load("assets/space-rocks/rock.png");
        for (int n = 0; n < 6; n++)
        {
            Sprite rock = new Sprite();
            double x = Math.random() * 400 + 300;
            double y = Math.random() * 600;
            rock.setPosition(x,y);
            rock.setTexture( rockTexture );
            rock.setPhysics(0,80,0);
            rock.physics.setSpeed( 80 );
            double angle = Math.random() * 360;
            rock.angle = angle;
            rock.physics.setMotionAngle( angle );
            rock.addAction( ActionFactory.wrapToScreen(800,600) );
            rockGroup.addEntity(rock);
        }

        laserTexture = Texture.load("assets/space-rocks/laser.png");
        explosionAnimation = Animation.load( "assets/space-rocks/explosion.png", 6, 6, 0.03, false );
        
           
        messageWin = new Sprite();
        messageWin.setPosition(400,300);
        messageWin.setTexture( Texture.load("assets/space-rocks/message-win.png") );
        messageWin.opacity = 0;
        messageWin.visible = false;
        group.addEntity(messageWin);
    }

    @Override
    public void update(double dt)
    {
        if ( input.isKeyPressed("LEFT") )
            spaceship.rotateBy(-2);
        if ( input.isKeyPressed("RIGHT") )
            spaceship.rotateBy(2);
        if ( input.isKeyPressed("UP") )
            spaceship.physics.accelerateAtAngle(spaceship.angle);

        if ( input.isKeyDown("SPACE") )
        {
            Sprite laser = new Sprite();
            laser.setPosition( spaceship.x, spaceship.y );
            laser.setPhysics(0, 400, 0);
            laser.physics.setSpeed( 400 );
            laser.physics.setMotionAngle( spaceship.angle );
            laser.setTexture( laserTexture );
            laser.addAction( ActionFactory.wrapToScreen(800,600) );
            laser.addAction(
                ActionFactory.sequence(
                    ActionFactory.delay(1),
                    ActionFactory.fadeOut(0.5),
                    ActionFactory.remove()
                )
            );
            laserGroup.addEntity(laser);
        }

        for (Entity laserObj : laserGroup.list())
        {
            Sprite laser = (Sprite)laserObj;
            for (Entity rockObj : rockGroup.list())
            {
                Sprite rock = (Sprite)rockObj;
                if ( laser.isOverlapping(rock) )
                {
                    laser.remove();
                    rock.remove();
                    
                    Sprite explosion = new Sprite();
                    explosion.setAnimation( explosionAnimation.clone() );
                    explosion.setPosition( rock.x, rock.y );
                    // remove after animation complete
                    explosion.addAction( 
                        ActionFactory.sequence(
                            ActionFactory.isAnimationFinished(),
                            ActionFactory.remove()
                        )
                    );
                    group.addEntity(explosion);
                    
                }

            }
        }

        if ( rockGroup.count == 0 && !messageWin.visible )
        {
            messageWin.visible = true;
            messageWin.addAction( ActionFactory.fadeIn(2) );
        }
    }
}
