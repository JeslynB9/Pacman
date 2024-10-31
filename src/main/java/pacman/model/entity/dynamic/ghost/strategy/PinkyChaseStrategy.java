package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Vector2D;

public class PinkyChaseStrategy implements ChaseMovementStrategy {
    @Override
    public Vector2D getTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Vector2D targetCorner, Vector2D blinkyPosition) {
        // Targets four spaces ahead of Pac-Man
        return playerPosition.add(new Vector2D(4, 0));
    }
}
