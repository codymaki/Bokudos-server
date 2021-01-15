package com.bokudos.bokudosserver.mappers;

import com.bokudos.bokudosserver.dtos.PlayerDTO;
import com.bokudos.bokudosserver.entities.Player;

public class PlayerMapper {

    public static Player mapFromDTO(PlayerDTO playerDTO) {
        return Player.builder().playerId(playerDTO.getPlayerId()).name(playerDTO.getName()).build();
    }

    public static PlayerDTO mapToDTO(Player player) {
        return PlayerDTO.builder().playerId(player.getPlayerId()).name(player.getName()).build();
    }
}
