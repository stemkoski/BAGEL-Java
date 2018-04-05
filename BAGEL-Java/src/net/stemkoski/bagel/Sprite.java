package net.stemkoski.bagel;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

/**
 * Sprites represent game world entities: characters, environment, items, obstacles, etc.
 */
public class Sprite extends Entity
{
	
	// core properties
	
	/**
	 * x-coordinate of center of sprite
	 */
	public double x;
	
	/**
	 * y-coordinate of center of sprite
	 */
	public double y;
	
	/**
	 * image displayed when rendering this sprite
	 */
	Texture texture;
	
	/**
	 * width of sprite
	 */
	public double width;
	
	/**
	 * height of sprite
	 */
	public double height;
	
	/**
	 * determines if sprite will be visible
	 */
	public boolean visible;

	/**
	 * shape used for collision
	 */
	Rectangle boundary;

	// graphics properties
	
	/**
	 * angle of rotation of the texture
	 */
	public double angle;
	
	/**
	 * amount of transparency; value from 0.0 (fully transparent) to 1.0 (fully opaque)
	 */
	public double opacity; 
	
	/**
	 *  determines whether texture is reversed along the x direction
	 */
	public boolean mirrored;
	
	/**
	 *  determines whether texture is reversed along the y direction
	 */
	public boolean flipped;

	// physics
	
	/**
	 *  stores physics-related data (acceleration, maximum speed, deceleration)
	 */
	public Physics physics;

	// actions
	
	/**
	 *  list of {@link Action} objects attached to this sprite
	 */
	public ArrayList<Action> actionList;

	// animation (use with ActionFactory.animate)
	
	/**
	 *  stores animation data
	 */
	public Animation animation;

	/**
	 * initialize default values of sprite properties
	 */
	public Sprite()
	{  
		this.x = 0;
		this.y = 0;
		this.visible  = true;

		// collision
		this.boundary = new Rectangle();

		// graphics
		this.opacity  = 1.00;
		this.angle = 0;
		this.mirrored = false;
		this.flipped  = false;

		this.physics   = null;
		this.animation = null;
		
		// actions
		this.actionList = new ArrayList<Action>();
	}

	// basic methods

	/**
	 * Set the coordinates of the center of this sprite.
	 * @param x x-coordinate of center of sprite
	 * @param y y-coordinate of center of sprite
	 */
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Move this sprite by the specified amounts.
	 * @param deltaX amount to move sprite along x direction
	 * @param deltaY amount to move sprite along y direction
	 */
	public void moveBy(double deltaX, double deltaY)
	{
		this.x += deltaX;
		this.y += deltaY;
	}   

	/**
	 * Set the texture to be displayed when rendering this sprite.
	 * Also sets width and height of this sprite.
	 * @param tex texture to use for this sprite
	 */
	public void setTexture(Texture tex)
	{
		this.texture = tex;
		this.width   = tex.region.width;
		this.height  = tex.region.height;
	}

	/**
	 * Set size to use when for boundary and when drawing this sprite
	 * @param width width of sprite
	 * @param height height of sprite
	 */
	public void setSize(double width, double height)
	{
		this.width = width;
		this.height = height;
	}
	
	/**
     * Render this sprite to a canvas using specified parameters
     */
	@Override
	void draw(GraphicsContext context)
	{
		if ( !this.visible )
			return;

		double A = this.angle * Math.PI/180;
		double scaleX = 1;
		double scaleY = 1;
		if (this.mirrored)
			scaleX *= -1;
		if (this.flipped)
			scaleY *= -1;
		double cosA = Math.cos(A);
		double sinA = Math.sin(A);
		context.setTransform(scaleX*cosA, scaleX*sinA, -scaleY*sinA, scaleY*cosA, this.x, this.y);
		context.setGlobalAlpha(this.opacity);
		// image, 4 source parameters, 4 destination parameters
		context.drawImage(this.texture.image, 
				this.texture.region.left, this.texture.region.top, this.texture.region.width, this.texture.region.height,
				-this.width/2, -this.height/2, this.width, this.height);
	}

	// collision methods

	/**
	 * Get boundary shape for this sprite, adjusted according to current position.
	 * Angle of rotation has no effect on the boundary.
	 * @return boundary shape for this sprite
	 */
	Rectangle getBoundary()
	{
		this.boundary.setValues(this.x - this.width/2, this.y - this.height/2, this.width, this.height);
		return this.boundary;
	}

	/**
	 * Check if this sprite is overlapping another sprite.
	 * @param other sprite to check for overlap with
	 * @return true if this sprite overlaps other sprite
	 */
	public boolean isOverlapping(Sprite other)
	{
		return this.getBoundary().overlaps( other.getBoundary() );
	}

	/**
	 * Prevent this sprite from overlapping another sprite.
	 * @param other sprite to prevent overlap with
	 */
	public void preventOverlap(Sprite other)
	{
		if ( this.isOverlapping(other) )
		{
			Vector2 mtv = this.getBoundary().getMinTranslationVector( other.getBoundary() );
			this.moveBy(mtv.x, mtv.y);
		}
	}

	// angle methods

	/**
	 * Rotate sprite by the specified angle.
	 * @param deltaAngle the angle (in degrees) to rotate this sprite
	 */
	public void rotateBy(double deltaAngle)
	{
		this.angle += deltaAngle;
	}

	/** 
	 * Move sprite by the specified distance at the specified angle.
	 * @param distance the distance to move this sprite
	 * @param angleDegrees the angle (in degrees) along which to move this sprite
	 */
	public void moveAtAngle(double distance, double angleDegrees)
	{
		this.x += distance * Math.cos(angleDegrees * Math.PI/180);
		this.y += distance * Math.sin(angleDegrees * Math.PI/180);
	}

	/**
	 * Move sprite forward by the specified distance at current angle.
	 * @param distance the distance to move this sprite
	 */
	public void moveForward(double distance)
	{
		this.moveAtAngle(distance, this.angle);
	}

	// screen methods

	/**
	 * Keep sprite completely within screen bounds 
	 * by adjusting position if necessary.
	 * @param screenWidth width of screen
	 * @param screenHeight height of screen
	 */
	public void boundToScreen(double screenWidth, double screenHeight)
	{
		if (this.x - this.width/2 < 0)
			this.x = this.width/2;
		if (this.x + this.width/2 > screenWidth)
			this.x = screenWidth - this.width/2;
		if (this.y - this.height/2 < 0)
			this.y = this.height/2;
		if (this.y + this.height/2 > screenHeight)
			this.y = screenHeight - this.height/2;
	}

	/**
	 * If sprite moves completely beyond one edge of the screen,
	 * adjust position so that it reappears by opposite edge of the screen.
	 * @param screenWidth width of screen
	 * @param screenHeight height of screen
	 */
	public void wrapToScreen(double screenWidth, double screenHeight)
	{
		if (this.x + this.width/2 < 0)
			this.x = screenWidth + this.width/2;
		if (this.x - this.width/2 > screenWidth)
			this.x = -this.width/2; 
		if (this.y + this.height/2 < 0)
			this.y = screenHeight + this.height/2;
		if (this.y - this.height/2 > screenHeight)
			this.y = -this.height/2;
	}

	/**
	 * Check if sprite (boundary rectangle) remains on screen
	 * @param screenWidth width of screen
	 * @param screenHeight height of screen
	 * @return true, if part of sprite (boundary rectangle) remains on screen
	 */
	public boolean isOnScreen(double screenWidth, double screenHeight)
	{
		boolean offScreen = (this.x + this.width/2 < 0)
				|| (this.x - this.width/2 > screenWidth)
				|| (this.y + this.height/2 < 0)
				|| (this.y - this.height/2 > screenHeight);

		return (!offScreen);
	}

	// physics
	
	/**
     * Initialize physics object and corresponding values.
     * @param accValue acceleration value
     * @param maxSpeed maximum speed
     * @param decValue deceleration value
     */
	public void setPhysics(double accValue, double maxSpeed, double decValue)
	{
		this.physics = new Physics(accValue, maxSpeed, decValue);
	}

	/**
	 * Simulate this sprite bouncing off other sprite.
	 * Requires {@link #physics} object to be initialized.
	 * @param other Sprite acting as solid surface to bounce against
	 */
	public void bounceAgainst(Sprite other)
	{
		if ( this.isOverlapping(other) )
		{
			Vector2 mtv = this.getBoundary().getMinTranslationVector( other.getBoundary() );

			// prevent overlap
			this.moveBy(mtv.x, mtv.y);

			// assume surface perpendicular to displacement
			double surfaceAngle = mtv.getAngle() + 90; 
			// adjust velocity
			this.physics.bounceAgainst(surfaceAngle);
		}
	}

	// animation

	/**
	 * Set the animation to be used by this Sprite.
	 * Also sets texture, width, and height.
	 * @param anim Animation to be used by this Sprite
	 */
	public void setAnimation(Animation anim)
	{
		this.animation = anim;
		this.texture   = anim.currentTexture;
		this.width     = anim.currentTexture.region.width;
		this.height    = anim.currentTexture.region.height;
	}
	
	// actions

	/**
	 * Add an {@link Action} to this Sprite. Actions are automatically run.
	 * @param a action to add to this sprite
	 */
	public void addAction(Action a)
	{
		actionList.add(a);
	}

	/**
	 *  Run any {@link Action} objects added to this Sprite.
	 *  Actions are run in parallel (except for those created with {@link ActionFactory#sequence(Action...)}
	 *   and removed from the Sprite when they are finished.
	 *  @param deltaTime amount of time that has passed since the last iteration of the game loop
	 */
	@Override
	public void act(double deltaTime)
	{
		// update physics, position (based on velocity and acceleration)
		//   if it has been initialized for this sprite
		if ( this.physics != null )
		{
			// update position
			this.physics.positionVector.x = this.x;
			this.physics.positionVector.y = this.y;

			this.physics.update(deltaTime);

			this.x = this.physics.positionVector.x;
			this.y = this.physics.positionVector.y;
		}
		
		// update animation, current texture
	    //   if it has been initialized for this sprite
		if ( this.animation != null )
		{
			this.animation.update(deltaTime);
			this.texture = this.animation.currentTexture;
		}
		
		// update all actions (in parallel, by default)
		ArrayList<Action> actionListCopy = new ArrayList<Action>(this.actionList);
		for (Action a : actionListCopy)
		{
			boolean finished = a.apply(this, deltaTime);
			if (finished)
				this.actionList.remove(a);
		}
	}

}