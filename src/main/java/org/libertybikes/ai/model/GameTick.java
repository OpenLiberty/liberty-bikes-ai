package org.libertybikes.ai.model;

import java.util.List;

/**
 * Represents all of the JSON data (that we care about) from each
 * game tick received by game-service.
 */
public class GameTick {
    public List<Obstacle> movingObstacles;
    public List<Player> players;
}
