package game.logic.gauge;

import game.logic.config.Config;
import game.logic.GameLogic;

public abstract class Gauge extends GameLogic
{
    /* Objet Jauge permettant de gérer les niveaux des trois jauges caractéristiques des différentes régions
    et du monde */
    
    private static final double MIN = Config.minGauge;
    private static final double MAX = Config.maxGauge;

    protected double level;

    protected Gauge() {
        this(MIN);
    }

    protected Gauge(double level) {
        super();
        this.level = level;
        update(0); // pour gérer effets de bord
    }

    public final double getLevel() { return level; }
    public final void setLevel(double level) { this.level = level; }
    public final boolean isFull() { return this.level == Gauge.MAX; }
    public final boolean isEmpty() { return this.level == Gauge.MIN; }

    public final void update(double amount) {
        level += amount;
        if (level > MAX) level = MAX;
        else if (level < MIN) level = MIN;
    }

    public String toString() {
        if (Config.debug) {
            return super.toString() + " | Gauge ["+MIN+"-"+MAX+"] : " + level;
        }
        return "";
    }
}
