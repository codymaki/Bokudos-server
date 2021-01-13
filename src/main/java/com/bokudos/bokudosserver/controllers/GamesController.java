package com.bokudos.bokudosserver.controllers;

import com.bokudos.bokudosserver.data.Game;
import com.bokudos.bokudosserver.services.GamesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "v1/games")
public class GamesController {

    private GamesService gamesService;

    @Autowired
    GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Game> getGame() {
        return ResponseEntity.of(gamesService.getGameById(UUID.randomUUID()));
    }
}
