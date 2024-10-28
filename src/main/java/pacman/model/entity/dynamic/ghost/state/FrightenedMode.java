package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Vector2D;

public class FrightenedMode implements GhostModeState {
    GhostImpl ghost;
    @Override
    public Vector2D getTargetLocation(GhostImpl ghost, Vector2D playerPosition) {
        this.ghost = ghost;
        return ghost.getTargetCorner(); // MAKE GHOSTS MOVE RANDOM
    }

    @Override
    public void enter(GhostImpl ghost) {
        // Check if the speeds map is null
        if (ghost.getSpeeds() == null) {
            throw new IllegalStateException("Speeds map is not set for ghost: " + ghost);
        }

        System.out.println("Entering frightened mode for ghost: " + ghost);

        // Check if "FRIGHTENED" key exists
        if (!ghost.getSpeeds().containsKey("FRIGHTENED")) {
            System.out.println("Key 'FRIGHTENED' does not exist in speeds map for ghost: " + ghost);
            return; // Return early if the key is not present
        }

        Double frightenedSpeed = ghost.getSpeeds().get("FRIGHTENED");

        // Log the retrieved speed
        if (frightenedSpeed == null) {
            System.out.println("Frightened speed is not set for ghost: " + ghost);
            return; // Return early if frightenedSpeed is null
        }

        System.out.println("Retrieved frightened speed: " + frightenedSpeed);

        ghost.setSpeed(frightenedSpeed);
        ghost.setImage(ghost.getFrightenedImage());
    }

    @Override
    public GhostModeState nextState() {
        return new ScatterMode();
    }

}

