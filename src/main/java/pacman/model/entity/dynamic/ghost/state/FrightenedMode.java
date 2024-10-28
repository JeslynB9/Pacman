package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Vector2D;

public class FrightenedMode implements GhostModeState {
    GhostImpl ghost;
    @Override
    public Vector2D getTargetLocation(GhostImpl ghost, Vector2D playerPosition) {
        this.ghost = ghost;
        return ghost.getTargetCorner();
    }

    @Override
    public void enter(GhostImpl ghost) {
        ghost.setSpeed(ghost.getSpeeds().get("FRIGHTENED")); // Set speed specific to FRIGHTENED mode
    }

    @Override
    public GhostModeState nextState() {
        return new ScatterMode(); // Transition back to SCATTER mode
    }

}

