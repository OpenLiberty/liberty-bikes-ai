package org.libertybikes.ai.model;

/**
 * Represents the information associated with each obstacle in Liberty Bikes
 */
public class Obstacle {

    // Game tick JSON schema:
    // {
    //   "height : int,
    //   "width" : int,
    //   "x"     : int,
    //   "y"     : int
    // }
    //
    // Sample data:
    // {"height":10,"width":2,"x":48,"y":10}

    public static class MovingObstacle extends Obstacle {}
}
