package game.logic;

import game.logic.config.Config;

import java.util.ArrayList;

public abstract class GameLogic
{
    /* Permet de garder traces des instances du moteur logique */
    
    private static int instanceCount = 0;
    private static ArrayList<GameLogic> instances = new ArrayList<GameLogic>();

    public final static int getInstanceCount() { return instanceCount; }
    public final static GameLogic getElement(int id) { return instances.get(id); }

    protected final int id;

    protected GameLogic() {
        this.id = instanceCount++;
        instances.add(this);
    }

    public final int getId() { return id; }

    public String toString() {
        if (Config.debug) {
            return "["+id+"]";
        }
        return "";
    }
}
