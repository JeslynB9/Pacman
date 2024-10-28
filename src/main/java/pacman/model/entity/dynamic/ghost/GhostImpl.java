package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.state.GhostModeState;
import pacman.model.entity.dynamic.ghost.state.ScatterMode;
import pacman.model.entity.dynamic.ghost.strategy.ChaseMovementStrategy;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {

    private static final int minimumDirectionCount = 8;
    private final Layer layer = Layer.FOREGROUND;
    private final Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostModeState currentMode; // Use the State pattern for ghost modes
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private Map<String, Double> speeds = new HashMap<>(); // Speeds for each mode
    private Map<String, Integer> modeLengths = new HashMap<>(); // Speeds for each mode
    private int currentDirectionCount = 0;
    private final ChaseMovementStrategy movementStrategy;
    private long modeStartTime;

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, Vector2D targetCorner, ChaseMovementStrategy strategy) {
        this.image = image;
        this.speeds = getSpeeds();
        this.modeLengths = getModeLengths();
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.targetCorner = targetCorner;
        this.possibleDirections = new HashSet<>();
        this.movementStrategy = strategy;
        this.currentMode = new ScatterMode(); // Start in SCATTER mode
        this.currentMode.enter(this); // Initialize mode-specific settings
        this.modeStartTime = System.currentTimeMillis();
    }

    private void checkModeDuration() {
        String currentModeName = currentMode.getClass().getSimpleName();
        int modeDuration = modeLengths.getOrDefault(currentModeName, Integer.MAX_VALUE); // Get duration in seconds

        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - modeStartTime) / 1000; // Convert to seconds

        if (elapsedTime >= modeDuration) {
            System.out.println("Mode Switched");
            switchMode(); // Switch to the next mode
        }
    }

    @Override
    public void setSpeeds(Map<String, Double> speeds) {
        this.speeds = speeds;
    }
    public void setModeLengths(Map<String, Integer> modeLengths) {
        this.modeLengths = modeLengths;
    }

    public Map<String, Integer> getModeLengths() {
        return this.modeLengths;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void update() {
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());

        // Check if the mode time has expired
//        checkModeDuration();
    }

    private void updateDirection() {
        this.targetLocation = currentMode.getTargetLocation(this, playerPosition); // Use current mode

        if (Maze.isAtIntersection(this.possibleDirections)) {
            this.targetLocation = currentMode.getTargetLocation(this, playerPosition); // Use current mode
        }

        Direction newDirection = selectDirection(possibleDirections);
        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }

    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            currentDirectionCount++;
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void setGhostMode(GhostModeState newState) {
        this.currentMode = newState;
        this.currentMode.enter(this); // Apply settings specific to this mode
    }

    public void switchMode() {
        this.currentMode = currentMode.nextState();
        currentMode.enter(this); // Apply settings specific to the new mode
        this.modeStartTime = System.currentTimeMillis(); // Reset the timer
    }

    public void setSpeed(double speed) {
        this.kinematicState.setSpeed(speed);
    }


    public Map<String, Double> getSpeeds() {
        return this.speeds;
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            level.handleLoseLife();
        }
    }

    @Override
    public void update(Vector2D playerPosition) {
        this.playerPosition = playerPosition;
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(startingPosition);
        this.currentMode = new ScatterMode(); // Reset to SCATTER mode
        this.currentMode.enter(this); // Apply settings for SCATTER mode
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    public Vector2D getTargetCorner() {
        return targetCorner;
    }

    public Vector2D getBlinkyPosition() {
        // This method can be adjusted to retrieve Blinky's position if needed
        return null; // Placeholder for Blinky’s position
    }

    public ChaseMovementStrategy getMovementStrategy() {
        return this.movementStrategy;
    }
}
