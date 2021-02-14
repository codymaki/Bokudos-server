package com.bokudos.bokudosserver.physics;

import com.bokudos.bokudosserver.constants.PhysicsConstants;
import com.bokudos.bokudosserver.enums.AssetType;
import com.bokudos.bokudosserver.external.stagebuilder.Tiles;
import com.bokudos.bokudosserver.packets.in.PlayerUpdatePacket;
import com.bokudos.bokudosserver.packets.out.Animation;
import com.bokudos.bokudosserver.packets.out.PlayerAsset;
import com.bokudos.bokudosserver.packets.out.enums.Action;
import com.bokudos.bokudosserver.packets.out.enums.Direction;
import com.bokudos.bokudosserver.packets.out.enums.Movement;
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
        boolean attack = (packet != null && packet.getKeys() != null) && packet.getKeys().isAttack();
        boolean glide = (packet != null && packet.getKeys() != null) && packet.getKeys().isGlide();

        Velocity perTickVelocity = new Velocity(gameAsset.getDx() / settings.getTickRate(), gameAsset.getDy() / settings.getTickRate());

        if (up && !gameAsset.isJumping() && !gameAsset.isJumpUsed()) {
            perTickVelocity.setDy(perTickVelocity.getDy() + settings.getJumpSpeed());
        }
        if (!right && !left) {
            perTickVelocity.setDx(0.0D);
        } else if (right != left) {
            boolean movingRight = perTickVelocity.getDx() > 0.0D;
            double speed = right != movingRight && perTickVelocity.getDx() != 0 ? 0 : perTickVelocity.getDx()
                    + (right ? settings.getMovementAcceleration() : -settings.getMovementAcceleration());
            double maxSpeed = settings.getMovementSpeed() * (glide ? settings.getGlidingVelocityMultiplier() : 1.0D);
            perTickVelocity.setDx(
                    speed < 0 ? Math.max(speed, -maxSpeed)
                            : Math.min(speed, maxSpeed));
        }
        if(glide) {
            perTickVelocity.setDy(-settings.getGlidingVelocity());
        } else {
            if (perTickVelocity.getDy() > -settings.getTerminalVelocity()) {
                perTickVelocity.setDy(perTickVelocity.getDy() - settings.getGravityAcceleration());
            }
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
                ? 0.0D : updatedVelocity.getDx() * settings.getTickRate()));
        updatedAsset.setDy(CollisionDetectionUtilities.roundToTwoDecimals(perTickVelocity.getDy() != updatedVelocity.getDy()
                ? 0.0D : updatedVelocity.getDy() * settings.getTickRate()));

        // To remove repetitive jumping when key is held
        updatedAsset.setJumpUsed(up);
        updatedAsset.setJumping(updatedVelocity.getDy() != 0.0D);
        updatedAsset.setGliding(updatedVelocity.getDy() < 0 && glide);
        updatedAsset.setWidth(gameAsset.getWidth());
        updatedAsset.setHeight(gameAsset.getHeight());
        updatedAsset.setAnimation(getAnimation(gameAsset, attack));

        return updatedAsset;
    }

    private static Animation getAnimation(PlayerAsset playerAsset, boolean isAttacking) {
        Animation currentAnimation = playerAsset.getAnimation();
        if (currentAnimation.getTicks() < PhysicsConstants.FRAME_DELAY) {
            return new Animation(currentAnimation.getDirection(),
                    currentAnimation.getMovement(),
                    currentAnimation.getAction(),
                    currentAnimation.getFrame(),
                    currentAnimation.getTicks() + 1);
        }
        Action action = isAttacking || (currentAnimation.getAction() == Action.ATTACK
                && currentAnimation.getFrame() < PhysicsConstants.FRAMES_PER_ANIMATION - 1) ? Action.ATTACK : null;
        Animation animation = Animation.builder()
                .action(action)
                .movement(playerAsset.isGliding() ? Movement.GLIDE :
                        playerAsset.getDy() != 0.0D ? Movement.JUMP
                        : (playerAsset.getDx() == 0.0D ? Movement.IDLE : Movement.RUN))
                .direction(playerAsset.getDx() == 0.0D ? currentAnimation.getDirection()
                        : (playerAsset.getDx() > 0.0D ? Direction.RIGHT : Direction.LEFT))
                .build();
        int frame = currentAnimation.getAction() != action ? 0 : currentAnimation.getFrame() + 1;
        if (frame >= PhysicsConstants.FRAMES_PER_ANIMATION) {
            frame = frame % PhysicsConstants.FRAMES_PER_ANIMATION;
        }
        animation.setFrame(frame);
        return animation;
    }
}
