package com.bokudos.bokudosserver.physics;

import com.bokudos.bokudosserver.packets.out.MovingObject;
import com.bokudos.bokudosserver.external.stagebuilder.v1.data.Tiles;
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
    public static MovingObject updatePosition(PhysicsSettings settings, MovingObject currentDTO, Tiles tiles) {
        Box box = Box.builder()
                .position(new Point(currentDTO.getX(), currentDTO.getY()))
                .dimensions(new Dimensions(currentDTO.getWidth(), currentDTO.getHeight()))
                .build();
        Velocity perTickVelocity = new Velocity(currentDTO.getDx()/settings.getTickRate(), currentDTO.getDy()/settings.getTickRate());
        if (perTickVelocity.getDy() > -settings.getTerminalVelocity()/settings.getTickRate()) {
            perTickVelocity.setDy(perTickVelocity.getDy() - settings.getGravityAcceleration()/settings.getTickRate());
        }

        Velocity updatedVelocity = CollisionDetectionUtilities.collideWithTiles(box, perTickVelocity, tiles);

        MovingObject updatedObject = new MovingObject();
        // when enemies run into walls, they just turn around and walk the other direction
        if(perTickVelocity.getDx() != updatedVelocity.getDx()) {
            updatedObject.setDx(CollisionDetectionUtilities.roundToTwoDecimals(-perTickVelocity.getDx()*settings.getTickRate()));
        } else {
            updatedObject.setDx(CollisionDetectionUtilities.roundToTwoDecimals(perTickVelocity.getDx()*settings.getTickRate()));
        }
        updatedObject.setDy(CollisionDetectionUtilities.roundToTwoDecimals(updatedVelocity.getDy()*settings.getTickRate()));
        updatedObject.setX(CollisionDetectionUtilities.roundToTwoDecimals(currentDTO.getX() + (updatedVelocity.getDx())));
        updatedObject.setY(CollisionDetectionUtilities.roundToTwoDecimals(currentDTO.getY() + (updatedVelocity.getDy())));
        updatedObject.setWidth(currentDTO.getWidth());
        updatedObject.setHeight(currentDTO.getHeight());

        return updatedObject;
    }

}
