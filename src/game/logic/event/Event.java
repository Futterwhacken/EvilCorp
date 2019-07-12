package game.logic.event;

import game.logic.config.Config;
import game.logic.GameLogic;
import game.logic.area.region.Region;
import game.logic.NotificationBus;

public abstract class Event extends GameLogic
{
    protected final String name;
    protected final String description;
    protected final String source;

    /* effectsParams[0] gère l'effet sur le paramètre tActivity
       effectsParams[1] gère l'effet sur le paramètre tEnvironment
       effectsParams[2] gère l'effet sur le paramètre tOpposition

       chaque case peut prendre 6 valeurs:
           -1: ne rien faire
           0: set le paramètre à 0
           1: set le paramètre à 1
           2: set le paramètre à 2
           3: diminuer le paramètre de 1
           4: augmenter le paramètre de 1 */
    protected final int[] effectsParams;

    /* effectsGauges[0] gère l'effet sur la jauge de productivité
       effectsGauges[1] gère l'effet sur la jauge de visibilité
       effectsGauges[2] gère l'effet sur la jauge de social

       les cases prennent les valeurs de modification du niveau de la jauge */
    protected final double[] effectsGauges;

    protected final int duration; // nombre de tours que l'effet dure
    protected int remainingTurns;

    protected boolean triggered = false;

    protected int uses; // nombre d'utilisations possibles de l'event, -1 = infinite uses

    protected Region region;

    public Event(String name, String description, String source,
                int[] effectsParams, double[] effectsGauges,
                int duration, int uses, Region region)
    {
        super();

        this.name = name;
        this.description = description;
        this.source = source;

        this.effectsParams = effectsParams;
        this.effectsGauges = effectsGauges;

        this.duration = duration;
        this.remainingTurns = duration;

        this.uses = uses;
        this.region = region;
    }

    public abstract Event clone();
    public abstract void handleTrigger();

    public final String getName() { return name; }
    public final String getDescription() { return description; }
    public final String getSource() { return source; }
    public final int getDuration() { return duration; }
    public final int getRemainingTurns() { return remainingTurns; }
    public final int getUses() { return uses; }
    public final boolean isTriggered() { return triggered; }

    public final void setRegion(Region r) { this.region = r; }

    public final void handleEvent() {
        if (remainingTurns == 0) { // reset
            triggered = false;
            remainingTurns = duration;
            if (uses > 0) uses--;
            NotificationBus.addWaitingNotif("Event stopped:\n"+name+", "+region.getName());
        }

        if (uses != 0) {
            handleTrigger();
            if (triggered) {
                NotificationBus.addWaitingNotif("Event active:\n"+name+", "+region.getName());
                handleEffects(); // balancer un "effet actif"
                remainingTurns--;
            }
        }
    }

    protected final void handleEffects() {
        switch (effectsParams[0]) { // effets sur parametres
            case -1: break;

            case 0: region.getParams().setActivityTolerance(0);
            break;

            case 1: region.getParams().setActivityTolerance(1);
            break;

            case 2: region.getParams().setActivityTolerance(2);
            break;

            case 3: region.getParams().setActivityTolerance(region.getParams().getActivityTolerance() - 1);
            break;

            case 4: region.getParams().setActivityTolerance(region.getParams().getActivityTolerance() + 1);
            break;

            default: break;
        }
        switch (effectsParams[1]) { // effets sur parametres
            case -1: break;

            case 0: region.getParams().setEnvironmentTolerance(0);
            break;

            case 1: region.getParams().setEnvironmentTolerance(1);
            break;

            case 2: region.getParams().setEnvironmentTolerance(2);
            break;

            case 3: region.getParams().setEnvironmentTolerance(region.getParams().getEnvironmentTolerance() - 1);
            break;

            case 4: region.getParams().setEnvironmentTolerance(region.getParams().getEnvironmentTolerance() + 1);
            break;

            default: break;
        }
        switch (effectsParams[2]) { // effets sur parametres
            case -1: break;

            case 0: region.getParams().setOppositionTolerance(0);
            break;

            case 1: region.getParams().setOppositionTolerance(1);
            break;

            case 2: region.getParams().setOppositionTolerance(2);
            break;

            case 3: region.getParams().setOppositionTolerance(region.getParams().getOppositionTolerance() - 1);
            break;

            case 4: region.getParams().setOppositionTolerance(region.getParams().getOppositionTolerance() + 1);
            break;

            default: break;
        }

        // effets sur jauges
        region.updateProductivity(effectsGauges[0]);
        region.updateVisibility(effectsGauges[1]);
        region.updateSocial(effectsGauges[2]);
    }

    public String toString() {
        if (Config.debug) {
            String s = "name: " + name+"\ndescription: "+description+"\nsource: "+source;
            s += "\neffectsParams: "+effectsParams[0]+", "+effectsParams[1]+", "+effectsParams[2]+"\n";
            s += "effectsGauges: "+effectsGauges[0]+", "+effectsGauges[1]+", "+effectsGauges[2]+"\n";
            s += "duration: "+remainingTurns+"/"+duration;
            return s;
        }
        return name+"\n"+description+"\n"+remainingTurns+"/"+duration+", "+uses+" uses";
    }
}
