package org.libertybikes.ai.model;

import java.util.List;

import org.libertybikes.ai.model.Obstacle.MovingObstacle;

/**
 * Represents all of the JSON data (that we care about) from each
 * game tick received by game-service.
 */
public class GameTick {

    // Game tick JSON schema:
    // {
    //   "movingObstacles" : [
    //     OBSTACLE_SCHEMA,
    //     OBSTACLE_SCHEMA,
    //     etc...
    //   ],
    //   "obstacles" : [
    //     OBSTACLE_SCHEMA,
    //     OBSTACLE_SCHEMA,
    //     etc...
    //   ],
    //   "players" : [
    //     PLAYER_SCHEMA,
    //     PLAYER_SCHEMA,
    //     etc...
    //   ]
    // }
}
