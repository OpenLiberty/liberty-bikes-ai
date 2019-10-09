/**
 * Represents the information associated with each player in Liberty-Bikes
 */
package org.libertybikes.ai.model;

import org.libertybikes.ai.service.AIWebSocket.DIRECTION;

public class Player { 	
    public String id;
    public String color;
    public String status;
    public boolean alive;
    public int x;
    public int y;
    public int height;
    public int width;
    public DIRECTION direction;
}
