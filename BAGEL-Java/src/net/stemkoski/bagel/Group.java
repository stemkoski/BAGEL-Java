package net.stemkoski.bagel;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

/**
 *  A collections of {@link Entity} objects.
 *
 */
public class Group extends Entity
{
	/**
	 *  The collection underlying this list.
	 */
    private ArrayList<Entity> list;
    
    /**
     *  The number of Entity objects currently in this list.
     */
    public int count;
    
    /**
     *  Initialize this object.
     */
    public Group()
    {
        this.list = new ArrayList<Entity>();
        this.count = 0;
    }

    /**
     *  Add an {@link Entity} to this collection.
     *  @param e The Entity being added to this collection.
     */
    public void addEntity(Entity e)
    {
        this.list.add(e);
        e.container = this;
        this.count++;
    }

    /**
     *  Remove an {@link Entity} from this collection.
     *  @param e The Entity being removed from this collection.
     */
    public void removeEntity(Entity e)
    {
        this.list.remove(e);
        e.container = null;
        this.count--;
    }
    
    /**
     *  Retrieve a (shallow) copy of this list.
     *  Especially useful in for loops. 
     *  Avoids concurrent modification exceptions
     *  when adding/removing from this collection during iteration.
     * @return a copy of the list underlying this collection
     */
    public ArrayList<Entity> list()
    {
        return new ArrayList<Entity>(list);   
    }

    /**
     * Retrieve a single Entity instance from this collection.
     * @param index The index of the entity in this collection.
     * @return a single Entity instance from this collection.
     */
    public Entity get(int index)
    {
    	return this.list().get(index);
    }
    
    /**
     *  Render all Entity objects in this collection to a canvas.
     */
    void draw(GraphicsContext context)
    {
        for ( Entity e : this.list() )
            e.draw(context);
    }
    
    /**
     *  Runs act method of all objects in this collection.
     */
    @Override
    void act(double deltaTime)
    {
        for ( Entity e : this.list() )
            e.act(deltaTime);
    }
}
