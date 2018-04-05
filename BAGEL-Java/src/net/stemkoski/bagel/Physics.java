package net.stemkoski.bagel;

/** 
 *  Used to simulate change in position of an object based on velocity and acceleration;
 *  useful in games with a top-down view perspective.
 *  (For side-view perspective games, see {@link PlatformPhysics}.)
 */
public class Physics
{
    /**
     * Position of object.
     */
    public Vector2 positionVector;
    
    /**
     * Velocity (rate of change of position) of object.
     */
    public Vector2 velocityVector;
    
    /**
     * Acceleration (rate of chance of velocity) of object.
     */
    public Vector2 accelerationVector; 
    
    /**
     * Constant amount of acceleration, used by {@link #accelerateAtAngle(double)}.
     */
    public double accelerationValue;
    
    /**
     * Maximum speed possible for object.
     */
    public double maximumSpeed;
    
    /**
     * Rate of speed reduction to apply when object is not accelerating.
     */
    public double decelerationValue;
    
    /**
     * Initialize values used by physics simulation.
     * For objects traveling at constant speed,
     * set acceleration and deceleration values to 0.
     * @param accValue acceleration value
     * @param maxSpeed maximum speed
     * @param decValue deceleration value
     */
    public Physics(double accValue, double maxSpeed, double decValue)
    {
        this.positionVector     = new Vector2();
        this.velocityVector     = new Vector2();
        this.accelerationVector = new Vector2();
        this.accelerationValue  = accValue;
        this.maximumSpeed       = maxSpeed;
        this.decelerationValue  = decValue;
    }

    /**
     * Calculate speed of object.
     * @return speed of object
     */
    public double getSpeed() 
    { 
        return this.velocityVector.getLength(); 
    }

    /**
     * Set speed of object.
     * If acceleration and deceleration values are 0, the speed will remain constant.
     * @param speed speed of object
     */
    public void setSpeed(double speed) 
    { 
        this.velocityVector.setLength(speed); 
    }

    /**
     * Calculate the angle of motion (in degrees)
     * as measured from the x-axis (the vector (1,0)). 
     * If the speed is 0, this method returns 0.
     * Return values are in the range from -180 to +180.
     * @return angle of motion of object
     */
    public double getMotionAngle()
    {
        return this.velocityVector.getAngle();
    }

    /**
     * Set the angle of motion of this object.
     * If the speed is 0, this method has no effect.
     * @param angleDegrees angle of motion of object
     */
    public void setMotionAngle(double angleDegrees)
    {
        this.velocityVector.setAngle(angleDegrees);
    }

    /**
     * Change angle of motion assuming a collision with a flat surface
     * inclined at an angle of surfaceAngleDegrees.
     * @param surfaceAngleDegrees inclination of surface this object has collided with
     */
    public void bounceAgainst(double surfaceAngleDegrees)
    {
        double relativeCollisionAngle = this.getMotionAngle() - surfaceAngleDegrees;
        double relativeBounceAngle = -relativeCollisionAngle;
        this.setMotionAngle( relativeBounceAngle + surfaceAngleDegrees );
    }
    
    /**
     * Accelerate this object in the direction angleDegrees
     *  by the amount specified by {@link #accelerationValue}.
     * @param angleDegrees direction of acceleration
     */
    public void accelerateAtAngle(double angleDegrees)
    {
        Vector2 v = new Vector2();
        v.setLength(this.accelerationValue);
        v.setAngle(angleDegrees);
        this.accelerationVector.addVector( v );
    }

    /**
     * Update the position of this object
     *  according to velocity and acceleration.
     *  Deceleration is applied if no acceleration is present.
     * @param dt elapsed time (seconds) since previous iteration of game loop 
     *   (typically approximately 1/60 second)
     */
    public void update(double dt)
    {
        // apply acceleration
        this.velocityVector.addValues( 
            this.accelerationVector.x * dt, 
            this.accelerationVector.y * dt );

        double speed = this.getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (this.accelerationVector.getLength() < 0.001)
            speed -= this.decelerationValue * dt;

        // keep speed within set bounds
        if (speed < 0)
            speed = 0;
        if (speed > this.maximumSpeed)
            speed = this.maximumSpeed;

        // update velocity
        this.setSpeed(speed);

        // update position according to value stored in velocity vector
        this.positionVector.addValues(
            this.velocityVector.x * dt, 
            this.velocityVector.y * dt );

        // reset acceleration
        this.accelerationVector.setValues(0,0);
    }
}
