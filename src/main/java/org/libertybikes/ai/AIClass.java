/**
 *
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
