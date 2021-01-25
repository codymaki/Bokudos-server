package com.bokudos.bokudosserver.physics;

import com.bokudos.bokudosserver.enums.AssetType;
import com.bokudos.bokudosserver.external.stagebuilder.Tiles;
import com.bokudos.bokudosserver.packets.in.PlayerUpdatePacket;
import com.bokudos.bokudosserver.packets.out.PlayerAsset;
import com.bokudos.bokudosserver.physics.data.Box;
import com.bokudos.bokudosserver.physics.data.Dimensions;
import com.bokudos.bokudosserver.physics.data.Point;
import com.bokudos.bokudosserver.physics.data.Velocity;
import com.bokudos.bokudosserver.utilities.CollisionDetectionUtilities;

public class PlayerPhysics {

    public static PlayerAsset updatePosition(PhysicsSettings settings, PlayerAsset gameAsset, Tiles tiles, PlayerUpdatePacket packet) {
        boolean up = (packet != null && packet.getKeys() != null) && packet.getKeys().isUp();
        boolean left = (packet != null && packet.getKeys() != null) && packet.getKeys().isLeft();
        boolean right = (packet != null && packet.getKeys() != null) && packet.getKeys().isRight();

        Velocity perTickVelocity = new Velocity(gameAsset.getDx()/settings.getTickRate(), gameAsset.getDy()/settings.getTickRate());

        if (up && !gameAsset.isJumping() && !gameAsset.isJumpUsed()) {
            perTickVelocity.setDy(perTickVelocity.getDy() + settings.getJumpSpeed());
        }
        if (right != left) {
            boolean movingRight = perTickVelocity.getDx() > 0.0D;
            double speed = right != movingRight && perTickVelocity.getDx() != 0 ? 0 : perTickVelocity.getDx()
                    + (right ? settings.getMovementAcceleration() : -settings.getMovementAcceleration());
            perTickVelocity.setDx(speed < 0 ? Math.max(speed, -settings.getMovementSpeed()) : Math.min(speed, settings.getMovementSpeed()));
        }
        if (perTickVelocity.getDy() > -settings.getTerminalVelocity()) {
            perTickVelocity.setDy(perTickVelocity.getDy() - settings.getGravityAcceleration());
        }

        Box box = Box.builder()
                .position(new Point(gameAsset.getX(), gameAsset.getY()))
                .dimensions(new Dimensions(gameAsset.getWidth(), gameAsset.getHeight()))
                .build();

        Velocity updatedVelocity = CollisionDetectionUtilities.collideWithTiles(box, perTickVelocity, tiles);

        PlayerAsset updatedAsset = new PlayerAsset();
        updatedAsset.setAssetType(AssetType.PLAYER);
        updatedAsset.setX(CollisionDetectionUtilities.roundToTwoDecimals(gameAsset.getX() + updatedVelocity.getDx()));
        updatedAsset.setY(CollisionDetectionUtilities.roundToTwoDecimals(gameAsset.getY() + updatedVelocity.getDy()));

        updatedAsset.setDx(CollisionDetectionUtilities.roundToTwoDecimals(perTickVelocity.getDx() != updatedVelocity.getDx()
                ? 0.0D : updatedVelocity.getDx()*settings.getTickRate()));
        updatedAsset.setDy(CollisionDetectionUtilities.roundToTwoDecimals(perTickVelocity.getDy() != updatedVelocity.getDy()
                ? 0.0D : updatedVelocity.getDy()*settings.getTickRate()));

        // To remove repetitive jumping when key is held
        updatedAsset.setJumpUsed(up);
        updatedAsset.setJumping(perTickVelocity.getDy() != updatedVelocity.getDy());

        return updatedAsset;
    }
}
