package net.stemkoski.bagel;

import java.util.Arrays;

/**
 *  A rectangle shape, defined by its position and size, 
 *  particularly useful in collision detection 
 *  (see {@link Sprite}, {@link TileMap})
 *  and for specifying area of an image to be drawn 
 *  (see {@link Texture}). 
 *  Rectangles with width or height equal to 0 can be used to represent edges
 *  (see {@link Tile#edgeLeft}, etc.)
 *
 */
public class Rectangle
{	
	/**
	 * x-coordinate of left edge of rectangle
	 */
	double left;

	/**
	 * y-coordinate of top edge of rectangle
	 */
	double top;

	/**
	 * width of rectangle
	 */
	double width;

	/**
	 * height of rectangle
	 */
	double height;

	/**
	 * x-coordinate of right edge of rectangle
	 */
	double right;

	/**
	 * y-coordinate of bottom edge of rectangle
	 */
	double bottom;

	/**
	 * Initialize rectangle with all values set to 0.
	 */
	Rectangle()
	{
		this.setValues(0,0,0,0);
	}

	/**
	 * Initialize rectangle data from coordinates of top-left corner and size.
	 * @param left x-coordinate of top-left corner (left edge) of rectangle
	 * @param top y-coordinate of top-left corner (top edge) of rectangle
	 * @param width width of rectangle
	 * @param height height of rectangle
	 */
	Rectangle(double left, double top, double width, double height)
	{
		this.setValues(left, top, width, height);
	}

	/**
	 * Update rectangle data from coordinates of top-left corner and size.
	 * Useful for game entities that move.
	 * @param left x-coordinate of top-left corner (left edge) of rectangle
	 * @param top y-coordinate of top-left corner (top edge) of rectangle
	 * @param width width of rectangle
	 * @param height height of rectangle
	 */
	void setValues(double left, double top, double width, double height)
	{
		this.left   = left;
		this.top    = top;
		this.width  = width;
		this.height = height;
		this.right  = left + width;
		this.bottom = top + height;
	}

	/**
	 * Determine if this rectangle overlaps with other rectangle.
	 * @param other rectangle to check for overlap
	 * @return true if this rectangle overlaps with other rectangle
	 */
	boolean overlaps(Rectangle other)
	{
		boolean noOverlap = (other.right <= this.left) 
				|| (this.right <= other.left) 
				|| (other.bottom <= this.top) 
				|| (this.bottom <= other.top);
		return !noOverlap;
	}

	/**
	 * Assuming that this rectangle and other rectangle overlap,
	 * calculate the minimum length vector required to translate this rectangle
	 * so that there is no longer any overlap between them.
	 * @param other rectangle to translate away from
	 * @return minimum length vector required to translate by to avoid overlap
	 */
	Vector2 getMinTranslationVector(Rectangle other)
	{
		Vector2[] differences = { 
				new Vector2(other.right - this.left, 0), // how to displace this to the right
				new Vector2(other.left - this.right, 0), // to the left
				new Vector2(0, other.bottom - this.top), // to the top
				new Vector2(0, other.top - this.bottom)  // to the bottom
		};

		// sort method may be used since Vector2 implements Comparable interface
		Arrays.sort(differences);

		return differences[0];
	}

	/**
	 * Determine if this rectangle contains the point (x,y).
	 * @param x x-coordinate of point
	 * @param y y-coordinate of point
	 * @return true, if this rectangle contains the point (x,y)
	 */
	boolean contains(double x, double y)
	{
		return (this.left <= x && x <= this.right && this.top <= y && y <= this.bottom);
	}
}