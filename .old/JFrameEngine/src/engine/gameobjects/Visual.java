package engine.gameobjects;

import engine.Engine;
import engine.gfx.Image;

public class Visual extends GameObject
{
    private Image visual;
    private Action action;
    private int posX;
    private int posY;

    public Visual(Engine engine, Image visual, Action action, int posX, int posY) {
        super(engine);

        this.visual = visual;
        this.action = action;
        this.posX = posX;
        this.posY = posY;
    }

    public Visual(Engine engine, Image visual, int posX, int posY) {
        this(engine, visual, null, posX, posY);
    }

    @Override
    public void update() {
        if (action != null)
            action.exec();
    }

    @Override
    public void render() {
        engine.getRenderer().drawImage(visual, posX, posY);
    }

    public void setVisual(Image visual) { this.visual = visual; }
    public void setAction(Action action) { this.action = action; }
}
