package com.bokudos.bokudosserver.controllers

import com.bokudos.bokudosserver.categories.IntegrationTest
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@Category(IntegrationTest)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GamesControllerITSpec extends Specification {

    @Autowired
    private GamesController gamesController
}
