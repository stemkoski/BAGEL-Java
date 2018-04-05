package net.stemkoski.bagel;


/**
 * A two-dimensional vector (x,y), particularly useful for 
 * representing velocity and acceleration (see {@link Physics}), 
 * and minimum translation vectors for collision resolution 
 * (see {@link Rectangle}, {@link TileMap}).
 */
public class Vector2 implements Comparable<Vector2>
{
    /**
     *  x-coordinate of the vector
     */
	public double x;
    
    /**
     *  y-coordinate of the vector
     */
    public double y;

    /**
     * Initializes vector coordinates to (0,0).
     */
    public Vector2()
    {
        this.setValues(0,0);
    }

    /**
     * Initializes vector coordinates to (x,y).
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Vector2(double x, double y)
    {
        this.setValues(x,y);
    }

    /**
     * Set the values of the x- and y- coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setValues(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Add the values of the coordinates of other vector 
     * to the coordinates of this vector.
     * @param other vector to be added to this vector
     */
    public void addVector(Vector2 other)
    {
        this.x += other.x;
        this.y += other.y;
    }

    /**
     * Add values to the coordinates of this vector.
     * @param dx value to add to the x-coordinate
     * @param dy value to add to the y-coordinate
     */
    public void addValues(double dx, double dy)
    {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Multiple the coordinates of this vector by 
     * a given value.
     * @param scalar the value to multiply the coordinates of this vector by
     */
    public void multiply(double scalar)
    {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Calculate the length of this vector
     * @return the length of this vector
     */
    public double getLength()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Calculate the angle (in degrees) between this vector 
     * and the x-axis (the vector (1,0)). If this vector is 
     * the zero vector, returns 0. Return values are in the 
     * range from -180 to +180.
     * @return the angle between this vector and the x-axis
     */
    public double getAngle()
    {
        // range: -180 to 180
        if (this.getLength() == 0)
            return 0;
        else
            return Math.atan2(this.y, this.x) * 180/Math.PI;
    }

    /**
     * Changes the length of this vector to length while 
     * preserving the angle of this vector.
     * @param length the new length of this vector
     */
    public void setLength(double length)
    {
        double angleDegrees = this.getAngle();
        this.x = length * Math.cos(angleDegrees * Math.PI/180);
        this.y = length * Math.sin(angleDegrees * Math.PI/180);
    }   

    /**
     * Changes the angle of this vector to angleDegrees 
     * while preserving the length of this vector.
     * @param angleDegrees the new angle (in degrees) between this vector and the x-axis
     */
    public void setAngle(double angleDegrees)
    {
        double length = this.getLength();
        this.x = length * Math.cos(angleDegrees * Math.PI/180);
        this.y = length * Math.sin(angleDegrees * Math.PI/180);
    }

    /**
     * Required method from Comparable interface; used to sort collections of Vector2 objects by length, particularly for use in determining the minimum (shortest) translation vector for use in resolving collisions with solid objects.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param other the vector being compared against this vector
     * @return +1 if this vector is larger than other vector, -1 if other vector is larger than this vector, 0 if vectors have same length
     */
    public int compareTo(Vector2 other)
    {
        if ( this.getLength() < other.getLength() )
            return -1;
        else if (this.getLength() > other.getLength() )
            return 1;
        else
            return 0;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String xStr = String.format("%3.5f", this.x);
        String yStr = String.format("%3.5f", this.y);
        
        return "[" + xStr + " , " + yStr + "]";
    }
}