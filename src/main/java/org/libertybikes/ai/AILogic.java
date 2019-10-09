package org.libertybikes.ai;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

/**
 * Represents the main logic of your custom Liberty Bikes AI. Every game tick AIWebSocket will
 * call processAIMove(String message) where the message represents the state of the game board
 * at that time. Using this information you have to decide when to change your bike's direction.
 *
 * Helpful Information:
 *
 * The game board is represented as a 2d array of size 121
 * - top left corner is [0][0]
 * - bottom right is [120][120]
 *
 * Each game tick you will recieve a list of all obstacles (including players)
 * - Each obstacle is a rectangle
 * - Their stated position is the top left space they are occupying
 * - Adding their height and width, you can determine the total space they occupy
 *
 * Each game tick, each player will leave a trail behind from the middle of their 3x3 surface area
 * - This is not provided in the message, you will have to keep track of the building walls.
 * - For example, a player at [50][50] will leave an trail at [51][51]
 * - Moving obstacles can erase player trails
 */
public class AILogic {

    public static enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    };

    private static final Jsonb jsonb = JsonbBuilder.create();

    private int tick = 0;

    /*
     * This should contain the main logic of the AI, it receives a JSON straing of all the
     * objects on the board, players and obstacles.
     */
    public DIRECTION processAiMove(String message) {
        System.out.println("got msg: " + message);
        // use jsonb to convert from String --> POJO and more easily made decisions on where to move
        tick++;
        if (tick % 10 == 0)
            return DIRECTION.DOWN;
        else
            return DIRECTION.RIGHT;
    }
}
