package com.bokudos.bokudosserver.repositories;

import com.bokudos.bokudosserver.entities.Game;
import com.bokudos.bokudosserver.entities.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PlayersRepository extends CrudRepository<Player, UUID> {

    Iterable<Player> findAllByGame(Game game);
}
