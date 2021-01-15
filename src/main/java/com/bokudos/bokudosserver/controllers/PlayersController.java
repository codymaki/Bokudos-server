package com.bokudos.bokudosserver.controllers;

import com.bokudos.bokudosserver.dtos.PlayerDTO;
import com.bokudos.bokudosserver.services.PlayersService;
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
@RequestMapping(path = "v1/games/{gameId}/players")
public class PlayersController {

    private PlayersService playersService;

    @Autowired
    PlayersController(PlayersService playersService) {
        this.playersService = playersService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<PlayerDTO>> getPlayers(@PathVariable(name = "gameId") UUID gameId) {
        return ResponseEntity.ok(playersService.getPlayersByGameId(gameId));
    }

    @GetMapping(
            path = "/{playerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable(name = "gameId") UUID gameId, @PathVariable(name = "playerId") UUID playerId) {
        return ResponseEntity.ok(playersService.getPlayerById(gameId, playerId));
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PlayerDTO> addPlayer(@PathVariable(name = "gameId") UUID gameId, @Valid @RequestBody PlayerDTO player) {
        return ResponseEntity.ok(playersService.addPlayer(gameId, player));
    }

    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable(name = "gameId") UUID gameId, @Valid @RequestBody PlayerDTO player) {
        return ResponseEntity.ok(playersService.updatePlayer(gameId, player));
    }

    @DeleteMapping(
            path = "/{playerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity removeGame(@PathVariable(name = "gameId") UUID gameId, @PathVariable(name = "playerId") UUID playerId) {
        playersService.deletePlayer(gameId, playerId);
        return ResponseEntity.ok("");
    }
}
