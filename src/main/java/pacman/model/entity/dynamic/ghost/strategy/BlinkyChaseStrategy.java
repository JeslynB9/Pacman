package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Vector2D;

public class BlinkyChaseStrategy implements ChaseMovementStrategy{
    @Override
    public Vector2D getTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Vector2D targetCorner, Vector2D blinkyPosition) {
        return playerPosition; // Targets Pac-Man directly
    }
}
