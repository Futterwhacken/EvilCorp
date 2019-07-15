package game.logic.gauge;

import game.logic.config.Config;

public final class GaugeSocial extends Gauge
{
    public GaugeSocial() {
        super();
    }

    public GaugeSocial(double level) {
        super(level);
    }

    public String toString() {
        if (Config.debug) {
            return super.toString() + " (Social)";
        }
        return "";
    }
}
