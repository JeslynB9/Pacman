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
//            System.out.println("Speeds map is not set for ghost: " + ghost);
        }

        if (!ghost.getSpeeds().containsKey("CHASE")) {
//            System.out.println("Key 'CHASE' does not exist in speeds map for ghost: " + ghost);
            return;
        }

        Double chaseSpeed = ghost.getSpeeds().get("CHASE");

        if (chaseSpeed == null) {
//            System.out.println("Chase speed is not set for ghost: " + ghost);
            return;
        }

        ghost.setSpeed(chaseSpeed);
        ghost.setImage(ghost.getNormalImage());
    }

    @Override
    public GhostModeState nextState() {
        return new ScatterMode(); // Transition to SCATTER mode
    }
}
