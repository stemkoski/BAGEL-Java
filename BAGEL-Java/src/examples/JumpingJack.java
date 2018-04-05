package examples;

import net.stemkoski.bagel.*;

public class JumpingJack extends Game
{
    // need to launch from here so that @Override methods are used.
    public static void main(String[] args) 
    {
        try
        {
            Game.windowWidth  = 64 * 15; // 960
            Game.windowHeight = 64 * 10; // 640
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

    TileMap map;
    Sprite jack;
    Animation animWalk;
    Animation animStand;
    Animation animJump;

    @Override
    public void create()
    {
        Sprite background = new Sprite();
        background.setPosition(480,320); 
        background.setTexture( Texture.load("assets/jumping-jack/background.png") );
        this.group.addEntity(background);

        this.map = new TileMap(10,15, 64,64);
        String[] mapData =
            {   "_______________",
                "_______________",
                "_______________",
                "C_____________A",
                "D_____________D",
                "D__B__AC______D",
                "D________ABC__D",
                "D_J_____AD___AD",
                "D____________DD",
                "DBBBBBBBBBBBBDD"  };
        String[] mapDataSymbolArray  = {"A", "B", "C", "D"};
        int[]    tileImageIndexArray = {1,2,3,0};
        this.map.loadMapData( mapData, mapDataSymbolArray, tileImageIndexArray ); 
        this.map.loadTilesetImage("assets/jumping-jack/tileset.png");
        this.group.addEntity(map);

        this.jack = new Sprite(); 
        Vector2 v = map.getSymbolPositionList("J").get(0);
        this.jack.setPosition( v.x, v.y );
        this.jack.addAction( ActionFactory.boundToScreen(960,640) );

        this.jack.physics = new PlatformPhysics(512,128,512, 450,700,1000);

        this.animWalk  = Animation.load( "assets/jumping-jack/walk.png",  1, 4, 0.15, true );
        this.animStand = Animation.load( "assets/jumping-jack/stand.png", 1, 1, 1.00, true );
        this.animJump  = Animation.load( "assets/jumping-jack/jump.png",  1, 1, 1.00, true );
        this.jack.setAnimation(animWalk);

        this.group.addEntity(this.jack);
    }

    @Override
    public void update(double dt)
    {
        // walk
        if ( this.input.isKeyPressed("RIGHT") )
            this.jack.physics.accelerateAtAngle(0);
        if ( this.input.isKeyPressed("LEFT") )
            this.jack.physics.accelerateAtAngle(180);

        // collision detection
        this.map.preventSpriteOverlap( this.jack );

        // check if on ground
        this.jack.moveBy(0,2);
        boolean onGround = map.checkSpriteOverlap(this.jack);
        this.jack.moveBy(0,-2);

        // jump
        if ( this.input.isKeyDown("SPACE") && onGround )
        {
            ((PlatformPhysics)(this.jack.physics)).jump();
        }

        // manage animations
        if (onGround)
        {
            if ( this.jack.physics.velocityVector.x == 0 )
                this.jack.setAnimation(this.animStand);
            else
                this.jack.setAnimation(this.animWalk);
        }
        else
        {
            this.jack.setAnimation(this.animJump);
        }

        if ( this.jack.physics.velocityVector.x > 0 ) // face right
            this.jack.mirrored = false;
        if ( this.jack.physics.velocityVector.x < 0 ) // face left
            this.jack.mirrored = true;
    }
}
