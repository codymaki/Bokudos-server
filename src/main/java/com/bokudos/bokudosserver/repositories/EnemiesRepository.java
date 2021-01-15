package com.bokudos.bokudosserver.repositories;

import com.bokudos.bokudosserver.entities.Enemy;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EnemiesRepository extends CrudRepository<Enemy, UUID> {
}
