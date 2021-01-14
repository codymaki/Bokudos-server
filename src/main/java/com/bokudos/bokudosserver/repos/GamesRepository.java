package com.bokudos.bokudosserver.repos;

import com.bokudos.bokudosserver.data.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GamesRepository extends CrudRepository<Game, UUID> {
}
