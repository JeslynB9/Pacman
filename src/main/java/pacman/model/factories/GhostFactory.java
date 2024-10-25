package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.strategy.*;
import pacman.model.entity.dynamic.physics.*;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete renderable factory for Ghost objects
 */
public class GhostFactory implements RenderableFactory {

    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

    private static final Image BLINKY_IMAGE = new Image("maze/ghosts/blinky.png");
    private static final Image INKY_IMAGE = new Image("maze/ghosts/inky.png");
    private static final Image CLYDE_IMAGE = new Image("maze/ghosts/clyde.png");
    private static final Image PINKY_IMAGE = new Image("maze/ghosts/pinky.png");
    private static Image GHOST_IMAGE = BLINKY_IMAGE;
    private ChaseMovementStrategy strategy;
    private Vector2D targetCorner;

    // Changed to use int for ghost start positions
    private static final int BLINKY_START_X = 160;
    private static final int BLINKY_START_Y = 224;
    private static final int PINKY_START_X = 192;
    private static final int PINKY_START_Y = 224;
    private static final int INKY_START_X = 224;
    private static final int INKY_START_Y = 224;
    private static final int CLYDE_START_X = 256;
    private static final int CLYDE_START_Y = 224;

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Override
    public Renderable createRenderable(
            Vector2D position // assuming position is still Vector2D
    ) {
        try {
            Vector2D position1 = position.add(new Vector2D(4, -4)); // Assuming Vector2D accepts int values

            int x = (int) position.getX();
            int y = (int) position.getY();

            // Check against the integer start positions
            if (x == BLINKY_START_X && y == BLINKY_START_Y) {
                GHOST_IMAGE = BLINKY_IMAGE;
                strategy = new BlinkyChaseStrategy();
                targetCorner = new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP);
            } else if (x == PINKY_START_X && y == PINKY_START_Y) {
                GHOST_IMAGE = PINKY_IMAGE;
                strategy = new PinkyChaseStrategy();
                targetCorner = new Vector2D(0, TOP_Y_POSITION_OF_MAP);
            } else if (x == INKY_START_X && y == INKY_START_Y) {
                GHOST_IMAGE = INKY_IMAGE;
                strategy = new InkyChaseStrategy();
                targetCorner = new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP);
            } else if (x == CLYDE_START_X && y == CLYDE_START_Y) {
                GHOST_IMAGE = CLYDE_IMAGE;
                strategy = new ClydeChaseStrategy();
                targetCorner = new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP);
            } else {
                throw new IllegalArgumentException("Unknown ghost type: " + position);
            }

            // Create a bounding box using the new positions
            BoundingBox boundingBox = new BoundingBoxImpl(
                    position1,
                    GHOST_IMAGE.getHeight(),
                    GHOST_IMAGE.getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position1)
                    .build();

            return new GhostImpl(
                    GHOST_IMAGE,
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    targetCorner,
                    strategy);
        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }
}
