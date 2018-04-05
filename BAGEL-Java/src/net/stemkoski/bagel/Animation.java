package net.stemkoski.bagel;


import java.io.File;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * A sequence of {@link Texture}s displayed in rapid succession 
 * to create the appearance of movement.
 * Animation objects are created using the {@link #load(String, int, int, double, boolean)} method.
 * Multiple instances of a {@link Sprite} should <b>not</b> share a single Animation reference,
 * as each Sprite will require its own {@link #elapsedTime} field; 
 * in this case, use the {@link #clone()} method to create a copy of the original Animation.
 */
public class Animation
{
	/**
	 *  The texture that should be displayed by this animation,
	 *  according to the amount of time that has elapsed ({@link #elapsedTime}).
	 */
	public Texture currentTexture;
	
	/**
	 *  Determines if this animation should stop updating.
	 */
	public boolean paused;
	
	/**
	 *  List of the textures that will be displayed in sequence by this animation.
	 */
	ArrayList<Texture> textureList;
    
	/**
	 *  The amount of time each texture should be displayed by this animation.
	 */
	double frameDuration;
    
	/**
	 *  Determines if this animation should repeat after displaying the last texture in the list.
	 */
	boolean loop;
    
	/**
	 *  The amount of time that has elapsed since this animation began playing.
	 */
	double elapsedTime;
    
	/**
	 *  The total amount of time required to display all textures in this animation.
	 */
	double totalDuration;

    /**
     * Create an empty animation. Useful when reusing a previously loaded image;
	 *  used by {@link #clone()}.
     */
    Animation()
    {
        textureList = new ArrayList<Texture>();
        elapsedTime = 0;
        paused = false;
    }

    /**
     *  Update {@link #elapsedTime} and {@link #currentTexture}
     *  based on the amount of time that has passed since last update. 
     *  @param dt amount of time that has passed since the last iteration of the game loop
     */
    public void update(double dt)
    {
        if (paused)
            return;

        elapsedTime += dt;

        if (loop && this.elapsedTime > this.totalDuration)
            this.elapsedTime -= this.totalDuration;

        int textureIndex = (int)Math.floor( this.elapsedTime / this.frameDuration );
        if ( textureIndex >= this.textureList.size() )
            textureIndex = this.textureList.size() - 1;
        this.currentTexture = this.textureList.get(textureIndex);
    }

    /**
     * Determines if this animation has finished playing.
     * Calculated by elapsed time, number of textures, frame duration, and loop.
     * @return true if this animation has finished playing
     */
    public boolean isFinished()
    {
        return (this.elapsedTime >= this.textureList.size() * this.frameDuration) && !this.loop;
    }

    // Note: images/list are a shallow copy; deep copy not required.
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Animation clone()
    {
        Animation anim      = new Animation();
        anim.textureList    = this.textureList;
        anim.frameDuration  = this.frameDuration;
        anim.loop           = this.loop;
        anim.totalDuration  = this.totalDuration;
        anim.currentTexture = this.textureList.get(0);
        return anim;
    }

    /**
     * Create an animation object from a sprite sheet.
     * @param imageFileName name of image file
     * @param rows number of rows of individual images in file 
     * @param cols number of columns of individual images in file
     * @param frameDuration amount of time to display each individual image
     * @param loop whether this animation should repeat after last image is displayed
     * @return an Animation created according to the given parameters
     */
    public static Animation load(String imageFileName, int rows, int cols, double frameDuration, boolean loop)
    {
        Animation anim = new Animation();
        String fileName = new File(imageFileName).toURI().toString();
        Image image = new Image(fileName);
        double frameWidth  = image.getWidth() / cols;
        double frameHeight = image.getHeight() / rows;
        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < cols; x++)
            {
                Texture texture = new Texture();
                texture.image = image;
                texture.region = new Rectangle(x*frameWidth, y*frameHeight, frameWidth, frameHeight);
                anim.textureList.add( texture );
            }
        }
        anim.frameDuration = frameDuration;
        anim.loop = loop;
        anim.totalDuration = anim.frameDuration * anim.textureList.size();
        anim.currentTexture = anim.textureList.get(0);

        return anim;
    }

}
