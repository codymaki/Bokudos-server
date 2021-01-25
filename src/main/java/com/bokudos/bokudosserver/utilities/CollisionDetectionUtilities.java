package com.bokudos.bokudosserver.utilities;

import com.bokudos.bokudosserver.external.stagebuilder.v1.data.Tiles;
import com.bokudos.bokudosserver.physics.data.Box;
import com.bokudos.bokudosserver.physics.data.Dimensions;
import com.bokudos.bokudosserver.physics.data.Point;
import com.bokudos.bokudosserver.physics.data.Velocity;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains helper methods for applying collision detection to objects.
 * This was converted from a typescript class, so may still need additional refactoring and cleanup.
 */
public class CollisionDetectionUtilities {

    public static final int TILE_SIZE = 1;

    /**
     * Given a hitbox and a velocity, return the resulted velocity after colliding with any tiles.
     * @param hitbox hitbox of the moving character or other object
     * @param initialVelocity this contains the vertical and horizontal speed components (dx and dy)
     * @return updated velocity after physics from collided tiles have been applied to the initial velocity
     */
    public static Velocity collideWithTiles(Box hitbox, Velocity initialVelocity, Tiles tiles) {
        final Velocity velocity = new Velocity(initialVelocity.getDx(), initialVelocity.getDy());

        final Point topLeft = new Point(hitbox.getPosition().getX(), hitbox.getPosition().getY());
        final Point bottomRight = Point.builder()
                .x(hitbox.getPosition().getX() + hitbox.getDimensions().getWidth())
                .y(hitbox.getPosition().getY() - hitbox.getDimensions().getHeight())
                .build();

        final Box detectionBox = getDetectionArea(hitbox, initialVelocity);
        final List<Point> selectedTiles = getTilesInDetectionArea(detectionBox, tiles);
        double modification;
        for (Point tile: selectedTiles) {
            // if moving to the right, check tiles to the right of the hitbox
            if (velocity.getDx() > 0.0D) {
                // check the alignment of the box to ensure its in the same vertical space
                if (hasOverlap(bottomRight.getY(), topLeft.getY(), tile.getY(), tile.getY() + TILE_SIZE)) {
                    // if the box will be moved beyond the tile boundaries, then update the velocity based off of tile physics
                    if (hasOverlap(bottomRight.getX(), bottomRight.getX() + velocity.getDx(), tile.getX(), tile.getX() + TILE_SIZE)) {
                        modification = (bottomRight.getX() + velocity.getDx() - tile.getX());
                        velocity.setDx(velocity.getDx() - modification);
                    }
                }
            } else if (velocity.getDx() < 0.0D) {
                // check the alignment of the box to ensure its in the same vertical space
                if (hasOverlap(bottomRight.getY(), topLeft.getY(), tile.getY(), tile.getY() + TILE_SIZE)) {
                    // if the box will be moved beyond the tile boundaries, then update the velocity based off of tile physics
                    if (hasOverlap(topLeft.getX() + velocity.getDx(), topLeft.getX(), tile.getX(), tile.getX() + TILE_SIZE)) {
                        modification = (tile.getX() + TILE_SIZE) - (topLeft.getX() + velocity.getDx());
                        velocity.setDx(velocity.getDx() + modification);
                    }
                }
            }

            // if moving up, check tiles above the hitbox
            if (velocity.getDy() > 0.0D) {
                // check the alignment of the box to ensure its in the same horizontal space
                if (hasOverlap(topLeft.getX(), bottomRight.getX(), tile.getX(), tile.getX() + TILE_SIZE)) {
                    // if the box will be moved beyond the tile boundaries, then update the velocity based off of tile physics
                    if (hasOverlap(topLeft.getY(), topLeft.getY() + velocity.getDy(), tile.getY(), tile.getY() + TILE_SIZE)) {
                        modification = topLeft.getY() + velocity.getDy() - tile.getY();
                        velocity.setDy(velocity.getDy() - modification);
                    }
                }
            } else if (velocity.getDy() < 0.0D) {
                // check the alignment of the box to ensure its in the same horizontal space
                if (hasOverlap(topLeft.getX(), bottomRight.getX(), tile.getX(), tile.getX() + TILE_SIZE)) {
                    // if the box will be moved beyond the tile boundaries, then update the velocity based off of tile physics
                    if (hasOverlap(bottomRight.getY() + velocity.getDy(), bottomRight.getY(), tile.getY(), tile.getY() + TILE_SIZE)) {
                        modification = (tile.getY() + TILE_SIZE) - (bottomRight.getY() + velocity.getDy());
                        velocity.setDy(velocity.getDy() + modification);

                        // not sure if this is needed or not
                        if (Math.abs(velocity.getDy()) < 0.00001) {
                            velocity.setDy(0.0D);
                        }
                    }
                }
            }
        }

        return velocity;
    }

    /**
     * Get a box that is the original box combined with the given velocity.
     */
    public static Box getDetectionArea(Box box, Velocity velocity) {
        Point position = Point.builder()
                .x(box.getPosition().getX() + (velocity.getDx() > 0.0D ? 0 : velocity.getDx()))
                .y(box.getPosition().getY() + (velocity.getDy() > 0.0D ? velocity.getDy() : 0))
                .build();
        Dimensions dimensions = Dimensions.builder()
                .width(box.getDimensions().getWidth() + (Math.abs(velocity.getDx())))
                .height(box.getDimensions().getHeight() + (Math.abs(velocity.getDy())))
                .build();
        return Box.builder().position(position).dimensions(dimensions).build();
    }

    /**
     * Check to see if two ranges overlap
     * Requires range a to follow: a1 < a2
     * and range b to follow: b1 < b2
     */
    public static boolean hasOverlap(double a1, double a2, double b1, double b2) {
        return Math.max(a1, b1) < Math.min(a2, b2);
    }

    /**
     * Returns an array of tiles that are within the given box that are not empty tiles.
     * @param box
     */
    public static List<Point> getTilesInDetectionArea(Box box, Tiles tiles) {
        final List<Point> detectionTiles = new ArrayList<>();

        final int top = (int) Math.floor(box.getPosition().getY());
        final int bottom = (int) Math.floor(box.getPosition().getY() - box.getDimensions().getHeight());
        final int left = (int) Math.floor(box.getPosition().getX());
        final int right = (int) Math.floor(box.getPosition().getX() + box.getDimensions().getWidth());

        for (int row = bottom; row <= top + 1; row++) {
            for (int col = left; col <= right; col++) {
                Boolean tile = tiles.getTile(col, row);
                if(tile != null && tile) {
                    detectionTiles.add(new Point(col, row));
                }
            }
        }
        return detectionTiles;
    }

    /**
     * Round to 2 decimal places. This is a utility helper method that can be used to help resolve some of the precision issues with hitbox detection.
     */
    public static Point roundPosition(Point point) {
        return Point.builder()
                .x(roundToTwoDecimals(point.getX()))
                .y(roundToTwoDecimals(point.getY()))
                .build();
    }

    public static double roundToTwoDecimals(double x) {
        return Math.round(x * 100.0D) / 100.0D;
    }
}
