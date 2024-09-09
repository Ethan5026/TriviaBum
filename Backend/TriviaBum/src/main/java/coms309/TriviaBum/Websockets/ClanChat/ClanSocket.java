package coms309.TriviaBum.Websockets.ClanChat;


import coms309.TriviaBum.Users.UserRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@ServerEndpoint("/clan/chat/{orgName}/{username}")
@Component
public class ClanSocket {

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();

    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private String orgName;




    // server side logger
    private static Logger logger = LoggerFactory.getLogger(ClanSocket.class);


    @OnOpen
    public void OnOpen(Session session,@PathParam("orgName") String orgName, @PathParam("username") String username) throws IOException {

        logger.info("Entered into Open");

        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        sendMessageToPArticularUser(username, "Welcome to your clan's chat! " + '"' + orgName + '"');
        broadcast(username + " has entered the chat.");
    }

    @OnMessage
    public void OnMessage(Session session, String message) throws IOException {
        logger.info("Entered into message: Got Message: " + message);
        String username = sessionUsernameMap.get(session);
        broadcast(username + ": " + message);
    }

    @OnClose
    public void OnClose(Session session) throws IOException {

        logger.info("Entered into Close");

        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        String message = username + " has left the chat.";

        broadcast(message);

    }

    private void sendMessageToPArticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private static void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}