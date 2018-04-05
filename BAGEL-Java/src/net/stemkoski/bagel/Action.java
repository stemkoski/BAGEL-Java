package net.stemkoski.bagel;

/**
 * A framework for storing a method that is applied to a target {@link Sprite} over time. 
 * Actions are typically created with the static methods in the 
 * ActionFactory class, added to the target Sprite, and will then be applied 
 * automatically. Custom Action objects may be created by using lambda 
 * expressions that conform to the Function interface, such as:
 * <pre>{@code
 * Action teleportRight = new Action(
 *   (target, deltaTime, totalTime) ->
 *   {
 *     target.moveBy(100, 0);
 *     return true;
 *   }
 * );
 * }</pre>
 */
public class Action
{
    /**
     * This interface is used internally by the Action class to encapsulate a
     *  method that will be applied to a target Sprite.
     *
     */
    interface Function
    {
        /**
         * @param target Sprite to which this method will be applied
         * @param deltaTime elapsed time (seconds) since previous iteration of game loop (typically approximately 1/60 second)
         * @param totalTime  the total time that has elapsed since the encapsulating Action has started
         * @return true if the function has completed, false otherwise
         */
        boolean run(Sprite target, double deltaTime, double totalTime);
    }
    
    /**
     * The total time that has elapsed since the Action has started.
     */
    double totalTime;
    
    /**
     * Encapsulates the method that will be applied to the Sprite this Action is attached to.
     */
    Function function;
    
    /**
     * 
     * This constructor is used by {@link ActionFactory} methods that need to extend the Action class to store additional information or override methods, typically involving one or more additional Actions (see ActionFactory.sequence, repeat, forever).
     */
    Action()
    {
        this.totalTime = 0;
    }
    
    /**
     * This constructor is used by simple {@link ActionFactory} methods or to create custom Action objects.
     * @param f Function object that encapsulates the method that will be applied to the Sprite this Action is attached to.
     * 
     */
    public Action(Function f)
    {
        this.totalTime = 0;
        this.function  = f;
    }
    
    /**
     * Increments totalTime by deltaTime and applies function.run method to target.
     * @param target Sprite to which the function.run method will be applied
     * @param deltaTime elapsed time (seconds) since previous iteration of game loop (typically approximately 1/60 second)
     * @return true if the function.run method has completed, false otherwise
     */
    boolean apply(Sprite target, double deltaTime)
    {
        this.totalTime += deltaTime;
        return this.function.run(target, deltaTime, totalTime);
    }
    
    /**
     * Sets {@link #totalTime} to 0, effectively restarting this Action.
     */
    public void reset()
    {
        this.totalTime = 0;
    }
}
    
    
    