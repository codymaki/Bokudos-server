package com.bokudos.bokudosserver.services;

import com.bokudos.bokudosserver.dtos.GameDTO;
import com.bokudos.bokudosserver.entities.Game;
import com.bokudos.bokudosserver.enums.GameStatus;
import com.bokudos.bokudosserver.exceptions.GameNotFoundException;
import com.bokudos.bokudosserver.exceptions.InvalidGameStatusException;
import com.bokudos.bokudosserver.mappers.GameMapper;
import com.bokudos.bokudosserver.repositories.GamesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class GamesService {

    @Autowired
    private GamesRepository gamesRepository;

    public List<GameDTO> getGameDTOs() {
        return StreamSupport
                .stream(gamesRepository.findAll().spliterator(), false)
                .map(GameMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public GameDTO getGameDTOById(UUID gameId) {
        return GameMapper.mapToDTO(gamesRepository.findById(gameId)
                .orElseThrow(GameNotFoundException::new));
    }

    public Game getGameById(UUID gameId) {
        return gamesRepository.findById(gameId)
                .orElseThrow(GameNotFoundException::new);
    }

    public GameDTO addGame(GameDTO game) {
        if(game.getGameStatus() != GameStatus.CREATING) {
            throw new InvalidGameStatusException();
        }
        return saveGame(game);
    }

    public GameDTO updateGame(GameDTO gameDTO) {
        return saveGame(gameDTO);
    }

    private GameDTO saveGame(GameDTO gameDTO) {
        Game game = GameMapper.mapFromDTO(gameDTO);
        return GameMapper.mapToDTO(gamesRepository.save(game));
    }

    public void deleteGame(UUID gameId) {
        Game game = gamesRepository.findById(gameId)
                .orElseThrow(GameNotFoundException::new);
        gamesRepository.delete(game);
    }
}
