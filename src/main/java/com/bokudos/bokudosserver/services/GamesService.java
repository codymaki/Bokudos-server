package com.bokudos.bokudosserver.services;

import com.bokudos.bokudosserver.data.Game;
import com.bokudos.bokudosserver.data.GameStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class GamesService {

    public Optional<Game> getGameById(UUID gameId) {
        return Optional.of(new Game(gameId, GameStatus.CREATING));
    }
}
