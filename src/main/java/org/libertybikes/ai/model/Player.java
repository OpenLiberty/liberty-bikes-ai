package org.libertybikes.ai.model;

import org.libertybikes.ai.AILogic.DIRECTION;

/**
 * Represents the information associated with each player in Liberty Bikes
 */
public class Player {
    public String id;
    public String status;
    public boolean alive;
    public int x;
    public int y;
    public int height;
    public int width;
    public DIRECTION direction;
}
