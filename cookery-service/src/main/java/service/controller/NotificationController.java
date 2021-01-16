package service.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import service.model.DTO.Notification;
import service.model.DTO.UserDTO;
import service.model.DTO.UserFollowDTO;
import service.model.Recipe;

import java.io.IOException;
import java.util.*;

public class NotificationController extends WebSocketApplication {
    // list of sockets
    private static final Set<WebSocket> sockets = Collections.synchronizedSet(new HashSet<>());
    // Connect user's id with web socket
    private static final Map<Integer, WebSocket> socketsMap = Collections.synchronizedMap(new HashMap<>());
    private WebSocket lastSocket;

    // Singleton
    private static NotificationController myself; // this

    private NotificationController() {
    }

    public static NotificationController getInstance() {
        if(myself == null) {
            myself = new NotificationController();
        }

        return myself;
    }


    // when a client connects
    @Override
    public void onConnect(WebSocket socket) {
            sockets.add(socket); // add socket to list
            lastSocket = socket;

            System.out.println("On connect");
            System.out.println(sockets.size());

            super.onConnect(socket); // WebSocketApplication.onConnect(socker)
    }


    public void onConnect(int userId) {
        System.out.println("User id on connect " + userId);
        // if key doesn't exist create it, if it exists replace it
        socketsMap.put(userId, lastSocket);
        System.out.println("map " + socketsMap.size());
    }

    // when a message is sent by a client
    @Override
    public void onMessage(WebSocket current, String text) { // client?, message text
        Recipe recipe = new Recipe();

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Convert text to recipe
            recipe = mapper.readValue(text, Recipe.class);
        }
        catch (JsonParseException e) {
            e.printStackTrace();
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        UsersController usersController = new UsersController();
        // Find user who created the recipe
        UserDTO user = usersController.getUser(recipe.getUserId());
        // Find followers who have sockets open
        List<UserFollowDTO> followers = usersController.getFollowers(recipe.getUserId()); // followers of the user who created the new recipe

        Recipe finalRecipe = recipe;
        followers.forEach(follower -> {
            int key = follower.getUser().getId();
            System.out.println("Followers");
            System.out.println("key " + key);

            if (socketsMap.containsKey(key) && socketsMap.get(key).isConnected()) {
                Notification notification = new Notification(user.getName() + " has posted a new recipe '" + finalRecipe.getName() + "'");

                String notificationAsString = "";
                try {
                    // convert notification object to json string and return it
                    notificationAsString = mapper.writeValueAsString(notification);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                // Send message to followers
                socketsMap.get(key).send(notificationAsString);
            }
        });
    }
}
