package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.strategy.ChaseMovementStrategy;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ChaseMode implements GhostModeState {
    private final ChaseMovementStrategy strategy;

    public ChaseMode(ChaseMovementStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Vector2D getTargetLocation(GhostImpl ghost, Vector2D playerPosition) {
        return strategy.getTargetLocation(playerPosition, ghost.getPosition(), ghost.getTargetCorner(), ghost.getBlinkyPosition());
    }

    @Override
    public void enter(GhostImpl ghost) {
        // Check if the speeds map is null
        if (ghost.getSpeeds() == null) {
            throw new IllegalStateException("Speeds map is not set for ghost: " + ghost);
        }

        System.out.println("Entering chase mode for ghost: " + ghost);
//        System.out.println("Speeds map before access: " + ghost.getSpeeds());

        // Check if "SCATTER" key exists
        if (!ghost.getSpeeds().containsKey("CHASE")) {
            System.out.println("Key 'CHASE' does not exist in speeds map for ghost: " + ghost);
            return; // Return early if the key is not present
        }

        Double chaseSpeed = ghost.getSpeeds().get("CHASE");

        // Log the retrieved speed
        if (chaseSpeed == null) {
            System.out.println("Chase speed is not set for ghost: " + ghost);
            return; // Return early if scatterSpeed is null
        }

        System.out.println("Retrieved chase speed: " + chaseSpeed);

        ghost.setSpeed(chaseSpeed);

    }

    @Override
    public GhostModeState nextState() {
        return new ScatterMode(); // Transition to SCATTER mode
    }
}
