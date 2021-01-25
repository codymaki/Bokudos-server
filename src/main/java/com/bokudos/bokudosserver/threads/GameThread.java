package com.bokudos.bokudosserver.threads;

import com.bokudos.bokudosserver.constants.ServerConfigurationConstants;
import com.bokudos.bokudosserver.dtos.GameDTO;
import com.bokudos.bokudosserver.packets.in.PlayerUpdatePacket;
import com.bokudos.bokudosserver.packets.out.MovingObject;
import com.bokudos.bokudosserver.packets.out.ServerUpdatePacket;
import com.bokudos.bokudosserver.external.stagebuilder.v1.data.Tiles;
import com.bokudos.bokudosserver.physics.EnemyPhysics;
import com.bokudos.bokudosserver.physics.PhysicsSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class GameThread extends Thread {

    private GameDTO gameDTO;
    private boolean running = true;
    private BlockingQueue<PlayerUpdatePacket> playerPacketQueue;
    private List<WebSocketSession> clientSessions;
    private ObjectMapper objectMapper;
    private String lastPacket;
    private Tiles tiles;
    private Map<UUID, MovingObject> enemies;
    private PhysicsSettings physicsSettings;

    public GameThread(GameDTO gameDTO, BlockingQueue<PlayerUpdatePacket> playerPacketQueue, List<WebSocketSession> clientSessions, Tiles tiles) {
        super(null, null, gameDTO.getGameId().toString(), 0);
        this.gameDTO = gameDTO;
        this.playerPacketQueue = playerPacketQueue;
        this.clientSessions = clientSessions;
        this.objectMapper = new ObjectMapper();
        this.tiles = tiles;
        this.enemies = new HashMap<>();
        this.physicsSettings = new PhysicsSettings(ServerConfigurationConstants.DEFAULT_SERVER_TICK_RATE);
    }

    public void stopRunning() {
        this.running = false;
    }

    @Override
    public void run() {
        log.info("Game Thread Started: " + gameDTO.getGameId());
        long timer = System.currentTimeMillis();

        initEnemies();

        while (running) {
            if (System.currentTimeMillis() - timer > this.physicsSettings.getMsPerTick()) {
                List<PlayerUpdatePacket> packets = this.popPlayerPackets();
                ServerUpdatePacket serverUpdatePacket = this.tickServer(packets);
                final String serverPacket = writeToJSON(serverUpdatePacket);
                if (serverPacket != null && !serverPacket.equals(lastPacket)) {
                    clientSessions.forEach(webSocketSession -> {
                        try {
                            webSocketSession.sendMessage(new TextMessage(serverPacket));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    lastPacket = serverPacket;
                }
                timer += this.physicsSettings.getMsPerTick();
            }
        }
        log.info("Game Thread Complete: " + gameDTO.getGameId());
    }

    private void initEnemies() {
        this.enemies.put(UUID.randomUUID(), new MovingObject(19.0D, 28.0D, -2.4D, 0.0D, 1.0D, 1.0D));
//        this.enemies.put(UUID.randomUUID(), new MovingObject(68.0D, 50.0D, 5D, 0.0D, 1.0D, 1.0D));
//        this.enemies.put(UUID.randomUUID(), new MovingObject(50.0D, 78.0D, -2D, 0.0D, 1.0D, 1.0D));
    }

    private String writeToJSON(ServerUpdatePacket serverUpdatePacket) {
        try {
            return objectMapper.writeValueAsString(serverUpdatePacket);
        } catch (JsonProcessingException e) {
            log.error("Failed to build JSON from Server Packet", e);
            return null;
        }
    }

    private ServerUpdatePacket tickServer(List<PlayerUpdatePacket> packets) {
        enemies.replaceAll((i, v) -> EnemyPhysics.updatePosition(physicsSettings, v, tiles));

        return ServerUpdatePacket.builder()
                .gameId(gameDTO.getGameId())
                .enemies(enemies)
                .build();
    }

    private List<PlayerUpdatePacket> popPlayerPackets() {
        int queueSize = playerPacketQueue.size();

        Map<UUID, PlayerUpdatePacket> playerPacketDTOMap = new HashMap<>();
        for (int i = 0; i < queueSize; i++) {
            try {
                PlayerUpdatePacket playerUpdatePacket = playerPacketQueue.take();
                playerPacketDTOMap.put(playerUpdatePacket.getPlayerId(), playerUpdatePacket);
            } catch (InterruptedException ex) {
                log.error("Error getting player packets", ex);
            }
        }
        return new ArrayList<>(playerPacketDTOMap.values());
    }

}
