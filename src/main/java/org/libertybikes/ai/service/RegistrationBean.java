package org.libertybikes.ai.service;

import java.net.URI;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.annotation.JsonbProperty;
import javax.websocket.ContainerProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;

import org.libertybikes.ai.restclient.GameServiceClient;

/**
 * This bean handles getting the bot into its first round and then requeueing
 * the bot to the next round after the current round has completed
 */
@ApplicationScoped
public class RegistrationBean {

    private static final Jsonb jsonb = JsonbBuilder.create();

    AIConfiguration config;

    GameServiceClient gameService;

    public void joinRound() {
        System.out.println("Attempting to register with game service...");

        // TODO: use MPRestClient to pull the valid partyID from the game service
        String partyId = "";

        // MP Rest Client doesn't yet support JAX-RS server-sent-events, so we nee to do this manually
        // enhancement tracked at: https://github.com/eclipse/microprofile-rest-client/issues/11
        String targetStr = config.getGameServiceHttp() + "/party/" + partyId + "/queue?playerId=" + config.getPlayerId();
        System.out.println("Establishing SSE target at: " + targetStr);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(targetStr);
        // intentionally "leak" the source, it will be closed in processInboundEvent() when the proper event is received
    }

    public static class InboundMessage {
        @JsonbProperty("requeue")
        public String roundId;
    }

    private void processInboundEvent(InboundSseEvent event, SseEventSource source) {
        Objects.requireNonNull(event, "InboundSseEvent was null");
        System.out.println("Got raw SSE message: " + event.readData());
        InboundMessage msg = jsonb.fromJson(event.readData(), InboundMessage.class);
        if (msg.roundId != null) {
            System.out.println("Ready to move into round: " + msg.roundId);
            try {
                String ws = config.getGameServiceWebsocket() + msg.roundId;
                System.out.println("Establishing websocket to: " + ws);
                AIWebSocket websocket = new AIWebSocket(config, this);
                ContainerProvider.getWebSocketContainer().connectToServer(websocket, new URI(ws));
                System.out.println("Successfully connected to server");
                source.close();
            } catch (Exception e) {
                System.out.println("Unable to establish websocket due to:");
                e.printStackTrace();
            }
        }
    }

}
