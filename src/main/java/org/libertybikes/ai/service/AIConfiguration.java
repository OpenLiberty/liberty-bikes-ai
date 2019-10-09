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

    @Inject
    @ConfigProperty(name = GAME_SERVICE_KEY + "/mp-rest/url", defaultValue = "http://localhost:8080")
    private String gameServiceUrl;

    /**
     * After you go to the instance of Liberty-Bikes running and register your bot
     * you will stick your key in src/main/resources/META-INF/microprofile-config.properties
     */
    @Inject
    @ConfigProperty(name = "player_key")
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
