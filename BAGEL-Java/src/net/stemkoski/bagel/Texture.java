package net.stemkoski.bagel;

import java.io.File;
import javafx.scene.image.Image;

/**
 * Image data used when drawing a {@link Sprite} 
 * or a {@link Tile}. 
 * Texture objects are typically created using the {@link #load(String)} method.
 * Multiple instances of a {@link Sprite} may share a single Texture reference.
 *
 */
public class Texture
{
	/**
	 *  The image to be drawn.
	 */
	public Image image;
	
	/**
	 *  A rectangular sub-area of the image to be drawn.
	 */
	public Rectangle region;

	/**
	 *  Create an empty texture. Useful when reusing a previously loaded image;
	 *  used by {@link Animation} and {@link TileMap}.
	 */
	Texture()
    {  }

	/**
	 * Create a Texture from the image file with the given file name. 
	 * Sets {@link #region} to the original image dimensions.
	 * @param imageFileName name of the image file
	 * @return A Texture object that displays the image file with the given file name.
	 */
	public static Texture load(String imageFileName)
    {
        Texture tex = new Texture();
        String fileName = new File(imageFileName).toURI().toString();
        tex.image   = new Image( fileName );
        tex.region  = new Rectangle();
        tex.region.setValues( 0, 0, tex.image.getWidth(), tex.image.getHeight() );
        return tex;
    }
}
