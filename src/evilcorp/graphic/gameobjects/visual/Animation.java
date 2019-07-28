package evilcorp.graphic.gameobjects.visual;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gfx.Image;

public class Animation extends GameObject
{
    private final Image[] images;
    private final double duration;

    private double delay;
    private double clock;

    private boolean active = false;

    public Animation(Engine engine, Image[] images, double duration) {
        super(engine);

        this.images = images;
        this.duration = duration;

        delay = images.length / duration;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }

    public void launch() { active = true; }
}
