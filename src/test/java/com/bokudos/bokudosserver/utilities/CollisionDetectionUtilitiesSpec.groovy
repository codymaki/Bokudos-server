package com.bokudos.bokudosserver.utilities

import com.bokudos.bokudosserver.categories.UnitTest
import com.bokudos.bokudosserver.external.stagebuilder.Tiles
import com.bokudos.bokudosserver.physics.data.Box
import com.bokudos.bokudosserver.physics.data.Dimensions
import com.bokudos.bokudosserver.physics.data.Point
import com.bokudos.bokudosserver.physics.data.Velocity
import org.junit.experimental.categories.Category
import spock.lang.Specification
import spock.lang.Unroll

@Category(UnitTest)
class CollisionDetectionUtilitiesSpec extends Specification {

    Tiles tiles

    /**
     * Building a tile grid that should look like the following, where x is a filled in tile and _ is an empty tile.
     * X___X
     * X___X
     * X___X
     * XXXXX
     * @return
     */
    def setup() {
        tiles = new Tiles()
        tiles.setTile(0, 0, true)
        tiles.setTile(0, 1, true)
        tiles.setTile(0, 2, true)
        tiles.setTile(0, 3, true)
        tiles.setTile(1, 0, true)
        tiles.setTile(2, 0, true)
        tiles.setTile(3, 0, true)
        tiles.setTile(4, 0, true)
        tiles.setTile(4, 1, true)
        tiles.setTile(4, 2, true)
        tiles.setTile(4, 3, true)
    }

    @Unroll
    def "CollideWithTiles"() {
        given:
        Point point = new Point(x: 2, y: 2)
        Dimensions dimensions = new Dimensions(width: 1, height: 1)
        Box hitbox = new Box(position: point, dimensions: dimensions)

        when:
        Velocity updatedVelocity = CollisionDetectionUtilities.collideWithTiles(hitbox, initialVelocity, tiles)

        then:
        0 * _

        and:
        updatedVelocity.dx == expectedVelocity.dx
        updatedVelocity.dy == expectedVelocity.dy

        where:
        initialVelocity             | expectedVelocity
        new Velocity(dx: 0, dy: -1) | new Velocity(dx: 0, dy: 0)
    }

    def "GetDetectionArea"() {
    }

    def "HasOverlap"() {
    }

    @Unroll
    def "GetTilesInDetectionArea"() {
        given:
        Box hitbox = new Box(position: position, dimensions: dimensions)

        when:
        List<Point> response = CollisionDetectionUtilities.getTilesInDetectionArea(hitbox, tiles)

        then:
        0 * _

        and:
        response.size() == expectedTileCount

        where:
        position              | dimensions                            | expectedTileCount
        new Point(x: 2, y: 2) | new Dimensions(width: 1, height: 1)   | 0
        new Point(x: 2, y: 2) | new Dimensions(width: 1, height: 1.5) | 2
    }

    def "RoundPosition"() {
    }

    def "RoundToTwoDecimals"() {
    }
}
