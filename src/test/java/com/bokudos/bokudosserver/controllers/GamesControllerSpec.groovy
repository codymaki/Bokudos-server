package com.bokudos.bokudosserver.controllers

import com.bokudos.bokudosserver.categories.UnitTest
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
        1 * gamesService.getGames() >> games
        0 * _

        and:
        responseEntity.getBody() == games
    }

    def "GetGame"() {
        given:
        UUID gameId = UUID.randomUUID()
        Game game = new Game(gameId: gameId, gameStatus: GameStatus.CREATING)

        when:
        ResponseEntity<Game> responseEntity = gamesController.getGame(gameId)

        then:
        1 * gamesService.getGameById(gameId) >> Optional.of(game)
        0 * _

        and:
        responseEntity.getBody() == game
    }

    def "AddGame"() {
        given:
        Game game = new Game(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)

        when:
        ResponseEntity<Game> entityResponse = gamesController.addGame()

        then:
        1 * gamesService.addGame() >> Optional.of(game)
        0 * _

        and:
        entityResponse.getBody() == game
    }

    def "UpdateGame"() {
        given:
        Game game = new Game(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)
        Game updatedGame = new Game(gameId: UUID.randomUUID(), gameStatus: GameStatus.CREATING)

        when:
        ResponseEntity<Game> responseEntity = gamesController.updateGame(game)

        then:
        1 * gamesService.updateGame(game) >> Optional.of(updatedGame)
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
