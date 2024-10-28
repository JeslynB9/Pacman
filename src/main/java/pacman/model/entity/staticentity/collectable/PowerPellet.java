package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;

public class PowerPellet extends StaticEntityImpl implements Collectable {

    private final int points;
    private boolean isCollectable;
    private boolean isPowerPellet;

    public PowerPellet(BoundingBox boundingBox, Renderable.Layer layer, Image image, int points) {
        super(boundingBox, layer, image);
        this.points = points;
        this.isCollectable = true;
        this.isPowerPellet = true;
    }

    @Override
    public void collect() {
        this.isCollectable = false;
        setLayer(Renderable.Layer.INVISIBLE);
    }

    @Override
    public void reset() {
        this.isCollectable = true;
        setLayer(Renderable.Layer.BACKGROUND);
    }

    @Override
    public boolean isCollectable() {
        return this.isCollectable;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return this.points;
    }
}
