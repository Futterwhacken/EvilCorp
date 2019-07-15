package evilcorp.logic.gauge;

import evilcorp.logic.config.Config;

public final class GaugeProductivity extends Gauge
{
    public GaugeProductivity() {
        super();
    }

    public GaugeProductivity(double level) {
        super(level);
    }

    public String toString() {
        if (Config.debug) {
            return super.toString() + " (Productivity)";
        }
        return "";
    }
}
