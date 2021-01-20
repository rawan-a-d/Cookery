package service;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import service.controller.NotificationSocketController;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publisher {
//    private static final URI BASE_URI = URI.create("http://localhost:90");
//    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(90).build();
    private static final URI BASE_URI = UriBuilder.fromUri("http://0.0.0.0/").port(90).build();

//    private static final URI BASE_URI = URI.create("http://0.0.0.0:90/"); // docker

    public static void main(String[] args) {
        try {
            CustomApplicationConfig customApplicationConfig = new CustomApplicationConfig();
            // create a grizzly server
            // don't start the server yet, wait until the websocket addon has been added
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, customApplicationConfig, false);

            // Create websocket addon (add this service to our http server)
            WebSocketAddOn webSocketAddOn = new WebSocketAddOn();
            server.getListeners().forEach(listener -> { listener.registerAddOn(webSocketAddOn);}); // listen to web socket

            // register my websocket app (shape web socket, specify under which url it will be available -> root/ws/demo)
            NotificationSocketController socketController = NotificationSocketController.getInstance(); // notification controller
            WebSocketEngine.getEngine().register("/ws", "/notification", socketController);

            // Now start the server
            server.start();


            System.out.println("Hosting resources at " + BASE_URI.toURL());

            System.out.println("Try the following GET operations in your internet browser: ");
            String[] getOperations = {BASE_URI.toURL() + "users",
                    BASE_URI.toURL() + "users/2"};
            for (String getOperation : getOperations) {
                System.out.println(getOperation);
            }


        } catch (IOException ex) {
            Logger.getLogger(Publisher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
