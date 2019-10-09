/**
 * Represents the main logic of your custom Liberty Bikes AI. Every game tick AIWebSocket will
 * call processAIMove(String message) where the message represents the state of the game board
 * at that time. Using this information you have to decide when to change your bike's direction.
 * 
 * Helpful Information:
 * 
 * The game board is represented as a 2d array of size 121 
 *  - top left corner is [0][0] 
 *  - bottom right is [120][120]
 *  
 * Each game tick you will recieve a list of all obstacles (including players)
 *  - Each obstacle is a rectangle
 *  - Their stated position is the top left space they are occupying
 *  - Adding their height and width, you can determine the total space they occupy
 *  
 * Each game tick, each player will leave a trail behind from the middle of their 3x3 surface area
 *  - This is not provided in the message, you will have to keep track of the building walls.
 *  - For example, a player at [50][50] will leave an trail at [51][51]
 *  - Moving obstacles can erase player trails
 */
package org.libertybikes.ai;

import org.libertybikes.ai.service.AIWebSocket;
import org.libertybikes.ai.service.AIWebSocket.DIRECTION;

public class AIClass {

    AIWebSocket socket;

    public AIClass(AIWebSocket socket) {
        this.socket = socket;
    }

    /*
     * This should contain the main logic of the AI, it receives a JSON of all the
     * objects on the board, players and obstacles.
     */
    public void processAiMove(String message) {
        socket.sendDirection(DIRECTION.RIGHT);
    }
}
