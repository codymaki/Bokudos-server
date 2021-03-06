package com.bokudos.bokudosserver.sockets;

import com.bokudos.bokudosserver.dtos.GameDTO;
import com.bokudos.bokudosserver.packets.in.PlayerUpdatePacket;
import com.bokudos.bokudosserver.external.stagebuilder.StageBuilder;
import com.bokudos.bokudosserver.external.stagebuilder.Tiles;
import com.bokudos.bokudosserver.services.GamesService;
import com.bokudos.bokudosserver.threads.GameThread;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GameWebSocketHandler extends AbstractWebSocketHandler {

    private final static int DEFAULT_QUEUE_SIZE = 1000;

    private final Map<UUID, List<WebSocketSession>> webSocketSessions = new ConcurrentHashMap<>();
    private final Map<UUID, GameThread> gameThreadMap = new ConcurrentHashMap<>();
    private final Map<UUID, BlockingQueue<PlayerUpdatePacket>> packetQueueMap = new ConcurrentHashMap<>();

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    GamesService gamesService;
    @Autowired
    StageBuilder stageBuilder;

    private UUID getGameIdFromURI(URI uri) {
        String path = uri != null ? uri.getPath() : null;
        String gameId = path != null ? path.substring(path.lastIndexOf('/') + 1) : null;
        assert gameId != null;
        return UUID.fromString(gameId);
    }

    private BlockingQueue<PlayerUpdatePacket> getPlayerPacketQueue(UUID gameId) {
        BlockingQueue<PlayerUpdatePacket> queue = packetQueueMap.get(gameId);
        if (queue == null) {
            queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
            packetQueueMap.put(gameId, queue);
        }
        return queue;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Session Connected: " + session.getId());

        UUID gameId = getGameIdFromURI(session.getUri());
        addGameSession(gameId, session);
        if (!gameThreadMap.containsKey(gameId)) {
            GameDTO gameDTO = gamesService.getGameDTOById(gameId);
            BlockingQueue<PlayerUpdatePacket> packetQueue = getPlayerPacketQueue(gameId);
            Tiles tiles = stageBuilder.getTiles(gameDTO.getStageId());
            GameThread gameThread = new GameThread(gameDTO, packetQueue, webSocketSessions.get(gameId), tiles);
            gameThreadMap.put(gameId, gameThread);
            gameThread.start();
        }
    }

    private void addGameSession(UUID gameId, WebSocketSession session) {
        List<WebSocketSession> sessions = webSocketSessions.get(gameId);
        if (sessions == null) {
            sessions = Collections.synchronizedList(new ArrayList<>());
        }
        sessions.add(session);
        webSocketSessions.put(gameId, sessions);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PlayerUpdatePacket playerUpdatePacket = objectMapper.readValue(message.getPayload(), PlayerUpdatePacket.class);
        log.debug("Message received: " + playerUpdatePacket);

        UUID gameId = getGameIdFromURI(session.getUri());
        getPlayerPacketQueue(gameId).add(playerUpdatePacket);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Session removed: " + session.getId());
        UUID gameId = getGameIdFromURI(session.getUri());
        webSocketSessions.get(gameId).remove(session);
        if (webSocketSessions.get(gameId).size() == 0) {
            gameThreadMap.get(gameId).stopRunning();
            gameThreadMap.remove(gameId);
            webSocketSessions.remove(gameId);
            packetQueueMap.remove(gameId);
            gamesService.deleteGame(gameId);
        }
    }
}
