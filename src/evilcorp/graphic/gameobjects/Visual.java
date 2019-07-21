package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gfx.Image;

public class Visual extends GameObject
{
    private Image image;
    private Action action;
    private final int posX;
    private final int posY;

    private Visual(Engine engine, int posX, int posY, Image image, Action action) {
        super(engine);

        this.image = image;
        this.action = action;
        this.posX = posX;
        this.posY = posY;
    }

    public Visual(Engine engine, int posX, int posY, Image image) {
        this(engine, posX, posY, image, null);
    }

    @Override
    public void update() {
        if (action != null)
            action.exec();
    }

    @Override
    public void render() {
        engine.getRenderer().drawImage(image, posX, posY);
    }

    public void setImage(Image image) { this.image = image; }
    public void setAction(Action action) { this.action = action; }
}
