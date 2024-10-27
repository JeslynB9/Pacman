package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPellet;

public class PelletFactory implements RenderableFactory {
    private static final Image PELLET_IMAGE = new Image("maze/pellet.png");
    private static final Image POWER_PELLET_IMAGE = new Image("maze/pellet.png");
    private static final int REGULAR_PELLET_POINTS = 10;
    private static final int POWER_PELLET_POINTS = 50;
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            int x = (int) position.getX();
            int y = (int) position.getY();

            if ((x == 16 && y == 64) || (x == 416 && y == 64) || (x == 16 && y == 512) || (x == 416 && y == 512)) {
                // Offset by (-8, -8) and double the size
                Vector2D powerPelletPosition = new Vector2D(position.getX() - 8, position.getY() - 8);
                BoundingBox boundingBox = new BoundingBoxImpl(
                        powerPelletPosition,
                        POWER_PELLET_IMAGE.getHeight() * 2,
                        POWER_PELLET_IMAGE.getWidth() * 2
                );
                return new PowerPellet(boundingBox, layer, POWER_PELLET_IMAGE, POWER_PELLET_POINTS);
            } else {
                // Regular pellet
                BoundingBox boundingBox = new BoundingBoxImpl(
                        position,
                        PELLET_IMAGE.getHeight(),
                        PELLET_IMAGE.getWidth()
                );
                return new Pellet(boundingBox, layer, PELLET_IMAGE, REGULAR_PELLET_POINTS);
            }
        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid pellet configuration | %s", e));
        }
    }
}
