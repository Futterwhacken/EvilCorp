package evilcorp.graphic.gameobjects.visual;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gfx.Image;

public class Animation extends GameObject
{
    // position is middle of animation
    private final int posX;
    private final int posY;

    private final Image[] images;
    private final double delay;

    private int currentImage;

    private double clock;

    private boolean active = false;

    public Animation(int posX, int posY, Image[] images, double duration) {
        super();

        if (posX == -1) {
            this.posX = engine.getWidth()/2;
        }
        else {
            this.posX = posX;
        }

        this.posY = posY;

        this.images = images;

        this.delay =  duration / images.length;

        currentImage = 0;
    }

    @Override
    public void update() {
        if (active) {
            if (currentImage >= images.length) {
                active = false;
            }
            else if (Engine.getTime() - clock >= delay) {
                clock = Engine.getTime();
                currentImage++;
            }
        }
    }

    @Override
    public void render() {
        if (active) {
            if (currentImage < images.length) { // fix this in update
                engine.getRenderer().drawImage(images[currentImage], posX - (images[currentImage].getWidth() / 2), posY - (images[currentImage].getHeight() / 2));
            }
        }
    }

    public void launch() {
        active = true;
        clock = Engine.getTime();
        currentImage = 0;
    }

    public boolean isFinished() {
        return currentImage >= images.length;
    }

    public boolean isActive() {
        return active;
    }
}
