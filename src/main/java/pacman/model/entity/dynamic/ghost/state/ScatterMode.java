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

        System.out.println("Entering scatter mode for ghost: " + ghost);
//        System.out.println("Speeds map before access: " + ghost.getSpeeds());

        // Check if "SCATTER" key exists
        if (!ghost.getSpeeds().containsKey("SCATTER")) {
            System.out.println("Key 'SCATTER' does not exist in speeds map for ghost: " + ghost);
            return; // Return early if the key is not present
        }

        Double scatterSpeed = ghost.getSpeeds().get("SCATTER");

        // Log the retrieved speed
        if (scatterSpeed == null) {
            System.out.println("Scatter speed is not set for ghost: " + ghost);
            return; // Return early if scatterSpeed is null
        }

        System.out.println("Retrieved scatter speed: " + scatterSpeed);

        ghost.setSpeed(scatterSpeed);
        ghost.setImage(ghost.getNormalImage());

    }


    @Override
    public GhostModeState nextState() {
        return new ChaseMode(ghost.getMovementStrategy());
    }
}
