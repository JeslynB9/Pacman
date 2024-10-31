package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.Maze;

import java.util.Random;

public class FrightenedMode implements GhostModeState {
    private Random random;
    GhostImpl ghost;
    @Override
    public Vector2D getTargetLocation(GhostImpl ghost, Vector2D playerPosition) {
        this.ghost = ghost;
        this.random = new Random();
        // If at an intersection, make random turning decisions
        if (Maze.isAtIntersection(ghost.getPossibleDirections())) {
            return getRandomDirection();
        } else {
            // Continue in the current direction
            return ghost.getKinematicState().getPosition();
        }
    }

    private Vector2D getRandomDirection() {
        // List of possible directions
        Direction[] directions = Direction.values();
        Direction randomDirection = directions[random.nextInt(directions.length)];

        // Ensure it's not the opposite of the current direction
        while (ghost.getDirection() != null && randomDirection == ghost.getDirection().opposite()) {
            randomDirection = directions[random.nextInt(directions.length)];
        }

        // Calculate potential new position based on random direction
        return ghost.getKinematicState().getPotentialPosition(randomDirection);
    }

    @Override
    public void enter(GhostImpl ghost) {
        // Check if the speeds map is null
        if (ghost.getSpeeds() == null) {
            throw new IllegalStateException("Speeds map is not set for ghost: " + ghost);
        }

        Double frightenedSpeed = ghost.getSpeeds().get("FRIGHTENED");

        ghost.setSpeed(frightenedSpeed);
        ghost.setImage(ghost.getFrightenedImage());
    }

    @Override
    public GhostModeState nextState() {
        return new ScatterMode();
    }

}

