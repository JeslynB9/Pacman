package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ScatterMode implements GhostModeState {
    GhostImpl ghost;

    @Override
    public Vector2D getTargetLocation(GhostImpl ghost, Vector2D playerPosition) {
        return ghost.getTargetCorner(); // Each ghost has a unique scatter corner
    }

    @Override
    public void enter(GhostImpl ghost) {
        this.ghost = ghost;

        // Check if the speeds map is null
        if (ghost.getSpeeds() == null) {
            throw new IllegalStateException("Speeds map is not set for ghost: " + ghost);
        }

        Double scatterSpeed = ghost.getSpeeds().get("SCATTER");


        ghost.setSpeed(scatterSpeed);
        ghost.setImage(ghost.getNormalImage());

    }


    @Override
    public GhostModeState nextState() {
        return new ChaseMode(ghost.getMovementStrategy());
    }
}
