/**
 *
 */
package org.libertybikes.ai.service;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.libertybikes.ai.AIClass;


@ClientEndpoint
public class AIWebSocket {

    private static String uri = "ws://localhost:8080/round/ws/";
    

    public Session session;

    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    };

    AIClass AI;

    public AIWebSocket(String roundId) {
        String key = "";
        System.out.println("Initializing WebSocket with round " + roundId + " and key " + key);

        try {
            URI endpointURI = new URI(uri + roundId);
            
            // Connect to LibertyBikes GameRoundWebsocket
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
            
            String dataai = "{\"playerjoined\":\"" + key + "\"}";
            sendMessage(dataai);

            AI = new AIClass(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Opened Websocket");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close Websocket");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        AI.processAiMove(message);
    }

    public void sendDirection(DIRECTION dir) {
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
        this.session.getAsyncRemote().sendText(message);
    }
}
