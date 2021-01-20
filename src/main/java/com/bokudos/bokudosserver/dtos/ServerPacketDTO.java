package com.bokudos.bokudosserver.dtos;

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
public class ServerPacketDTO {

    private UUID gameId;
    private Map<UUID, ObjectDTO> players;
    private Map<UUID, ObjectDTO> enemies;
}
