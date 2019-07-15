package evilcorp.logic.event;

import evilcorp.logic.config.Config;
import evilcorp.logic.area.region.Region;
import evilcorp.logic.NotificationBus;

public final class EventTriggerable extends Event
{
    /* Event déclenchable sous certaines conditions */

    /* conditions[0] gère les conditions sur la jauge de productivité
       conditions[1] gère les conditions sur la jauge de visibilité
       conditions[2] gère les conditions sur la jauge de social

       chaque case prend la valeur qu'il faut comparer avec la jauge en question */
    private final double[] conditions;

    /* conditionType exprime le type de comparaison qu'il faut opérer

       conditionType[0] gère la comparaison sur la jauge de productivité
       conditionType[1] gère la comparaison sur la jauge de visibilité
       conditionType[2] gère la comparaison sur la jauge de social

       chaque case peut prendre trois valeurs:
           0: comparaison jauge <= condition
           1: comparaison jauge == condition
           2: comparaison jauge >= condition */
    private final int[] conditionType;

    public EventTriggerable(String name, String description, String source,
                            int[] effectsParams, double[] effectsGauges, int duration,
                            int uses, Region region, double[] conditions, int[] conditionType)
    {
        super(name, description, source, effectsParams, effectsGauges, duration, uses, region);
        this.conditions = conditions;
        this.conditionType = conditionType;
    }

    public Event clone() {
        return new EventTriggerable(name, description, source,
            new int[]{effectsParams[0],effectsParams[1],effectsParams[2]},
            new double[]{effectsGauges[0],effectsGauges[1],effectsGauges[2]},
            duration, uses, region,
            new double[]{conditions[0],conditions[1],conditions[2]},
            new int[]{conditionType[0],conditionType[1],conditionType[2]});
    }

    public void handleTrigger() {
        if (triggered) return;

        boolean prodCond = false;
        boolean visiCond = false;
        boolean sociCond = false;

        switch (conditionType[0]) { // prod
            case 0:
                prodCond = (region.getProductivity() <= conditions[0]);
            break;

            case 1:
                prodCond = (region.getProductivity() == conditions[0]);
            break;

            case 2:
                prodCond = (region.getProductivity() >= conditions[0]);
            break;

            default: prodCond = false;
        }
        switch (conditionType[1]) { // visi
            case 0:
                visiCond = (region.getVisibility() <= conditions[1]);
            break;

            case 1:
                visiCond = (region.getVisibility() == conditions[1]);
            break;

            case 2:
                visiCond = (region.getVisibility() >= conditions[1]);
            break;

            default: visiCond = false;
        }
        switch (conditionType[2]) { // social
            case 0:
                sociCond = (region.getSocial() <= conditions[2]);
            break;

            case 1:
                sociCond = (region.getSocial() == conditions[2]);
            break;

            case 2:
                sociCond = (region.getSocial() >= conditions[2]);
            break;

            default: sociCond = false;
        }

        if (prodCond && visiCond && sociCond) {
            triggered = true;
            NotificationBus.addWaitingNotif("Event Triggered: \n"+name+"\n"+description+"\n"+duration+" turns,"+region.getName());
        }
    }

    public String toString() {
        if (Config.debug) {
            String s = super.toString() + "\n";
            s += "conditions: "+conditions[0]+", "+conditions[1]+", "+conditions[2]+"\n";
            s += "conditionType: "+conditionType[0]+", "+conditionType[1]+", "+conditionType[2]+"\n";
            return s;
        }
        return super.toString();
    }

}
