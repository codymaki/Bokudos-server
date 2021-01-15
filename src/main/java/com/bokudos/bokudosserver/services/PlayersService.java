package com.bokudos.bokudosserver.services;

import com.bokudos.bokudosserver.dtos.PlayerDTO;
import com.bokudos.bokudosserver.entities.Game;
import com.bokudos.bokudosserver.entities.Player;
import com.bokudos.bokudosserver.enums.GameStatus;
import com.bokudos.bokudosserver.exceptions.InvalidGameStatusException;
import com.bokudos.bokudosserver.repositories.PlayersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class PlayersService {

    @Autowired
    private PlayersRepository playersRepository;
    @Autowired
    private GamesService gamesService;

    public List<PlayerDTO> getPlayersByGameId(UUID gameId) {
        Game game = gamesService.getGameById(gameId).orElseThrow();
        return StreamSupport
                .stream(playersRepository.findAllByGame(game).spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO getPlayerById(UUID gameId, UUID playerId) {
        gamesService.getGameById(gameId).orElseThrow();
        Player player = playersRepository.findById(playerId).orElseThrow();
        return mapToDTO(player);
    }

    public PlayerDTO addPlayer(UUID gameId, PlayerDTO player) {
        return savePlayer(gameId, player);
    }

    public PlayerDTO updatePlayer(UUID gameId, PlayerDTO player) {
        return savePlayer(gameId, player);
    }

    private PlayerDTO savePlayer(UUID gameId, PlayerDTO playerDTO) {
        Game game = gamesService.getGameById(gameId).orElseThrow();
        if(game.getGameStatus() != GameStatus.CREATING) {
            throw new InvalidGameStatusException();
        }
        Player player = mapFromDTO(playerDTO);
        player.setGame(game);
        return mapToDTO(playersRepository.save(player));
    }

    public void deletePlayer(UUID gameId, UUID playerId) {
        gamesService.getGameById(gameId).orElseThrow();
        Player player = playersRepository.findById(playerId).orElseThrow();
        playersRepository.delete(player);
    }

    public Player mapFromDTO(PlayerDTO playerDTO) {
        return Player.builder().playerId(playerDTO.getPlayerId()).name(playerDTO.getName()).build();
    }

    public PlayerDTO mapToDTO(Player player) {
        return PlayerDTO.builder().playerId(player.getPlayerId()).name(player.getName()).build();
    }
}
