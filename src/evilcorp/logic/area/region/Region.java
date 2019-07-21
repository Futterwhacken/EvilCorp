package evilcorp.logic.area.region;

import evilcorp.logic.config.Config;
import evilcorp.logic.GameMaster;
import evilcorp.logic.area.Area;
import evilcorp.logic.exploitation.*;
import evilcorp.logic.event.*;
import evilcorp.logic.NotificationBus;

import java.util.ArrayList;

public final class Region extends Area
{
    private static final int MAX_EXPL = Config.maxExpl; // maximum d'exploitations

    public static int getMaxExpl() { return MAX_EXPL; }

    // taux de variation des jauges
    private double rateProductivity;
    private double rateVisibility;
    private double rateSocial;

    private final Parameters params;

    private final ArrayList<Exploitation> exploitations;
    private final ArrayList<Event> events;

    private boolean exploitable;

    public Region(String name, Parameters params, boolean exploitable) {
        super(name);

        this.params = params;
        this.exploitable = exploitable;
        this.events = new ArrayList<>();
        this.exploitations = new ArrayList<>();
    }

    public Parameters getParams() { return params; }

    public void addEvent(Event e) { events.add(e); }

    public ArrayList<EventBuyable> getBuyableEvents() {
        ArrayList<EventBuyable> eb = new ArrayList<>();
        for (Event e: events) {
            if (e instanceof EventBuyable) {
                eb.add((EventBuyable)e);
            }
        }
        return eb;
    }

    public boolean buyEvent(EventBuyable e) {
        if (!e.isTriggered() && !e.isBought() && e.getUses() != 0) {
            if (GameMaster.getGameMaster().spendPoints(e.getCost())) {
                e.buy();
                NotificationBus.addImmediateNotif(e.getName()+" bought\nwill take effect next turn");
                return true;
            }
            else {
                NotificationBus.addImmediateNotif("Not enough funds");
                return false;
            }
        }
        NotificationBus.addImmediateNotif("Cannot buy "+e.getName());
        return false;
    }

    private void makeExploitable() { exploitable = true; }
    private void makeUnexploitable()  { exploitable = false; }
    public boolean isExploitable() { return exploitable; }

    public ArrayList<Exploitation> getExploitations() {
        return exploitations;
    }

    public boolean buyExploitation(int type) {
        if (exploitable && exploitations.size() < MAX_EXPL) {
            switch (type) {
                case 0:
                    if (GameMaster.getGameMaster().spendPoints(ExploitationPrimary.COST)) {
                        exploitations.add(new ExploitationPrimary());
                        NotificationBus.addImmediateNotif("Primary exploitation added to "+name);
                        return true;
                    }
                break;

                case 1:
                    if (GameMaster.getGameMaster().spendPoints(ExploitationSecondary.COST)) {
                        exploitations.add(new ExploitationSecondary());
                        NotificationBus.addImmediateNotif("Secondary exploitation added to "+name);
                        return true;
                    }
                break;

                case 2:
                    if (GameMaster.getGameMaster().spendPoints(ExploitationTertiary.COST)) {
                        exploitations.add(new ExploitationTertiary());
                        NotificationBus.addImmediateNotif("Tertiary exploitation added to "+name);
                        return true;
                    }
                break;

                default: break;
            }
        }
        if (!exploitable) {
            NotificationBus.addImmediateNotif(name+" blocked, cannot be exploited");
        }
        else if (exploitations.size() == MAX_EXPL) {
            NotificationBus.addImmediateNotif("Exploitation limit reached");
        }
        else {
            NotificationBus.addImmediateNotif("Not enough funds");
        }
        return false;
    }

    public boolean removeExploitation(int index) {
        if (index >= 0 && index < exploitations.size()) {
            if (GameMaster.getGameMaster().spendPoints(exploitations.get(index).getRemoveCost())) {
                exploitations.remove(index);
                if (social.isFull()) {
                    updateSocial(Config.strikeRecovery);
                    makeExploitable();
                }
                NotificationBus.addImmediateNotif("Exploitation removed");
                return true;
            }
        }
        NotificationBus.addImmediateNotif("Cannot remove exploitation");
        return false;
    }

    public void updateGauges() {
        updateRates();

        updateProductivity(rateProductivity);
        updateVisibility(rateVisibility);
        updateSocial(rateSocial);
    }

    public void updateRatePoints() {
        if (exploitable && exploitations.size() != 0) { // la région ne rapport des points que si elle est exploitable
            ratePoints = Config.modPoints * (int)getProductivity();
        }
        else {
            ratePoints = 0;
        }
    }

    public void updateEvents() {
        for (Event e: events) {
            e.handleEvent();
        }
    }

    private void updateRates() {
        if (exploitations.size() == 0) { // -1 pour la résilience
            rateProductivity = -1;
            rateVisibility = -1;
            rateSocial = -1;
            return;
        }
        else if (social.isFull()) {
            /* si le social est au max, la zone n'est plus exploitable,
            pour rendre exploitable à nouveau il faut enlever une exploitation */
            makeUnexploitable();
            rateSocial = 0;
            rateProductivity = -1;
            rateVisibility = -1;
            return;
        }

        rateProductivity = 0;
        rateVisibility = 0;
        rateSocial = 0;

        for (Exploitation e: exploitations) {
            // modifier taux de prod en fonction des exploitations
            if (e instanceof ExploitationPrimary) {
                rateProductivity += e.getRateProductivity() * params.getModActivityPrimary();
            }
            else if (e instanceof ExploitationSecondary) {
                rateProductivity += e.getRateProductivity() * params.getModActivitySecondary();
            }
            else if (e instanceof ExploitationTertiary) {
                rateProductivity += e.getRateProductivity() * params.getModActivityTertiary();
            }
            // taux de pollution n'est pas sensible aux modifieurs d'activité; aka pollution reste là même
            rateVisibility += e.getRateVisibility();
        }

        // productivité et pollution modifiés par normes environnementales
        rateProductivity = rateProductivity * params.getModEnvironment();
        rateVisibility = rateVisibility * params.getModEnvironment();

        // calcul social en fonction de la pollution
        if (getVisibility() < 25) {
            rateSocial = Config.scaleSocial0;
        }
        else if (getVisibility() < 50) {
            rateSocial = Config.scaleSocial1;
        }
        else if (getVisibility() < 75) {
            rateSocial = Config.scaleSocial2;
        }
        else {
            rateSocial = Config.scaleSocial3;
        }

        // application du modificateur d'opposition
        rateSocial = rateSocial * params.getModOpposition();

        rateProductivity = rateProductivity / Config.dampener;
        rateVisibility = rateVisibility / Config.dampener;
        rateSocial = rateSocial / Config.dampener;
    }

    public String toStringLight() {
        return super.toString()+" "+exploitations.size()+"/"+MAX_EXPL+" exploitations";
    }

    public String toString() {
        if (Config.debug) {
            return super.toString() +
            " | [Region] Params("+getParams().getActivityTolerance()+", "+getParams().getEnvironmentTolerance()+", "+getParams().getOppositionTolerance()+
            ") Rates("+rateProductivity+", "+rateVisibility+", "+rateSocial+") " +exploitations.size()+"/"+MAX_EXPL+" exploitations";
        }
        return super.toString()+" "+exploitations.size()+"/"+MAX_EXPL+" exploitations\n"+params;
    }
}
