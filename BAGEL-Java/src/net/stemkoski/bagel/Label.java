package net.stemkoski.bagel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * A structure to store and display text.
 * Most properties are public and should be set directly.
 */
public class Label extends Entity
{
	/**
	 * Name of the current font. Set in {@link #loadFontFromSystem(String, int)}
	 * or set by file in {@link #loadFontFromFile(String, int)}.
	 */
	String fontName;
	
	/**
	 * Size of the current font. Set in {@link #loadFontFromSystem(String, int)}
	 * or {@link #loadFontFromFile(String, int)}.
	 */
	int fontSize;
	
	/**
	 * Automatically set by load methods.
	 */
	Font font;
	
	/**
	 * color used to draw font
	 */
	public Color fontColor;

	/**
	 * text to display in label
	 */
	public String text;
	
	/**
	 * x-coordinate of anchor of label; see {@link #setPosition(double, double)}
	 */
	public double x;
	
	/**
	 * y-coordinate of anchor of label; see {@link #setPosition(double, double)}
	 */
	public double y;
	
	/**
	 * text alignment ("LEFT", "CENTER", "RIGHT") with respect to anchor point (x,y)
	 */
	public String alignment;
	
	/**
	 * determines if font border will be drawn
	 */
	public boolean borderDraw;
	
	/**
	 * size of font border 
	 */
	public int borderSize;
	
	/**
	 * color used to draw font border
	 */
	public Color borderColor;
	
	/**
	 * determines if label will be visible
	 */
	public boolean visible;
    
	/**
	 * Initialize label to default settings.
	 */
    public Label()
    {
        this.fontName = "Arial";
        this.fontSize = 16;
        this.font = new Font( this.fontName, this.fontSize );
        this.fontColor = Color.BLACK;
        this.text = " ";
        this.x = 0;
        this.y = 0;
        this.alignment = "LEFT";
        this.borderDraw = false;
        this.borderSize = 1;
        this.borderColor = Color.BLACK;
        this.visible = true;
    }

    /**
     * Configure this label to use a font already installed on this system.
     * @param fontName name of font (e.g. "Arial", "Times New Roman", "Courier New"); must be installed on system
     * @param fontSize size of font
     */
    public void loadFontFromSystem(String fontName, int fontSize)
    {
        this.font = new Font( fontName, fontSize );
        this.fontName = fontName;
        this.fontSize = fontSize;
    }

    /**
     * Configure this label to use a font from a specified file.
     * @param fontFileName name of font file
     * @param fontSize size of font
     */
    public void loadFontFromFile(String fontFileName, int fontSize)
    {
    	// String fileName = new File(fontFileName).toURI().toURL().toString();
        this.font = Font.loadFont( "file:" + fontFileName, fontSize );    
        this.fontName = this.font.getName();
        this.fontSize = fontSize;
    }
    
    /**
     * Set the coordinates of the anchor position of this label;
     *  this may be to the left, center, or right of the text
     *  according to the value of {@link #alignment}.  
     * @param x x-coordinate of anchor of label
     * @param y y-coordinate of anchor of label
     */
    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Render this text to a canvas used specified parameters. 
     */
    @Override
    public void draw(GraphicsContext context)
    {
        if ( !this.visible )
            return;
            
        context.setFont( this.font );
        context.setFill( this.fontColor );

        if (this.alignment.equals("LEFT"))
            context.setTextAlign(TextAlignment.LEFT);
        else if (this.alignment.equals("CENTER"))
            context.setTextAlign(TextAlignment.CENTER);
        else if (this.alignment.equals("RIGHT"))
            context.setTextAlign(TextAlignment.RIGHT);

        context.setTransform(1,0, 0,1, 0,0);
        context.setGlobalAlpha(1);
        context.fillText( this.text, this.x, this.y );
        
        if (this.borderDraw)
        {
            context.setStroke(this.borderColor);
            context.setLineWidth(this.borderSize);
            context.strokeText( this.text, this.x, this.y );
        }
    }

}
