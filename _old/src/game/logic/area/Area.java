package game.logic.area;

import game.logic.config.Config;
import game.logic.GameLogic;
import game.logic.gauge.*;

public abstract class Area extends GameLogic
{
    /* Classe qui gère les région et le monde qui tous les deux demandent trois jauges et un taux de points */
    protected final String name;

    protected final GaugeProductivity productivity;
    protected final GaugeVisibility visibility;
    protected final GaugeSocial social;

    protected int ratePoints;

    public Area(String name) {
        super();
        this.name = name;

        this.productivity = new GaugeProductivity();
        this.social = new GaugeSocial();
        this.visibility = new GaugeVisibility();

        this.ratePoints = 0;
    }

    public abstract void updateGauges();
    public abstract void updateRatePoints();
    public abstract void updateEvents();

    public final String getName() { return name; }

    public final double getProductivity() { return productivity.getLevel(); }
    public final double getVisibility() { return visibility.getLevel(); }
    public final double getSocial() { return social.getLevel(); }

    public final void setProductivity(double amount) { productivity.setLevel(amount); }
    public final void setVisibility(double amount) { visibility.setLevel(amount); }
    public final void setSocial(double amount) { social.setLevel(amount); }

    public final void updateProductivity(double amount) { productivity.update(amount); }
    public final void updateVisibility(double amount) { visibility.update(amount); }
    public final void updateSocial(double amount) { social.update(amount); }

    public final int getRatePoints() { return ratePoints; }

    public String toString() {
        if (Config.debug) {
            return super.toString() + " | [Area] "+name+"(P:"+getProductivity()+", V:"+getVisibility()+", S:"+getSocial()+")";
        }
        return name+" (Production: "+getProductivity()+", Pollution: "+getVisibility()+", Social: "+getSocial()+")";
    }
}
