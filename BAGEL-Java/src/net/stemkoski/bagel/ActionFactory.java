package net.stemkoski.bagel;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * A factory class for creating {@link Action} objects.
 */
public class ActionFactory
{
    /**
     * Create an Action to move a Sprite by a fixed amount over a period of time.
     * <br>This Action is complete once movement has finished.
     * @param deltaX distance to move Sprite in the x-direction
     * @param deltaY distance to move Sprite in the y-direction
     * @param duration total time to be used for movement
     * @return Action to move a Sprite by a fixed amount over a period of time.
     */
    public static Action moveBy(double deltaX, double deltaY, double duration)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                target.moveBy( deltaX/duration * deltaTime, deltaY/duration * deltaTime );
                return (totalTime >= duration);
            }
        );
    }

    /**
     * Create an Action to rotate a Sprite by a fixed amount over a period of time.
     * <br>This Action is complete once rotation has finished.
     * @param deltaAngle angle to rotate Sprite
     * @param duration total time to be used for movement
     * @return Action to rotate a Sprite by a fixed amount over a period of time
     */
    public static Action rotateBy(double deltaAngle, double duration)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                target.rotateBy( deltaAngle/duration * deltaTime );
                return (totalTime >= duration);
            }
        );
    }

    /**
     * Create an Action to fade out (reduce opacity of) a Sprite over a period of time.
     * <br>To automatically remove a Sprite once it has finished fading out, use:
     * <br><code>ActionFactory.sequence( ActionFactory.fadeOut(fadeRate), ActionFactory,remove() )</code>
     * <br>This Action is complete once the Sprite's opacity reaches 0.
     * @param fadeRate amount to reduce opacity by per second
     * @return Action to fade out a Sprite over a period of time
     */
    public static Action fadeOut(double fadeRate)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                target.opacity -= fadeRate * deltaTime;
                if (target.opacity < 0)
                    target.opacity = 0;
                return (target.opacity <= 0);
            }
        );
    }

    /**
     * Create an Action to fade in (increase opacity of) a Sprite over a period of time.
     * <br>This Action is complete once the Sprite's opacity reaches 1.
     * @param fadeRate amount to increase opacity by per second
     * @return Action to fade in a Sprite over a period of time
     */
    public static Action fadeIn(double fadeRate)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                target.opacity += fadeRate * deltaTime;
                if (target.opacity > 1)
                    target.opacity = 1;
                return (target.opacity >= 1);
            }
        );
    }

    /**
     * Create an Action to perform a sequence of actions. 
     * Each Action in the list is performed only after all previous actions in the list have completed.
     * <br>This Action is complete once all the corresponding actions are complete.
     * @param actions one or more actions to perform in sequence
     * @return Action to perform a sequence of actions
     */
    public static Action sequence(Action... actions)
    { 
        return new Action()
        {
            ArrayList<Action> actionList = new ArrayList<Action>(Arrays.asList(actions));
            int currentIndex = 0;
            
            @Override
            public boolean apply(Sprite target, double dt)
            {
                Action currentAction = actionList.get(currentIndex);
                boolean finished = currentAction.apply(target, dt);
                if (finished)
                {
                    currentIndex += 1;
                }
                return (currentIndex == actionList.size());
            }
            
            @Override
            public void reset()
            {
                for (Action action : actionList)
                    action.reset();

                currentIndex = 0;
            }
        };
    }

    /**
     * Create an Action that repeats another Action a fixed number of times.
     * <br>This Action is complete once the specified Action has been completed
     *  the specified number of times.
     * @param action Action to be repeated
     * @param totalTimes total number of times to repeat the specified Action
     * @return Action that repeats another Action a fixed number of times
     */
    public static Action repeat(Action action, int totalTimes)
    { 
        return new Action()
        {
            int finishedTimes = 0;

            @Override
            public boolean apply(Sprite target, double dt)
            {
                boolean finished = action.apply(target, dt);
                if (finished)
                {
                    finishedTimes += 1;
                    action.reset();
                }
                return (finishedTimes == totalTimes);
            }
        };
    }

    /**
     * Create an Action that repeats another Action forever.
     * <br>This Action is never complete.
     * @param action Action to be repeated
     * @return Action that repeats another Action forever
     */
    public static Action forever(Action action)
    { 
        return new Action()
        {
            @Override
            public boolean apply(Sprite target, double dt)
            {
                boolean finished = action.apply(target, dt);
                if (finished)
                    action.reset();
                return false;
            }
        };
    }

    /**
     * Create an Action that waits for a specified amount of time.
     * This is typically used in conjunction with {@link #sequence(Action...)}.
     * <br>This Action is complete once the specified amount of time passes.
     * @param duration amount of time to wait
     * @return Action that waits for a specified amount of time
     */
    public static Action delay(double duration)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                return (totalTime >= duration);
            }
        );
    }

    /**
     * Create an Action that automatically removes a Sprite.
     * <br>This Action is complete immediately.
     * @return Action that automatically removes a Sprite
     */
    public static Action remove()
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                target.remove();
                return true;
            }
        );
    }
    
    /**
     * Create an Action that checks if animation is finished.
     * <br> Useful in conjunction with {@link #sequence(Action...)} and {@link #remove()}
     * to remove a Sprite after its animation is complete.
     * @return Action that automatically removes a Sprite
     */
    public static Action isAnimationFinished()
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                return target.animation.isFinished();
            }
        );
    }

    /**
     * Create an Action that automatically calls {@link Sprite#boundToScreen(double, double)}.
     * <br> This Action is never completed.
     * @param screenWidth width of screen
     * @param screenHeight height of screen
     * @return Action that automatically calls {@link Sprite#boundToScreen(double, double)}.
     */
    public static Action boundToScreen(double screenWidth, double screenHeight)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                target.boundToScreen(screenWidth, screenHeight);
                return false;
            }
        );
    }

    /**
     * Create an Action that automatically calls {@link Sprite#wrapToScreen(double, double)}.
     * <br>This Action is never completed.
     * @param screenWidth width of screen
     * @param screenHeight height of screen
     * @return Action that automatically calls {@link Sprite#wrapToScreen(double, double)}.
     */
    public static Action wrapToScreen(double screenWidth, double screenHeight)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                target.wrapToScreen(screenWidth, screenHeight);
                return false;
            }
        );
    }

    /**
     * Create an Action that removes a Sprite if no portion of the Sprite remains
     * within the screen boundaries.
     * <br>This Action is complete if the Sprite is removed from the screen.
     * @param screenWidth width of screen
     * @param screenHeight height of screen
     * @return Action that removes an object if no portion of the object remains
     * within the screen boundaries
     */
    public static Action destroyOutsideScreen(double screenWidth, double screenHeight)
    {
        return new Action(
            (Sprite target, double deltaTime, double totalTime) ->
            {
                if (!target.isOnScreen(screenWidth, screenHeight))
                {
                    target.remove();
                    return true;
                }
                else
                {
                    return false;
                }
            }
        );
    }

}