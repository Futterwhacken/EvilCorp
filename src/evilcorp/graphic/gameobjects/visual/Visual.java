package evilcorp.graphic.gameobjects.visual;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gfx.Image;

public class Visual extends GameObject
{
    private Image image;
    private Action action;
    private final int posX;
    private final int posY;

    public Visual(Engine engine, int posX, int posY, Image image) {
        super(engine);

        this.image = image;

        if (posX == -1) {
            this.posX = (int)(0.5*(engine.getWidth() - image.getWidth()));
        }
        else {
            this.posX = posX;
        }

        if (posY == -1) {
            this.posY = (int)(0.5*(engine.getHeight() - image.getHeight()));
        }
        else {
            this.posY = posY;
        }
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
