package net.stemkoski.bagel;

/**
 * Extension of the {@link Physics} class,
 * useful in games with a side-view perspective and platform games in particular.
 * In this context, {@link Physics#accelerationValue}, {@link Physics#maximumSpeed},
 * and {@link Physics#decelerationValue} refer to horizontal movement;
 * this class includes additional variables to quantify jump strength, 
 * the force of gravity (which constantly accelerates an object downwards),
 * and terminal velocity (maximum vertical speed).
 *
 */
public class PlatformPhysics extends Physics
{
	/**
	 * initial vertical velocity when jumping
	 */
	public double jumpSpeed;

	/**
	 * constant downwards acceleration
	 */
	public double gravity;
	
	/**
	 * maximum vertical speed
	 */
	public double terminalVelocity;
	
	// suggested values: 450, 700, 1000
    /**
     * Initialize values used by physics simulation.
     * @param accValue walk/run acceleration value
     * @param maxHorizontalSpeed maximum horizontal speed
     * @param decValue walk/run deceleration value
     * @param jumpSpeed initial vertical velocity when jumping
     * @param gravity force of gravity; constant downwards acceleration
     * @param terminalVelocity maximum vertical speed
     */
    public PlatformPhysics(double accValue, double maxHorizontalSpeed, double decValue,
    		double jumpSpeed, double gravity, double terminalVelocity)
    {
        super(accValue, maxHorizontalSpeed, decValue);
        this.jumpSpeed        = jumpSpeed; 
        this.gravity          = gravity;
        this.terminalVelocity = terminalVelocity;
    }
    
    /**
     * Simulate jumping, using value stored in {@link #jumpSpeed}.
     */
    public void jump()
    {
        this.velocityVector.y = -this.jumpSpeed;
    }
    
    /**
     * Update the position of this object
     *  according to velocity, acceleration, and gravity.
     *  Deceleration is applied if no acceleration (other than gravity) is present.
     *  Horizontal speed is bounded by {@link Physics#maximumSpeed}
     *  and vertical speed is bounded by {@link PlatformPhysics#terminalVelocity}.
     * @param dt elapsed time (seconds) since previous iteration of game loop 
     *   (typically approximately 1/60 second)
     */
    @Override
    public void update(double dt)
    {
        // decrease walk speed (decelerate) when not accelerating
        if (this.accelerationVector.getLength() < 0.001)
        {
            double decelerationAmount = decelerationValue * dt;
            
            double walkDirection;
            
            if ( velocityVector.x > 0 )
                walkDirection = 1;
            else
                walkDirection = -1;

            double walkSpeed = Math.abs( velocityVector.x );

            walkSpeed -= decelerationAmount;

            if (walkSpeed < 0)
                walkSpeed = 0;

            velocityVector.x = walkSpeed * walkDirection;
        }
        
        // apply gravity
        this.accelerationVector.addValues(0, gravity);
        
        // apply acceleration
        this.velocityVector.addValues( 
            this.accelerationVector.x * dt, 
            this.accelerationVector.y * dt );

        if (this.velocityVector.x < -this.maximumSpeed)
            this.velocityVector.x = -this.maximumSpeed;
        if (this.velocityVector.x > this.maximumSpeed)
            this.velocityVector.x = this.maximumSpeed;
            
        if (this.velocityVector.y < -this.terminalVelocity)
            this.velocityVector.y = -this.terminalVelocity;
        if (this.velocityVector.y > this.terminalVelocity)
            this.velocityVector.y = this.terminalVelocity;

        // update position according to value stored in velocity vector
        this.positionVector.addValues(
            this.velocityVector.x * dt, 
            this.velocityVector.y * dt );

        // reset acceleration
        this.accelerationVector.setValues(0,0);
    }
}
