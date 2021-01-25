package com.bokudos.bokudosserver.constants;

/**
 * Constants used for various physics calculations.
 * Note: All values should be in terms of time and units.
 * Nothing should be calculated on a per server tick basis.
 * This ensures that game acts similarly regardless of the server tick rate set on the server.
 * @see com.bokudos.bokudosserver.physics.PhysicsSettings for server tick rate specific settings
 */
public class PhysicsConstants {

    /**
     * In the future, we will probably want to have this vary by weight.
     */
    public static final double ACCELERATION_FROM_GRAVITY = 3.0D;

    /**
     * In the future, we may want to have this vary based on weight, or other attributes.
     */
    public static final double STANDARD_TERMINAL_VELOCITY = 45.0D;

    public static final double DEFAULT_JUMP_SPEED = 45.0D;
    public static final double DEFAULT_MOVEMENT_SPEED = 15.0D;
    public static final double DEFAULT_MOVEMENT_ACCELERATION = 1.2D;

}
