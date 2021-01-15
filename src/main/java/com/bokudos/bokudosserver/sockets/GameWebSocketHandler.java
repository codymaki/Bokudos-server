package com.bokudos.bokudosserver.sockets;

import com.bokudos.bokudosserver.entities.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GameWebSocketHandler extends AbstractWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Session Connected: " + session.getId());
        webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Message messageObject = objectMapper.readValue(message.getPayload(), Message.class);
        log.info("Message received: " + messageObject);
        for(WebSocketSession webSocketSession: webSocketSessions) {
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Session removed: " + session.getId());
        webSocketSessions.remove(session);
    }
}
