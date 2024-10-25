package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Vector2D;

public interface ChaseMovementStrategy {
    Vector2D getTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Vector2D targetCorner, Vector2D blinkyPosition);
}
