/**
 * This class represents a WebSocket that will be opened up between this liberty-bikes-ai service
 * and a GameRoundWebsocket from a running Liberty Bikes game-service. After a connection is sucessfully
 * established, the @OnMessage methos will recieve messages every game tick with an update on the state
 * of the game. This websocket can send messages back to the game-service to tell it to update the direction
 * of the bike being controlled by this service.
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
	/**
	 * After you go to the instance of Liberty-Bikes running and register your bot
	 * you will stick your key here, we will then send this key back as we join a game.
	 */
	String key = "";
	/**
	 * The Uri of the instance of Liberty-Bikes you are joining, it is currently pointed
	 * at the default location for a locally ran instance but this will need to be changed
	 * to a public Uri once we get to that point of the lab where you are joining the lab
	 * host's Liberty-Bikes instance.
	 */
    private static String uri = "ws://localhost:8080/round/ws/";
    
    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    };	

    public Session session;

    AIClass AI;

    public AIWebSocket(String roundId) {     
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

    // Every game tick, we recieve a message back listing each object on the game board and their location
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
    	//System.out.println("Recieved Message: " + message);
        AI.processAiMove(message);
    }

    // Sends a message representing a direction to tell the game-service what way to turn your bike
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
