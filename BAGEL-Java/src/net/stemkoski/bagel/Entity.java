package net.stemkoski.bagel;


import javafx.scene.canvas.GraphicsContext;

/**
 * Base class for all objects or collections of objects
 * that can be rendered to the screen.
 * 
 *
 */
public class Entity
{
    /**
     * The {@link Group} containing this Entity. The root object for the scene
     * will have {@link #container} set to null.
     */
    Group container;
    
    /**
     * Remove this object from the {@link Group} it is contained in.
     */
    public void remove()
    {
        if (container != null)
            container.removeEntity(this);
    }

    /**
     * Render this Entity to a canvas. 
     * @param context GraphicsContext object that handles drawing to the canvas
     */
    void draw(GraphicsContext context)
    {  }
    
    /**
     * Run any {@link Action} objects added to this Entity.
     * @param deltaTime amount of time that has passed since the last iteration of the game loop
     */
    void act(double deltaTime)
    {  }
    
}