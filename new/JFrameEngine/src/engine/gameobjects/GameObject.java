package engine.gameobjects;

import java.util.ArrayList;
import engine.Engine;

public abstract class GameObject
{
    private static int instanceCount = 0;
    private static ArrayList<GameObject> instances = new ArrayList<GameObject>();

    public static int getInstanceCount() { return instanceCount; }
    public static GameObject getElement(int id) { return instances.get(id); }

    protected final int id;
    protected final Engine engine;

    public GameObject(Engine engine) {
        this.engine = engine;
        this.id = instanceCount++;
        instances.add(this);
    }

    public final int getId() { return id; }

    public abstract void update();
    public abstract void render();
}
