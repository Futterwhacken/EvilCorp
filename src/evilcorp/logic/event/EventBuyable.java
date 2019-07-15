package evilcorp.logic.event;

import evilcorp.logic.config.Config;
import evilcorp.logic.area.region.Region;
import evilcorp.logic.NotificationBus;

public final class EventBuyable extends Event
{
    /* Event achetable par le joueur, souvent positif */

    private final int cost;
    private final double risk;

    private boolean bought = false;
    private boolean failed = false;

    public EventBuyable(String name, String description, String source,
                        int[] effectsParams, double[] effectsGauges, int duration,
                        int uses, Region region, int cost, double risk)
    {
        super(name, description, source, effectsParams, effectsGauges, duration, uses, region);
        this.cost = cost;
        this.risk = risk;
    }

    public Event clone() {
        return new EventBuyable(name, description, source,
            new int[]{effectsParams[0],effectsParams[1],effectsParams[2]},
            new double[]{effectsGauges[0],effectsGauges[1],effectsGauges[2]},
            duration, uses, region, cost, risk);
    }

    public int getCost() { return cost; }
    public double getRisk() { return risk; }

    public boolean isBought() { return bought; }
    public boolean hasFailed() { return failed; }

    public void buy() {
        bought = true; // s'active au prochain tour
    }

    public void handleTrigger() {
        if (bought) {
            triggered = true;

            if (Math.random() < risk) {
                NotificationBus.addWaitingNotif("Event failed:\n"+name+", "+region.getName());
                failed = true;
                // on inverse les effets
                for (int i = 0; i < 3; i++) {
                    effectsGauges[i] = -1 * effectsGauges[i];

                    switch (effectsParams[i]) {
                        case -1: break;
                        case 0: effectsParams[i] = 1;
                        case 1: effectsParams[i] = 2;
                        case 2: effectsParams[i] = 0;
                        case 3: effectsParams[i] = 4;
                        case 4: effectsParams[i] = 3;
                        default: break;
                    }
                }
            }
            else {
                NotificationBus.addWaitingNotif("Event succeeded:\n"+name+", "+region.getName());
            }
            bought = false;
        }
    }

    public String toString() {
        return super.toString()+", Cost: "+cost+", Risk: "+risk;
    }
}
