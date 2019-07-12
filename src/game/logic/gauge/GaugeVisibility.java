package game.logic.gauge;

import game.logic.config.Config;

public final class GaugeVisibility extends Gauge
{
    public GaugeVisibility() {
        super();
    }

    public GaugeVisibility(double level) {
        super(level);
    }

    public String toString() {
        if (Config.debug) {
            return super.toString() + " (Visibility)";
        }
        return "";
    }
}
