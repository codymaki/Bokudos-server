package com.bokudos.bokudosserver.controllers

import com.bokudos.bokudosserver.categories.UnitTest
import com.bokudos.bokudosserver.dtos.GameDTO
import com.bokudos.bokudosserver.entities.Game
import com.bokudos.bokudosserver.enums.GameStatus
import com.bokudos.bokudosserver.services.GamesService
import org.junit.experimental.categories.Category
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@Category(UnitTest)
class GamesControllerSpec extends Specification {

    GamesService gamesService = Mock(GamesService)
    GamesController gamesController

    def setup() {
        gamesController = new GamesController(gamesService)
    }

    def "GetGames"() {
        given:
        List<Game> games = [new Game(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)]

        when:
        ResponseEntity<List<Game>> responseEntity = gamesController.getGames()

        then:
        1 * gamesService.getGameDTOs() >> games
        0 * _

        and:
        responseEntity.getBody() == games
    }

    def "GetGame"() {
        given:
        UUID gameId = UUID.randomUUID()
        GameDTO game = new GameDTO(gameId: gameId, gameStatus: GameStatus.CREATING)

        when:
        ResponseEntity<GameDTO> responseEntity = gamesController.getGame(gameId)

        then:
        1 * gamesService.getGameDTOById(gameId) >> game
        0 * _

        and:
        responseEntity.getBody() == game
    }

    def "AddGame"() {
        given:
        GameDTO game = new GameDTO(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)
        GameDTO addedGame = new GameDTO(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)

        when:
        ResponseEntity<GameDTO> entityResponse = gamesController.addGame(game)

        then:
        1 * gamesService.addGame(game) >> addedGame
        0 * _

        and:
        entityResponse.getBody() == addedGame
    }

    def "UpdateGame"() {
        given:
        GameDTO game = new GameDTO(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)
        GameDTO updatedGame = new GameDTO(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)

        when:
        ResponseEntity<GameDTO> responseEntity = gamesController.updateGame(game)

        then:
        1 * gamesService.updateGame(game) >> updatedGame
        0 * _

        and:
        responseEntity.getBody() == updatedGame
    }

    def "RemoveGame"() {
        given:
        UUID gameId = UUID.randomUUID()

        when:
        ResponseEntity responseEntity = gamesController.removeGame(gameId)

        then:
        1 * gamesService.deleteGame(gameId)
        0 * _

        and:
        responseEntity.statusCode == HttpStatus.OK
    }
}
