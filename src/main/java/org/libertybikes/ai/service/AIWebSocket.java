package org.libertybikes.ai.service;

import java.io.IOException;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.libertybikes.ai.AILogic;
import org.libertybikes.ai.AILogic.DIRECTION;
import org.libertybikes.ai.model.GameTick;

/**
 * This class represents a WebSocket that will be opened up between this AI service and a
 * GameRoundWebsocket from a running Liberty Bikes game-service. After a connection is sucessfully
 * established, the @OnMessage method will receive messages every game tick with an update
 * on the state of the game. This websocket can send messages back to the game-service to tell
 * it to update the direction of the bike being controlled by this service.
 */
@ClientEndpoint
public class AIWebSocket {

    private static final Jsonb jsonb = JsonbBuilder.create();

    private final AIConfiguration config;
    private final RegistrationBean registration;

    private Session session;
    private AILogic aiLogic;
    private DIRECTION currentDirection = DIRECTION.UP;

    public AIWebSocket(AIConfiguration config, RegistrationBean registration) {
        this.config = config;
        this.registration = registration;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        String dataai = "{\"playerjoined\":\"" + config.getPlayerId() + "\"}";
        sendMessage(dataai);
        aiLogic = new AILogic(config.getPlayerId());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Close Websocket with reason: " + reason);
    }

    // Every game tick, we recieve a message back listing each object on the game board and their location
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        //System.out.println("got msg: " + message);
        try {
            if ("{\"gameStatus\":\"FINISHED\"}".equals(message)) {
                // game over, requeue
                session.close();
                registration.joinRound();
            } else {
                // use jsonb to convert from String --> POJO and more easily made decisions on where to move
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sends a message representing a direction to tell the game-service what way to turn your bike
    private void sendDirection(DIRECTION dir) {
        switch (dir) {
            case RIGHT:
                sendMessage("{\"direction\":\"RIGHT\"}");
                break;
            case DOWN:
                sendMessage("{\"direction\":\"DOWN\"}");
                break;
            case LEFT:
                sendMessage("{\"direction\":\"LEFT\"}");
                break;
            default:
                sendMessage("{\"direction\":\"UP\"}");
                break;
        }
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
            System.out.println("Sent message: " + message);
        } catch (Exception e) {
            System.out.println("Failed to send message: " + message);
            e.printStackTrace();
        }
    }
}
