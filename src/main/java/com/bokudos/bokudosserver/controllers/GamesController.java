package com.bokudos.bokudosserver.controllers;

import com.bokudos.bokudosserver.entities.Game;
import com.bokudos.bokudosserver.services.GamesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
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

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Game>> getGames() {
        return ResponseEntity.ok(gamesService.getGames());
    }

    @GetMapping(
            path = "/{gameId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Game> getGame(@PathVariable(name = "gameId") UUID gameId) {
        return ResponseEntity.of(gamesService.getGameById(gameId));
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Game> addGame() {
        return ResponseEntity.of(gamesService.addGame());
    }

    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Game> updateGame(@Valid @RequestBody Game game) {
        return ResponseEntity.of(gamesService.updateGame(game));
    }

    @DeleteMapping(
            path = "/{gameId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity removeGame(@PathVariable(name = "gameId") UUID gameId) {
        gamesService.deleteGame(gameId);
        return ResponseEntity.ok("");
    }
}
