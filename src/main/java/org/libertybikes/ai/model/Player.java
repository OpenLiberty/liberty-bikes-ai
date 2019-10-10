package org.libertybikes.ai.model;

import org.libertybikes.ai.AILogic.DIRECTION;

/**
 * Represents the information associated with each player in Liberty Bikes
 */
public class Player extends Obstacle {

    // Player schema:
    // {
    //   "id" : String,
    //   "name" : String,
    //   "color" : String,
    //   "status" : String,
    //   "alive"  : boolean,
    //   "x"      : int,
    //   "y"      : int,
    //   "width"  : int,
    //   "height" : int,
    //   "direction" : "UP"|"DOWN"|"LEFT"|"RIGHT"
    // }
    // Sample data:
    // {"id":"1KGV-RS2Y-66NJ-9I8M","name":"Bill","color":"#ABD155",
    //   "status":"Dead","alive":false,"x":68,"y":12,"width":3,
    //   "height":3,"direction":"RIGHT"}

    public String id;
    public String status;
    public boolean alive;
    public DIRECTION direction;
}
