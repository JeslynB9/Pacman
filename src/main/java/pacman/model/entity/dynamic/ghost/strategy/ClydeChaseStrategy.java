package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Vector2D;

public class ClydeChaseStrategy implements ChaseMovementStrategy{
    @Override
    public Vector2D getTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Vector2D targetCorner, Vector2D blinkyPosition) {
        // Calculate the distance from Clyde to Pac-Man
        double distance = Vector2D.calculateEuclideanDistance(ghostPosition, playerPosition);

        // If more than 8 units away, target Pac-Man. Otherwise, target bottom-left corner
        if (distance > 8) {
            return playerPosition;
        } else {
            return targetCorner; // Clyde's scatter corner
        }
    }
}
