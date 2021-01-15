package com.bokudos.bokudosserver.services;

import com.bokudos.bokudosserver.dtos.PlayerDTO;
import com.bokudos.bokudosserver.entities.Game;
import com.bokudos.bokudosserver.entities.Player;
import com.bokudos.bokudosserver.enums.GameStatus;
import com.bokudos.bokudosserver.exceptions.InvalidGameStatusException;
import com.bokudos.bokudosserver.exceptions.PlayerNotFoundException;
import com.bokudos.bokudosserver.mappers.PlayerMapper;
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
        Game game = gamesService.getGameById(gameId);
        return StreamSupport
                .stream(playersRepository.findAllByGame(game).spliterator(), false)
                .map(PlayerMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO getPlayerById(UUID gameId, UUID playerId) {
        gamesService.getGameDTOById(gameId);
        Player player = playersRepository.findById(playerId)
                .orElseThrow(PlayerNotFoundException::new);
        return PlayerMapper.mapToDTO(player);
    }

    public PlayerDTO addPlayer(UUID gameId, PlayerDTO player) {
        return savePlayer(gameId, player);
    }

    public PlayerDTO updatePlayer(UUID gameId, PlayerDTO player) {
        return savePlayer(gameId, player);
    }

    private PlayerDTO savePlayer(UUID gameId, PlayerDTO playerDTO) {
        Game game = gamesService.getGameById(gameId);
        if(game.getGameStatus() != GameStatus.CREATING) {
            throw new InvalidGameStatusException();
        }
        Player player = PlayerMapper.mapFromDTO(playerDTO);
        player.setGame(game);
        return PlayerMapper.mapToDTO(playersRepository.save(player));
    }

    public void deletePlayer(UUID gameId, UUID playerId) {
        gamesService.getGameById(gameId);
        Player player = playersRepository.findById(playerId)
                .orElseThrow(PlayerNotFoundException::new);
        playersRepository.delete(player);
    }
}
