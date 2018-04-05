package examples;

import net.stemkoski.bagel.*;

public class MapExplorer extends Game
{
    // need to launch from here so that @Override methods activate.
    public static void main(String[] args) 
    {
        try
        {
            Game.windowWidth  = 64 * 12;
            Game.windowHeight = 64 * 10;
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
    Sprite turtle;

    @Override
    public void create()
    {
        Sprite ocean = new Sprite();
        ocean.setPosition(400,300); 
        ocean.setTexture( Texture.load("assets/map-explorer/water.png") );
        this.group.addEntity(ocean);

        this.map = new TileMap(10,12, 64,64);
        String[] mapData =
            {   "AAAAAAAAAAAA",
                "AT_____C___A",
                "A_BBB______A",
                "A__BBB__C__A",
                "A__________A",
                "A_C__BB__C_A",
                "A____BBB___A",
                "A__C__B___CA",
                "A_______C__A",
                "AAAAAAAAAAAA"  };
        String[] mapDataSymbolArray  = {"A", "B", "C"};
        int[]    tileImageIndexArray = {4, 14, 2};
        this.map.loadMapData( mapData, mapDataSymbolArray, tileImageIndexArray ); 
        this.map.loadTilesetImage("assets/map-explorer/tileset.png");

        this.group.addEntity(map);
        
        this.turtle = new Sprite(); 
        this.turtle.setTexture( Texture.load("assets/map-explorer/turtle.png") );
        Vector2 v = map.getSymbolPositionList("T").get(0);
        this.turtle.setPosition( v.x, v.y );
        this.group.addEntity(this.turtle);

        this.turtle.setPhysics(512,64,512);
    }

    @Override
    public void update(double dt)
    {
        if ( this.input.isKeyPressed("UP") )
            this.turtle.physics.accelerateAtAngle(270);
        if ( this.input.isKeyPressed("DOWN") )
            this.turtle.physics.accelerateAtAngle(90);
        if ( this.input.isKeyPressed("RIGHT") )
            this.turtle.physics.accelerateAtAngle(0);
        if ( this.input.isKeyPressed("LEFT") )
            this.turtle.physics.accelerateAtAngle(180);

        this.map.preventSpriteOverlap( this.turtle );
        
        double motionAngle = this.turtle.physics.getMotionAngle(); // -180 to +180
        if ( this.turtle.physics.getSpeed() > 1)
        {
            if (-45 <= motionAngle && motionAngle <= 45)
                this.turtle.angle = 0;
            else if (45 <= motionAngle && motionAngle <= 135)
                this.turtle.angle = 90;
            else if (-135 <= motionAngle && motionAngle <= -45)
                this.turtle.angle = 270;
            else 
                this.turtle.angle = 180;
        }
    }
}
