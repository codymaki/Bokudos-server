package com.bokudos.bokudosserver.controllers

import com.bokudos.bokudosserver.categories.IntegrationTest
import com.bokudos.bokudosserver.entities.Game
import com.bokudos.bokudosserver.enums.GameStatus
import com.bokudos.bokudosserver.utilities.IntegrationTestUtilities
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Category(IntegrationTest)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GamesControllerITSpec extends Specification {

    @Value('${api.url.games.v1}')
    private String gamesEndpoint;

    def "post game"() {
        given:
        HttpUriRequest request = new HttpGet(gamesEndpoint)

        when:
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request)

        then:
        httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()
//        Game game = IntegrationTestUtilities.retrieveResourceFromResponse(
//                httpResponse, Game)
//        game.gameId
//        game.gameStatus == GameStatus.CREATING
    }
}
