package evilcorp.graphic.gameobjects;

import java.util.ArrayList;
import evilcorp.graphic.Engine;

public abstract class GameObject
{
    private static int instanceCount = 0;
    private static final ArrayList<GameObject> instances = new ArrayList<>();

    public static int getInstanceCount() { return instanceCount; }
    public static GameObject getElement(int id) { return instances.get(id); }

    protected final int id;
    protected final Engine engine;

    public GameObject() {
        this.engine = Engine.getEngine();
        this.id = instanceCount++;
        instances.add(this);
    }

    public final int getId() { return id; }

    public abstract void update();
    public abstract void render();
}
