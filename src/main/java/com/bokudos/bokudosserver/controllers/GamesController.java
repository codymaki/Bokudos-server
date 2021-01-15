package com.bokudos.bokudosserver.controllers;

import com.bokudos.bokudosserver.dtos.GameDTO;
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
    public ResponseEntity<List<GameDTO>> getGames() {
        return ResponseEntity.ok(gamesService.getGameDTOs());
    }

    @GetMapping(
            path = "/{gameId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GameDTO> getGame(@PathVariable(name = "gameId") UUID gameId) {
        return ResponseEntity.ok(gamesService.getGameDTOById(gameId));
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GameDTO> addGame(@Valid @RequestBody GameDTO gameDTO) {
        return ResponseEntity.ok(gamesService.addGame(gameDTO));
    }

    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GameDTO> updateGame(@Valid @RequestBody GameDTO gameDTO) {
        return ResponseEntity.ok(gamesService.updateGame(gameDTO));
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
