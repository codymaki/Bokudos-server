package com.bokudos.bokudosserver.mappers;

import com.bokudos.bokudosserver.dtos.GameDTO;
import com.bokudos.bokudosserver.entities.Game;

public class GameMapper {

    public static Game mapFromDTO(GameDTO gameDTO) {
        return Game.builder()
                .gameId(gameDTO.getGameId())
                .gameStatus(gameDTO.getGameStatus())
                .stageId(gameDTO.getStageId())
                .build();
    }

    public static GameDTO mapToDTO(Game game) {
        return GameDTO.builder()
                .gameId(game.getGameId())
                .gameStatus(game.getGameStatus())
                .stageId(game.getStageId())
                .build();
    }
}
