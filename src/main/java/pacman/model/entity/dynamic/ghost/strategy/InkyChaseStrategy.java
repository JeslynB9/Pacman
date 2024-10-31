package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Vector2D;

public class InkyChaseStrategy implements ChaseMovementStrategy{
    @Override
    public Vector2D getTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Vector2D targetCorner, Vector2D blinkyPosition) {
        // Calculate two tiles ahead of Pac-Man (in his current direction)
        Vector2D twoTilesAhead = playerPosition.add(new Vector2D(2, 0));

        // Calculate vector from Blinky to two tiles ahead of Pac-Man
        Vector2D blinkyToAhead = twoTilesAhead.subtract(blinkyPosition);

        // Double the vector and return as Inky's target
        Vector2D target = twoTilesAhead.add(blinkyToAhead);

        return target;
    }
}
