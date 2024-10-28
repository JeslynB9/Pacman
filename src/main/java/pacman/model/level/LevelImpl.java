package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.engine.observer.GameState;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.state.FrightenedMode;
import pacman.model.entity.dynamic.ghost.state.GhostModeState;
import pacman.model.entity.dynamic.ghost.state.ScatterMode;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.decorator.PoweredPacmanDecorator;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.PowerPellet;
import pacman.model.level.observer.LevelStateObserver;
import pacman.model.maze.Maze;

import javax.swing.text.Position;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Concrete implementation of Pac-Man level
 */
public class LevelImpl implements Level {

    private static final int START_LEVEL_TIME = 100;
    private final Maze maze;
    private final List<LevelStateObserver> observers;
    private List<Renderable> renderables;
    private Controllable player;
    private List<Ghost> ghosts;
    private int tickCount;
    private Map<String, Integer> modeLengths;
    private Map<String, Double> ghostSpeeds;
    private int numLives;
    private int points;
    private GameState gameState;
    private List<Renderable> collectables;
    private GhostModeState currentGhostMode;
    private int frightenedModeTickCount = 0;
    private Controllable originalPacman;
    private int ghostsEatenInFrightenedMode = 0;

    public LevelImpl(JSONObject levelConfiguration, Maze maze) {
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.observers = new ArrayList<>();
        this.modeLengths = new HashMap<>();
        this.ghostSpeeds = new HashMap<>();
        this.gameState = GameState.READY;
        this.currentGhostMode = new ScatterMode();
        this.points = 0;

        initLevel(new LevelConfigurationReader(levelConfiguration));
    }

    private void initLevel(LevelConfigurationReader levelConfigurationReader) {
        this.renderables = maze.getRenderables();

        if (!(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.originalPacman = (Controllable) maze.getControllable();
        this.player = originalPacman;
        this.player.setSpeed(levelConfigurationReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());

        this.ghosts = maze.getGhosts().stream()
                .map(element -> (Ghost) element)
                .collect(Collectors.toList());

        // Initialize ghost speeds
        ghostSpeeds = levelConfigurationReader.getGhostSpeeds();
        if (ghostSpeeds == null || ghostSpeeds.isEmpty()) {
            throw new ConfigurationParseException("Ghost speeds are not properly initialized");
        }

        // Initialize mode lengths for ghost states
        this.modeLengths = levelConfigurationReader.getGhostModeLengths();
        if (modeLengths == null || modeLengths.isEmpty()) {
            throw new ConfigurationParseException("Ghost mode lengths are not properly initialized");
        }

        // Set ghost speeds and modes
        for (Ghost ghost : this.ghosts) {
            ghost.setSpeeds(ghostSpeeds);
            ghost.setGhostMode(this.currentGhostMode);
            ghost.setModeLengths(modeLengths);
            player.registerObserver(ghost);
//            System.out.println("Ghost Mode Lengths Set: " + modeLengths);
//            System.out.println("Ghost Speeds Set: " + ghostSpeeds);
        }

        // Initialize collectables
        this.collectables = new ArrayList<>(maze.getPellets());
    }

    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream()
                .filter(e -> e instanceof DynamicEntity)
                .map(e -> (DynamicEntity) e)
                .collect(Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream()
                .filter(e -> e instanceof StaticEntity)
                .map(e -> (StaticEntity) e)
                .collect(Collectors.toList());
    }

    @Override
    public void tick() {
        if (this.gameState != GameState.IN_PROGRESS) {
            if (tickCount >= START_LEVEL_TIME) {
                setGameState(GameState.IN_PROGRESS);
                tickCount = 0;
            }
        } else {
            if (frightenedModeTickCount > 0) {
                // Decrement frightened mode counter
                frightenedModeTickCount--;
                if (frightenedModeTickCount == 0) {
                    // End Frightened mode and return to regular mode sequence
                    this.currentGhostMode = new ScatterMode();
                    tickCount = 0; // Reset tickCount for normal mode sequence

                    if (player instanceof PoweredPacmanDecorator) {
                        player = originalPacman; // Switch back to the original Pacman
                    }
                    for (Ghost ghost : this.ghosts) {
                        ghost.setGhostMode(this.currentGhostMode); // Switch to Scatter or Chase
                    }

                }
            } else {
                // Handle regular mode switching if not in Frightened mode
                handleGhostModeSwitch();
            }
            updatePlayerImage();
            updateDynamicEntities();
        }
        tickCount++;
    }


    private void handleGhostModeSwitch() {
        int currentModeLength = modeLengths.getOrDefault(currentGhostMode.getClass().getSimpleName(), 0);
        if (tickCount == currentModeLength) {
            this.currentGhostMode = currentGhostMode.nextState();
            for (Ghost ghost : this.ghosts) {
                ghost.setGhostMode(this.currentGhostMode); // Update each ghost's mode
            }
            tickCount = 0;
        }
    }

    private void updatePlayerImage() {
        if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
            this.player.switchImage();
        }
    }

    private void updateDynamicEntities() {
        List<DynamicEntity> dynamicEntities = getDynamicEntities();
        for (DynamicEntity dynamicEntity : dynamicEntities) {
            maze.updatePossibleDirections(dynamicEntity);
            dynamicEntity.update();
            handleCollisions(dynamicEntity, dynamicEntities);
        }
    }

    private void handleCollisions(DynamicEntity dynamicEntityA, List<DynamicEntity> dynamicEntities) {
        for (DynamicEntity dynamicEntityB : dynamicEntities) {
            if (dynamicEntityA != dynamicEntityB && dynamicEntityA.collidesWith(dynamicEntityB)) {
                if (isPlayer(dynamicEntityA) && dynamicEntityB instanceof Ghost ghost) {
                    System.out.println("Collision detected with ghost"); // Debug to confirm collision detection
                    // Use collideWith on player, which should be the PoweredPacmanDecorator
                    player.collideWith(this, ghost);
                } else {
                    dynamicEntityA.collideWith(this, dynamicEntityB);
                    dynamicEntityB.collideWith(this, dynamicEntityA);
                }
            }
        }

        for (StaticEntity staticEntity : getStaticEntities()) {
            if (dynamicEntityA.collidesWith(staticEntity)) {
                dynamicEntityA.collideWith(this, staticEntity);
                PhysicsEngine.resolveCollision(dynamicEntityA, staticEntity);
            }
        }
    }

    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        if (renderable instanceof PowerPellet) {
            frightenedModeTickCount = modeLengths.get("FrightenedMode");
            ghostsEatenInFrightenedMode = 0;
            for (Ghost ghost : ghosts) {
                ghost.setGhostMode(new FrightenedMode());
            }

            if (player instanceof Pacman) {
                player = new PoweredPacmanDecorator((Pacman) player);
                System.out.println("PoweredPacmanDecorator applied"); // Debug to confirm decorator
            }
        }
        return maze.getPellets().contains(renderable) && ((Collectable) renderable).isCollectable();
    }

    @Override
    public void collect(Collectable collectable) {
        this.points += collectable.getPoints();
        notifyObserversWithScoreChange(collectable.getPoints());
        this.collectables.remove(collectable);
    }

    @Override
    public void handleLoseLife() {
        if (gameState == GameState.IN_PROGRESS) {
            for (DynamicEntity dynamicEntity : getDynamicEntities()) {
                dynamicEntity.reset();
            }
            setNumLives(numLives - 1);
            setGameState(GameState.READY);
            tickCount = 0;
        }
    }

    @Override
    public void moveLeft() {
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public void registerObserver(LevelStateObserver observer) {
        this.observers.add(observer);
        observer.updateNumLives(this.numLives);
        observer.updateGameState(this.gameState);
    }

    @Override
    public void removeObserver(LevelStateObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserversWithNumLives() {
        for (LevelStateObserver observer : observers) {
            observer.updateNumLives(this.numLives);
        }
    }

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
        notifyObserversWithGameState();
    }

    @Override
    public void notifyObserversWithGameState() {
        for (LevelStateObserver observer : observers) {
            observer.updateGameState(gameState);
        }
    }

    public void notifyObserversWithScoreChange(int scoreChange) {
        for (LevelStateObserver observer : observers) {
            observer.updateScore(scoreChange);
        }
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
        notifyObserversWithNumLives();
    }

    @Override
    public void handleGameEnd() {
        this.renderables.removeAll(getDynamicEntities());
    }
}
