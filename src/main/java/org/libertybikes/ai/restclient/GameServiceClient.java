package org.libertybikes.ai.restclient;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.libertybikes.ai.service.AIConfiguration;

/**
 * This class is a type-safe REST client that allows this service to easily make HTTP
 * requests on an the game-service. By default it points to a locally running instance,
 * but can be overridden with externalized configuration via MP Config to point to a
 * remote game-service instance.
 */
public interface GameServiceClient {}
