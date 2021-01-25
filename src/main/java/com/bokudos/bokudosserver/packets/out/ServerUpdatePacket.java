package com.bokudos.bokudosserver.packets.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServerUpdatePacket {

    private UUID gameId;
    private Map<UUID, MovingObject> players;
    private Map<UUID, MovingObject> enemies;
}
