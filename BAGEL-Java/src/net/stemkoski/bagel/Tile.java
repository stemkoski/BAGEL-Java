package net.stemkoski.bagel;

/**
 * A small rectangular image used in a {@link TileMap}.
 *
 */
public class Tile extends Entity
{
	
	/**
	 * x-coordinate of tile center
	 */
	public double x;

	/**
	 * y-coordinate of tile center
	 */
	public double y;
	
	/**
	 * tile width
	 */
	public double width;
	
	/**
	 * tile height
	 */
	public double height;
	
	/**
	 * index of texture in list from corresponding {@link TileMap} 
	 */
	public int tileTextureIndex;
    
	/**
	 * Full boundary of tile. Used for collision detection,
	 * but not for collision resolution, 
	 * due to "corner snag" or "internal edge" issues that can interfere with Sprite movement
	 */
	public Rectangle boundary;
    
	/**
	 * the left edge of the tile, if accessible
	 */
	public Rectangle edgeLeft;

	/**
	 * the right edge of the tile, if accessible
	 */
	public Rectangle edgeRight;
	
	/**
	 * the top edge of the tile, if accessible
	 */
	public Rectangle edgeTop;
	
	/**
	 * the bottom edge of the tile, if accessible
	 */
	public Rectangle edgeBottom;
    
	/**
	 * Set the location, size, and tile image index for this Tile.
	 * Automatically sets boundary rectangle; 
	 * edge data is set by corresponding {@link TileMap}. 
	 * @param x x-coordinate of tile center
	 * @param y y-coordinate of tile center
	 * @param width tile width
	 * @param height tile height
	 * @param tileTextureIndex index of texture in list from corresponding {@link TileMap}
	 */
    public Tile(double x, double y, double width, double height, int tileTextureIndex)
    {
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
        this.tileTextureIndex = tileTextureIndex;
        
        this.boundary = new Rectangle( x - width/2, y - height/2, width, height );
    }
}
