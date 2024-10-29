package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.state.GhostModeState;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;

import java.util.Map;

/**
 * Represents Ghost entity in Pac-Man Game
 */
public interface Ghost extends DynamicEntity, PlayerPositionObserver {

    /***
     * Sets the speeds of the Ghost for each GhostMode
     * @param speeds speeds of the Ghost for each GhostMode
     */
    void setSpeeds(Map<String, Double> speeds);

    /***
     * Sets the mode lengths of the Ghost for each GhostMode
     * @param modeLengths mode lengths of the Ghost for each GhostMode
     */
    void setModeLengths(Map<String, Integer> modeLengths);

    /**
     * Sets the mode of the Ghost used to calculate target position
     *
     * @param ghostModeState mode of the Ghost
     */
    void setGhostMode(GhostModeState ghostModeState);

    void setImage(Image image);

    GhostModeState getGhostMode();

    void setLayer(Layer layer);
    void freeze();
}
