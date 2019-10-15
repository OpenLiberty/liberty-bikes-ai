package org.libertybikes.ai.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This class will be initialized when the app first starts. We can use this hook
 * to automatically register our AI service with the core Liberty Bikes services
 */
@WebListener
public class StartupProcedure implements ServletContextListener {

    @PostConstruct
    public void joinFirstRound() {
        System.out.println("Hello world!");
    }
}