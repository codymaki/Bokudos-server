package com.bokudos.bokudosserver.controllers

import com.bokudos.bokudosserver.categories.UnitTest
import com.bokudos.bokudosserver.entities.Game
import com.bokudos.bokudosserver.enums.GameStatus
import com.bokudos.bokudosserver.services.GamesService
import org.junit.experimental.categories.Category
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
        Game expectedResponse = Game.builder().gameId(UUID.randomUUID()).gameStatus(GameStatus.CREATING).build()

        when:
        ResponseEntity<Game> responseEntity = gamesController.getGame()

        then:
        1 * gamesService.getGameById(_ as UUID) >> Optional.of(expectedResponse)
        0 * _

        and:
        responseEntity.getBody() == expectedResponse
    }
}
