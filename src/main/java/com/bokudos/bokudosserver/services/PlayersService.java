package com.bokudos.bokudosserver.services;

import com.bokudos.bokudosserver.entities.Game;
import com.bokudos.bokudosserver.entities.Player;
import com.bokudos.bokudosserver.repositories.PlayersRepository;
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
public class PlayersService {

    @Autowired
    private PlayersRepository playersRepository;
    @Autowired
    private GamesService gamesService;

    public List<Player> getPlayers() {
        return StreamSupport
                .stream(playersRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Player> getPlayerById(UUID playerId) {
        return playersRepository.findById(playerId);
    }

    public Optional<Player> addPlayer(UUID playerId, UUID gameId) {
        Game game = gamesService.getGameById(gameId).orElseThrow();
        Player player = new Player(playerId, game);
        return savePlayer(player);
    }

    public Optional<Player> updatePlayer(Player player) {
        return savePlayer(player);
    }

    private Optional<Player> savePlayer(Player player) {
        return Optional.of(playersRepository.save(player));
    }

    public void deletePlayer(UUID playerId) {
        Optional<Player> player = getPlayerById(playerId);
        player.ifPresent(value -> playersRepository.delete(value));
    }
}
