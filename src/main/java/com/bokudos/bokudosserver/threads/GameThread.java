package com.bokudos.bokudosserver.threads;

import com.bokudos.bokudosserver.constants.ServerConfigurationConstants;
import com.bokudos.bokudosserver.dtos.GameDTO;
import com.bokudos.bokudosserver.dtos.PlayerPacketDTO;
import com.bokudos.bokudosserver.dtos.ServerPacketDTO;
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
    private BlockingQueue<PlayerPacketDTO> playerPacketQueue;
    private List<WebSocketSession> clientSessions;
    private ObjectMapper objectMapper;
    private String lastPacket;

    public GameThread(GameDTO gameDTO, BlockingQueue<PlayerPacketDTO> playerPacketQueue, List<WebSocketSession> clientSessions) {
        super(null, null, gameDTO.getGameId().toString(), 0);
        this.gameDTO = gameDTO;
        this.playerPacketQueue = playerPacketQueue;
        this.clientSessions = clientSessions;
        this.objectMapper = new ObjectMapper();
    }

    public void stopRunning() {
        this.running = false;
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();

        while (running) {
            if (System.currentTimeMillis() - timer > ServerConfigurationConstants.SERVER_DELAY_MS) {
                List<PlayerPacketDTO> packets = this.popPlayerPackets();
                ServerPacketDTO serverPacketDTO = this.tickServer(packets);
                final String serverPacket = writeToJSON(serverPacketDTO);
                if(serverPacket != null && !serverPacket.equals(lastPacket)) {
                    clientSessions.forEach(webSocketSession -> {
                        try {
                            webSocketSession.sendMessage(new TextMessage(serverPacket));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    lastPacket = serverPacket;
                }
                timer += ServerConfigurationConstants.SERVER_DELAY_MS;
            }
        }
        log.info("Game Thread Complete: " + gameDTO.getGameId());
    }

    private String writeToJSON(ServerPacketDTO serverPacketDTO) {
        try {
            return objectMapper.writeValueAsString(serverPacketDTO);
        } catch (JsonProcessingException e) {
            log.error("Failed to build JSON from Server Packet", e);
            return null;
        }
    }

    private ServerPacketDTO tickServer(List<PlayerPacketDTO> packets) {
        return ServerPacketDTO.builder().gameId(this.gameDTO.getGameId()).build();
    }

    private List<PlayerPacketDTO> popPlayerPackets() {
        int queueSize = playerPacketQueue.size();

        Map<UUID, PlayerPacketDTO> playerPacketDTOMap = new HashMap<>();
        for(int i = 0; i< queueSize; i++) {
            try {
                PlayerPacketDTO playerPacketDTO = playerPacketQueue.take();
                playerPacketDTOMap.put(playerPacketDTO.getPlayerId(), playerPacketDTO);
            } catch (InterruptedException ex) {
                log.error("Error getting player packets", ex);
            }
        }
        return new ArrayList<>(playerPacketDTOMap.values());
    }

}
