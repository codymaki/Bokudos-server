package com.bokudos.bokudosserver.threads;

import com.bokudos.bokudosserver.constants.ServerConfigurationConstants;
import com.bokudos.bokudosserver.dtos.GameDTO;
import com.bokudos.bokudosserver.enums.AssetType;
import com.bokudos.bokudosserver.external.stagebuilder.Tiles;
import com.bokudos.bokudosserver.packets.in.PlayerUpdatePacket;
import com.bokudos.bokudosserver.packets.out.Animation;
import com.bokudos.bokudosserver.packets.out.EnemyAsset;
import com.bokudos.bokudosserver.packets.out.PlayerAsset;
import com.bokudos.bokudosserver.packets.out.ServerUpdatePacket;
import com.bokudos.bokudosserver.packets.out.enums.Direction;
import com.bokudos.bokudosserver.packets.out.enums.Movement;
import com.bokudos.bokudosserver.physics.EnemyPhysics;
import com.bokudos.bokudosserver.physics.PhysicsSettings;
import com.bokudos.bokudosserver.physics.PlayerPhysics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * A game thread represents a single instance of a running game.
 * This thread keeps track of connected players, all game assets, and game tiles.
 * The game thread is primarily focused on updating assets on a cycle and sending those updated assets back to the clients.
 */
@Slf4j
public class GameThread extends Thread {

    private GameDTO gameDTO;
    private boolean running = true;
    private BlockingQueue<PlayerUpdatePacket> playerPacketQueue;
    private Map<UUID, PlayerUpdatePacket> playerUpdatePacketMap;
    private List<WebSocketSession> clientSessions;
    private ObjectMapper objectMapper;
    private String lastPacket;
    private Tiles tiles;
    private Map<UUID, EnemyAsset> enemies;
    private Map<UUID, PlayerAsset> players;
    private PhysicsSettings physicsSettings;

    public GameThread(GameDTO gameDTO, BlockingQueue<PlayerUpdatePacket> playerPacketQueue, List<WebSocketSession> clientSessions, Tiles tiles) {
        super(null, null, gameDTO.getGameId().toString(), 0);
        this.gameDTO = gameDTO;
        this.playerPacketQueue = playerPacketQueue;
        this.playerUpdatePacketMap = new HashMap<>();
        this.clientSessions = clientSessions;
        this.objectMapper = new ObjectMapper();
        this.tiles = tiles;
        this.enemies = new HashMap<>();
        this.players = new HashMap<>();
        this.physicsSettings = new PhysicsSettings(ServerConfigurationConstants.DEFAULT_SERVER_TICK_RATE);
    }

    public void stopRunning() {
        this.running = false;
    }

    /**
     * Run the game, this will continue to run the game until the thread is stopped using stopRunning method.
     * While running, the asset positions will be updated once per server tick and be sent to clients.
     */
    @Override
    public void run() {
        log.info("Game Thread Started: " + gameDTO.getGameId());
        long timer = System.currentTimeMillis();

        initEnemies();
        long enemySpawnRate = 500; // testing enemy spawn every 500ms
        long lastEnemySpawn = timer;
        long maxEnemies = 100;

        while (running) {
            long time = System.currentTimeMillis();
            // uncomment this code to test a bunch of enemies being spawned and moving across the map
//            if (time - lastEnemySpawn > enemySpawnRate && this.enemies.size() < maxEnemies) {
////                initEnemies();
////                lastEnemySpawn = time;
////            }
            if (time - timer > this.physicsSettings.getMsPerTick()) {
                initPlayers();
                Map<UUID, PlayerUpdatePacket> packets = this.popPlayerPackets();
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

                long processingTime = System.currentTimeMillis() - time;
                if (processingTime > this.physicsSettings.getMsPerTick()) {
                    log.warn("Server slowdown, time for processing one tick: " + processingTime);
                    double newTickRate = Math.max(this.physicsSettings.getTickRate() - ServerConfigurationConstants.SERVER_TICK_RATE_INTERVAL, ServerConfigurationConstants.MIN_SERVER_TICK_RATE);
                    log.warn("Decreasing Tick Rate from " + this.physicsSettings.getTickRate() + " to " + newTickRate);
                    this.physicsSettings = new PhysicsSettings(newTickRate);
                    timer = (long) (System.currentTimeMillis() - this.physicsSettings.getMsPerTick());
                }
            }
        }
        log.info("Game Thread Complete: " + gameDTO.getGameId());
    }

    /**
     * Here is where we initialize any assets for the game.
     */
    private void initEnemies() {
        this.enemies.put(UUID.randomUUID(), new EnemyAsset(AssetType.ENEMY, 19.0D, 28.0D, -2.4D, 0.0D, 1.0D, 1.0D));
    }

    /**
     * Initialize new players with random starting location.
     */
    private void initPlayers() {
        playerUpdatePacketMap.forEach(
                (k, v) -> {
                    if (!this.players.containsKey(k)) {
                        PlayerAsset playerAsset = PlayerAsset.builder()
                                .x(17) // .x(Math.random() * 50.0D + 20.0D)
                                .y(9) // .y(50.0D)
                                .height(2)
                                .width(1)
                                .animation(new Animation(Direction.RIGHT, Movement.IDLE, null, 0, 0))
                                .build();
                        this.players.put(k, playerAsset);
                    }
                }
        );
    }

    /**
     * Create a JSON string from the server packet with all consolidated game assets.
     *
     * @param serverUpdatePacket server packet with game assets
     * @return server packet as a JSON string
     */
    private String writeToJSON(ServerUpdatePacket serverUpdatePacket) {
        try {
            return objectMapper.writeValueAsString(serverUpdatePacket);
        } catch (JsonProcessingException e) {
            log.error("Failed to build JSON from Server Packet", e);
            return null;
        }
    }

    /**
     * Tick server and do all necessary updates to player and enemy positions.
     *
     * @param packets map of packets received from the players to indicate which actions they are attempting
     * @return consolidated packet of all game assets to be sent to clients
     */
    private ServerUpdatePacket tickServer(Map<UUID, PlayerUpdatePacket> packets) {
        enemies.replaceAll((i, v) -> EnemyPhysics.updatePosition(physicsSettings, v, tiles));
        players.replaceAll((i, v) -> PlayerPhysics.updatePosition(physicsSettings, v, tiles, packets.get(i)));

        return ServerUpdatePacket.builder()
                .gameId(gameDTO.getGameId())
                .enemies(enemies)
                .players(players)
                .build();
    }

    /**
     * Pops player packets from the stack. Consolidates packets into a map of one packet per user. The latest packet is used.
     * Packets are only sent from the client when something changes, so we do not want to remove the previous packet
     * that each user sent, and we still want to use that same packet for further processing.
     * <p>
     * This strategy will likely have to change in the future.
     *
     * @return map of user ids to packets
     */
    private Map<UUID, PlayerUpdatePacket> popPlayerPackets() {
        int queueSize = playerPacketQueue.size();
        for (int i = 0; i < queueSize; i++) {
            try {
                PlayerUpdatePacket playerUpdatePacket = playerPacketQueue.take();
                playerUpdatePacketMap.put(playerUpdatePacket.getPlayerId(), playerUpdatePacket);
            } catch (InterruptedException ex) {
                log.error("Error getting player packets", ex);
            }
        }
        return playerUpdatePacketMap;
    }

}
