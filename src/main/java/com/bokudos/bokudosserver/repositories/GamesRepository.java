package com.bokudos.bokudosserver.repositories;

import com.bokudos.bokudosserver.entities.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GamesRepository extends CrudRepository<Game, UUID> {
}
