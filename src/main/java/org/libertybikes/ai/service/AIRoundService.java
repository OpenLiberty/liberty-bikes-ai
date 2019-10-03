/**
 *
 */
package org.libertybikes.ai.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.libertybikes.ai.restclient.GameServiceClient;

@ApplicationScoped
@Path("open")
public class AIRoundService {

    @Inject
    @RestClient
    GameServiceClient client;

    @GET
    @Path("/join/{partyId}")
    public String joinSession(@PathParam("partyId") String partyId) {
        System.out.println("Trying to join " + partyId + " party.");

        // get party's current round
        String roundId = client.getRoundId(partyId);

        // open a websocket and join it.
        AIWebSocket socket = new AIWebSocket(roundId);

        return "connected";
    }

}
