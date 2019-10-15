package org.libertybikes.ai.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Holds all of the externalized configuration for this service
 */
@ApplicationScoped
public class AIConfiguration {

    public static final String GAME_SERVICE_KEY = "GAME_SERVICE_URL";

	// We need config properties for the game-service url we will connect to and
	// the bot key we recieve when we register
    private String gameServiceUrl;

    /**
     * After you go to the instance of Liberty-Bikes running and register your bot
     * you will stick your key in src/main/resources/META-INF/microprofile-config.properties
     */
    private String playerKey;

    public String getGameServiceHttp() {
        return gameServiceUrl;
    }

    public String getGameServiceWebsocket() {
        return gameServiceUrl.replace("http://", "ws://") + "/round/ws/";
    }

    public String getPlayerId() {
        return playerKey;
    }

}
