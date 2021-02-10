package com.bokudos.bokudosserver.packets.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlayerUpdatePacket {

    private UUID playerId;
    private PlayerKeys keys;
}
