package com.bokudos.bokudosserver.physics;

import com.bokudos.bokudosserver.external.stagebuilder.Tiles;
import com.bokudos.bokudosserver.packets.out.EnemyAsset;
import com.bokudos.bokudosserver.physics.data.Box;
import com.bokudos.bokudosserver.physics.data.Dimensions;
import com.bokudos.bokudosserver.physics.data.Point;
import com.bokudos.bokudosserver.physics.data.Velocity;
import com.bokudos.bokudosserver.utilities.CollisionDetectionUtilities;

public class EnemyPhysics {

    /**
     * This method currently only supports the movement of BASIC_WALKER enemy.
     * @see com.bokudos.bokudosserver.enums.EnemyType
     */
    public static EnemyAsset updatePosition(PhysicsSettings settings, EnemyAsset gameAsset, Tiles tiles) {
        Box box = Box.builder()
                .position(new Point(gameAsset.getX(), gameAsset.getY()))
                .dimensions(new Dimensions(gameAsset.getWidth(), gameAsset.getHeight()))
                .build();
        Velocity perTickVelocity = new Velocity(gameAsset.getDx()/settings.getTickRate(), gameAsset.getDy()/settings.getTickRate());
        if (perTickVelocity.getDy() > -settings.getTerminalVelocity()) {
            perTickVelocity.setDy(perTickVelocity.getDy() - settings.getGravityAcceleration());
        }

        Velocity updatedVelocity = CollisionDetectionUtilities.collideWithTiles(box, perTickVelocity, tiles);

        EnemyAsset updatedAsset = new EnemyAsset();
        updatedAsset.setAssetType(gameAsset.getAssetType());
        // when enemies run into walls, they just turn around and walk the other direction
        if(perTickVelocity.getDx() != updatedVelocity.getDx()) {
            updatedAsset.setDx(CollisionDetectionUtilities.roundToTwoDecimals(-perTickVelocity.getDx()*settings.getTickRate()));
        } else {
            updatedAsset.setDx(CollisionDetectionUtilities.roundToTwoDecimals(perTickVelocity.getDx()*settings.getTickRate()));
        }
        updatedAsset.setDy(CollisionDetectionUtilities.roundToTwoDecimals(updatedVelocity.getDy()*settings.getTickRate()));
        updatedAsset.setX(CollisionDetectionUtilities.roundToTwoDecimals(gameAsset.getX() + (updatedVelocity.getDx())));
        updatedAsset.setY(CollisionDetectionUtilities.roundToTwoDecimals(gameAsset.getY() + (updatedVelocity.getDy())));
        updatedAsset.setWidth(gameAsset.getWidth());
        updatedAsset.setHeight(gameAsset.getHeight());

        return updatedAsset;
    }

}
