package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface GhostModeState {
    Vector2D getTargetLocation(GhostImpl ghost, Vector2D playerPosition);
    void enter(GhostImpl ghost); // Called when entering this mode
    GhostModeState nextState(); // Defines the transition to the next mode
}
