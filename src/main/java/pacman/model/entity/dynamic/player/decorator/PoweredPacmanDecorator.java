package pacman.model.entity.dynamic.player.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.state.FrightenedMode;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.level.Level;

import java.util.Set;

// Pacman decorator for enhanced abilities after eating a power pellet
public class PoweredPacmanDecorator implements Controllable {
    private final Controllable pacman;
    private int scoreMultiplier = 200; // Initial score for eating ghosts

    public PoweredPacmanDecorator(Controllable pacman) {
        this.pacman = pacman;
    }

    @Override
    public Image getImage() {
        return pacman.getImage();
    }

    @Override
    public double getWidth() {
        return pacman.getWidth();
    }

    @Override
    public double getHeight() {
        return pacman.getHeight();
    }

    @Override
    public void switchImage() {
        pacman.switchImage();
    }

    public Vector2D getPositionBeforeLastUpdate(){
        return pacman.getPositionBeforeLastUpdate();
    }

    @Override
    public Vector2D getPosition() {
        return pacman.getPosition();
    }

    @Override
    public Layer getLayer() {
        return pacman.getLayer();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return pacman.getBoundingBox();
    }

    @Override
    public void reset() {
        pacman.reset();
    }

    @Override
    public void setPosition(Vector2D position) {
        pacman.setPosition(position);
    }

    @Override
    public void update() {
        pacman.update();
    }

    @Override
    public void setSpeed(double speed) {
        pacman.setSpeed(speed);
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return pacman.collidesWith(renderable);
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        pacman.collideWith(level, renderable);
    }

    // Additional method for handling ghost collision while powered up
    public void collideWithGhost(Ghost ghost, Level level) {
        if (ghost.getGhostMode() instanceof FrightenedMode) {
//            level.addPoints(scoreMultiplier);
            scoreMultiplier *= 2; // Increase points for consecutive ghost eats
            ghost.reset(); // Respawn ghost in its starting position
        }
    }

    // Pass-through methods for movement
    @Override
    public void up() {
        pacman.up();
    }

    @Override
    public void down() {
        pacman.down();
    }

    @Override
    public void left() {
        pacman.left();
    }

    @Override
    public void right() {
        pacman.right();
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        pacman.setPossibleDirections(possibleDirections);
    }

    @Override
    public Direction getDirection() {
        return pacman.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return pacman.getCenter();
    }

    @Override
    public void registerObserver(PlayerPositionObserver observer) {
        pacman.registerObserver(observer);
    }

    @Override
    public void removeObserver(PlayerPositionObserver observer) {
        pacman.removeObserver(observer);
    }

    @Override
    public void notifyObservers() {
        pacman.notifyObservers();
    }

    public Controllable unwrap() {
        pacman.reset();
        return pacman;
    }
}

