package net.stemkoski.bagel;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

/**
 * A structure for storing and updating the state of 
 *  hardware input devices (keyboard and mouse).
 *  Handles key down/press/up events, 
 *  mouse down/up events, and mouse position. 
 */
public class Input
{
    ArrayList<String> keyDownQueue;
    ArrayList<String> keyUpQueue;
    ArrayList<String> keyDownList;
    ArrayList<String> keyPressedList;
    ArrayList<String> keyUpList;

    boolean mouseButtonDownQueue;
    boolean mouseButtonUpQueue;
    boolean mouseButtonDown;
    boolean mouseButtonUp;
    
    double mouseX;
    double mouseY;
    
    /**
     * Initialize object and activate event listeners.
     * @param listeningScene the window Scene that has focus during the game
     */
    Input(Scene listeningScene)
    {
        keyDownQueue   = new ArrayList<String>();
        keyUpQueue     = new ArrayList<String>();
        keyDownList    = new ArrayList<String>();
        keyPressedList = new ArrayList<String>();
        keyUpList      = new ArrayList<String>();

        // Example Strings: UP, LEFT, Q, DIGIT1, SPACE, SHIFT
        listeningScene.setOnKeyPressed( 
            (KeyEvent event) -> this.keyDownQueue.add(event.getCode().toString()) 
        );
        
        listeningScene.setOnKeyReleased( 
            (KeyEvent event) -> this.keyUpQueue.add(event.getCode().toString()) 
        );
        
        listeningScene.setOnMousePressed(
            (MouseEvent event) -> this.mouseButtonDownQueue = true
        );
        
        listeningScene.setOnMouseReleased(
            (MouseEvent event) -> this.mouseButtonUpQueue = true
        );
        
        // this works because the canvas and window are the same size?
        listeningScene.setOnMouseMoved(
            (MouseEvent event) -> 
            {
                this.mouseX = event.getX();
                this.mouseY = event.getY();
            }
        );
    }

    /**
     * Determine if key has been pressed / moved to down position (a discrete action).
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key was just pressed
     */
    public boolean isKeyDown(String keyName)
    {  return this.keyDownList.contains(keyName);  }

    /**
     * Determine if key is currently being pressed / held down (a continuous action).
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key is currently pressed
     */
    public boolean isKeyPressed(String keyName)
    {  return this.keyPressedList.contains(keyName);  }

    /**
     * Determine if key has been released / returned to up position (a discrete action).
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key was just released
     */
    public boolean isKeyUp(String keyName)
    {  return this.keyUpList.contains(keyName);  }

    /**
     * Return current position of mouse on game canvas.
     * @return current position of mouse
     */
    public Vector2 getMousePosition()
    {  return new Vector2(this.mouseX, this.mouseY);  }
    
    /**
     * Determine if (any) mouse button has been pressed / moved to down position.
     * @return true if mouse button was just pressed
     */
    public boolean isMouseButtonDown()
    {  return this.mouseButtonDown;  }
    
    /**
     * Determine if (any) mouse button has been released / returned to up position.
     * @return true if mouse button was just released
     */
    public boolean isMouseButtonUp()
    {  return this.mouseButtonUp;  }
    
    /**
     * Determine if (any) mouse button has recently been pressed
     *   while mouse cursor position is contained with bounding area of a sprite.
     * @param sprite Sprite to check if clicked by mouse
     * @return true if the mouse has clicked on the sprite
     */
    public boolean isClicked(Sprite sprite)
    {  return this.mouseButtonDown && sprite.getBoundary().contains( this.mouseX, this.mouseY );  }
    
    /**
     *  Update state information for keyboard and mouse.
     *  Automatically called by {@link Game} class during the game loop.
     */
    void update()
    {
        // clear previous discrete event status
        this.keyDownList.clear();
        this.keyUpList.clear();
        this.mouseButtonDown = false;
        this.mouseButtonUp = false;
        
        // update current event status
        for (String k : this.keyDownQueue)
        {
            // avoid multiple keydown events while holding key
            // avoid duplicate entries in key pressed list
            if ( !this.keyPressedList.contains(k) )
            {
                this.keyDownList.add(k);
                this.keyPressedList.add(k);
            }
        }
        
        for (String k : this.keyUpQueue)
        {
            this.keyPressedList.remove(k);
            this.keyUpList.add(k);
        }
        
        if (this.mouseButtonDownQueue)
            this.mouseButtonDown = true;
            
        if (this.mouseButtonUpQueue)
            this.mouseButtonUp = true;
            
        // clear the queues used to store events
        this.keyDownQueue.clear();
        this.keyUpQueue.clear();
        this.mouseButtonDownQueue = false;
        this.mouseButtonUpQueue = false;
    }
}

