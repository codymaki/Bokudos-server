package com.bokudos.bokudosserver.physics;

import com.bokudos.bokudosserver.constants.PhysicsConstants;
import lombok.Getter;

/**
 * This class is used to store all physics related constants.
 * These settings are all configured or used as constants but may vary depending on the given games tick rate.
 */
@Getter
public class PhysicsSettings {
    private double msPerTick;
    private double gravityAcceleration;
    private double terminalVelocity;
    private double glidingVelocity;
    private double glidingVelocityMultiplier;
    private double tickRate;
    private double jumpSpeed;
    private double movementSpeed;
    private double movementAcceleration;

    public PhysicsSettings(double tickRate) {
        this.tickRate = tickRate;
        this.msPerTick = 1000 / tickRate;
        this.gravityAcceleration = PhysicsConstants.ACCELERATION_FROM_GRAVITY / tickRate;
        this.terminalVelocity = PhysicsConstants.STANDARD_TERMINAL_VELOCITY / tickRate;
        this.glidingVelocity = PhysicsConstants.STANDARD_GLIDING_VELOCITY / tickRate;
        this.glidingVelocityMultiplier = PhysicsConstants.STANDARD_GLIDING_MULTIPLIER;
        this.jumpSpeed = PhysicsConstants.DEFAULT_JUMP_SPEED / tickRate;
        this.movementSpeed = PhysicsConstants.DEFAULT_MOVEMENT_SPEED / tickRate;
        this.movementAcceleration = PhysicsConstants.DEFAULT_MOVEMENT_ACCELERATION / tickRate;
    }
}
