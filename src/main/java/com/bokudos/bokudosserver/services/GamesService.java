package com.bokudos.bokudosserver.services;

import com.bokudos.bokudosserver.entities.Game;
import com.bokudos.bokudosserver.enums.GameStatus;
import com.bokudos.bokudosserver.repositories.GamesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class GamesService {

    @Autowired
    private GamesRepository gamesRepository;

    public List<Game> getGames() {
        return StreamSupport
                .stream(gamesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Game> getGameById(UUID gameId) {
        return gamesRepository.findById(gameId);
    }

    public Optional<Game> addGame() {
        Game game = Game.builder()
                .gameId(UUID.randomUUID())
                .gameStatus(GameStatus.CREATING)
                .build();
        return saveGame(game);
    }

    public Optional<Game> updateGame(Game game) {
        return saveGame(game);
    }

    private Optional<Game> saveGame(Game game) {
        return Optional.of(gamesRepository.save(game));
    }

    public void deleteGame(UUID gameId) {
        Optional<Game> game = getGameById(gameId);
        game.ifPresent(value -> gamesRepository.delete(value));
    }
}
