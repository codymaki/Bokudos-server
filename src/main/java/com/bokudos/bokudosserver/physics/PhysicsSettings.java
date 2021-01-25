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
    private double tickRate;

    public PhysicsSettings(double tickRate) {
        this.tickRate = tickRate;
        this.msPerTick = 1000 / tickRate;
        this.gravityAcceleration = PhysicsConstants.ACCELERATION_FROM_GRAVITY;
        this.terminalVelocity = PhysicsConstants.STANDARD_TERMINAL_VELOCITY;
    }
}
