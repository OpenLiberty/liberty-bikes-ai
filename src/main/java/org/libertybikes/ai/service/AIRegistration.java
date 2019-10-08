package org.libertybikes.ai.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.libertybikes.ai.restclient.GameServiceClient;

@WebListener
public class AIRegistration{
	
    @Inject
    @RestClient
    GameServiceClient client;

    @PostConstruct
    public void joinSession() {
        System.out.println("Trying to join party.");

        String roundId = client.getAvailableRounds();

        // open a websocket and join it.
        AIWebSocket socket = new AIWebSocket(roundId);
    }
    
}
