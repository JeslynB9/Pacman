package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;

public class PowerPellet extends Pellet {

    private static final int POWER_PELLET_POINTS = 50;

    public PowerPellet(BoundingBox boundingBox, Layer layer, Image image, int points) {
        super(boundingBox, layer, image, POWER_PELLET_POINTS);
    }

    @Override
    public void collect() {
        super.collect();
    }
}
