package com.bokudos.bokudosserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlayerPacketDTO {

    private UUID playerId;
    private KeysDTO keys;
}
