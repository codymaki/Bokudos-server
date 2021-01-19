package com.bokudos.bokudosserver.threads;

import com.bokudos.bokudosserver.dtos.GameDTO;
import com.bokudos.bokudosserver.dtos.PlayerPacketDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class GameThread extends Thread {

    private GameDTO gameDTO;
    private boolean running = true;
    private BlockingQueue<PlayerPacketDTO> playerPacketQueue;
    private List<WebSocketSession> clientSessions;

    public GameThread(GameDTO gameDTO, BlockingQueue<PlayerPacketDTO> playerPacketQueue, List<WebSocketSession> clientSessions) {
        this.gameDTO = gameDTO;
        this.playerPacketQueue = playerPacketQueue;
        this.clientSessions = clientSessions;
    }

    public void stopRunning() {
        this.running = false;
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();

        while (running) {
            if (System.currentTimeMillis() - timer > 1000) {
                log.info("Game " + gameDTO.getGameId() + ": " + timer);
                log.info("Queue Size: " + playerPacketQueue.size());
                final long tempTimer = timer;
                clientSessions.forEach(webSocketSession -> {
                    try {
                        webSocketSession.sendMessage(new TextMessage("Testing: " + tempTimer));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                timer += 1000;
            }
        }
        log.info("Game Thread Complete: " + gameDTO.getGameId());
    }

}
