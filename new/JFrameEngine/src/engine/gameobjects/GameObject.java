package engine.gameobjects;

import engine.GameContainer;

public abstract class GameObject
{
    protected GameContainer gc;

    public GameObject(GameContainer gc) {
        this.gc = gc;
    }

    public abstract void update();
    public abstract void render();
}
