package coms309.TriviaBum.Websockets.HelpChat;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


import jakarta.websocket.OnOpen;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@ServerEndpoint("/help/queue/{username}")
@Component
public class HelpChatServer {

    private static Map<Session, String > sessionUsernameMap = new Hashtable< >();
    private static Map<Session, String > adminSessionUsernameMap = new Hashtable< >();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();
    private static Map < String, Session > adminUsernameSessionMap = new Hashtable < > ();
    private static Map < Session, Session > adminUserSessionMap = new Hashtable < > ();

    private static Queue<String> helpQueue = new ConcurrentLinkedQueue<>(); // Queue to maintain users waiting for help


    // server side logger
    private static Logger logger = LoggerFactory.getLogger(HelpChatServer.class);



    @OnOpen
    public void OnOpen(Session session, @PathParam ("username") String username) throws IOException{

        logger.info("Entered into Open");

        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        helpQueue.offer(username); // Add user to the help queue

        int queuePos = -1; // Initialize index to -1 (not found)
        for (String user : helpQueue) {
            queuePos += 1;
            if (user.equals(username)) {

                break; // Break the loop once the user's session is found
            }
        }
        if(queuePos == 1) {
            sendMessageToPArticularUser(username, "There is " + queuePos + " user ahead of you. Please wait until the next admin is available"); // Notify the user

        }
        else {
            sendMessageToPArticularUser(username, "There are " + queuePos + " users ahead of you. Please wait until the next admin is available"); // Notify the user
        }


    }

    @OnMessage
    public void OnMessage(Session session, String message) throws IOException {
//        logger.info("Entered into message: Got Message: " + message);

        String username = sessionUsernameMap.get(session);
        boolean adminSession = adminUserSessionMap.containsValue(session);

        if (adminSession) {
            // Admin is sending the message
            // Handle admin messages, e.g., broadcast to users or perform admin-specific actions

                AdminEndpoint.sendMessage(adminUsernameSessionMap.get(username),username + ": " + message);
                sendMessageToPArticularUser(username, "Me: " + message);

        }
        else if (username != null) {
            // Regular user is sending the message
            int queuePos = -1; // Initialize index to -1 (not found)
            for (String user : helpQueue) {
                queuePos += 1;
                if (user.equals(username)) {

                    break; // Break the loop once the user's session is found
                }
            }
            if(queuePos == 1) {
                sendMessageToPArticularUser(username, "There is " + queuePos + " user ahead of you. Please wait until the next admin is available"); // Notify the user

            }
            else {
                sendMessageToPArticularUser(username, "There are " + queuePos + " users ahead of you. Please wait until the next admin is available"); // Notify the user
            }
        }
    }

    @OnClose
    public void OnClose(Session session) throws IOException {

        logger.info("Entered into Close");

        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        helpQueue.remove(username);


        String message = username + " disconnected";

        broadcast(message);

    }

    private void sendMessageToPArticularUser(String username , String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }
    private void sendMessageToAdminUser(String username , String message) {
        try {
            adminUsernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private static void broadcast(String message) {
        adminSessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

    // Method to get the user at the top of the queue
    public static String getTopOfQueue() {
        return helpQueue.poll();
    }

//    @MessageMapping("/admin/connect/{adminId}")
//    public void connectAdminToNextUser(Session adminSession, @PathParam("adminId") String adminUsername) {
//        String nextUser = getTopOfQueue(); // Implement this method to get the next user from the queue
//        if (nextUser != null) {
//            Session userSession = usernameSessionMap.get(nextUser);
//            if (userSession != null) {
//                // Establish WebSocket connection between admin and user
//                sessionUsernameMap.put(adminSession, adminUsername);
//                sessionUsernameMap.put(userSession, nextUser);
//
//                // Notify admin and user about the connection
//                sendMessageToPArticularUser(adminUsername, "Connected to user: " + nextUser);
//                sendMessageToPArticularUser(nextUser, "Admin connected");
//            } else {
//                // Handle case where user session is not found (e.g., user left queue)
//                sendMessageToPArticularUser(adminUsername, "No user available");
//            }
//        } else {
//            // Handle case where queue is empty
//            sendMessageToPArticularUser(adminUsername, "Queue is empty");
//        }
//    }

    // WebSocket endpoint for admins to connect
    @ServerEndpoint("/help/admin/{adminId}")
    @Component
    public static class AdminEndpoint {

        String nextUser = null;
        @OnOpen
        public void onOpen(Session adminSession, @PathParam("adminId") String adminId) throws IOException {
            logger.info("Admin '{}' connected", adminId);
            nextUser = HelpChatServer.getTopOfQueue();
            if (nextUser != null) {
                Session userSession = usernameSessionMap.get(nextUser);
                if (userSession != null) {
                    // Establish WebSocket connection between admin and user
                    HelpChatServer.adminUserSessionMap.put(adminSession, userSession);
                    HelpChatServer.adminSessionUsernameMap.put(adminSession, nextUser);
                    HelpChatServer.adminSessionUsernameMap.put(userSession, adminId);
                    HelpChatServer.adminUsernameSessionMap.put(nextUser, adminSession);
                    HelpChatServer.usernameSessionMap.remove(userSession);
                    HelpChatServer.sessionUsernameMap.remove(nextUser);
                    // Notify admin and user about the connection
                    sendMessage(adminSession, "Connected to user: " + nextUser);
                    sendMessage(userSession, "Admin connected");
                } else {
                    sendMessage(adminSession, "No user available");
                }
            } else {
                sendMessage(adminSession, "Queue is empty");
            }
        }

        @OnClose
        public void onClose(Session adminSession) throws IOException {
            String adminId = adminSessionUsernameMap.get(adminSession);
            logger.info("Admin '{}' disconnected", adminId);

            adminSessionUsernameMap.remove(adminSession);
            adminSessionUsernameMap.remove(usernameSessionMap.get(nextUser));
            broadcast("Admin '" + adminId + "' disconnected");
        }


        @OnMessage
        public static void handleMessageFromAdmin(Session adminSession, String message) {
            // Retrieve user session associated with admin
            Session userSession = adminUserSessionMap.get(adminSession);

            // Broadcast message to user
            if (userSession != null) {
                sendMessage(userSession, "[Admin]: " + message);

            }
        }

       private static void sendMessage(Session session, String message) {
           try {
               session.getBasicRemote().sendText(message);
           } catch (IOException e) {
               logger.error("Error sending message: {}", e.getMessage());
           }
       }
    }
}


