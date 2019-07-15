package evilcorp.logic.area;

import evilcorp.logic.config.Config;
import evilcorp.logic.area.region.Region;

public final class World extends Area
{
    /* Monde, singleton, contient le tableau avec toutes les régions du jeu et permet de faire
    la transition entre toutes les régions et le GameMaster, le GameMaster appelle les méthodes du World
    pour mettre à jour le jeu */

    private static World instance = null;

    public static World initWorld(Region[] regions) {
        if (instance == null) {
            instance = new World(regions);
        }
        return instance;
    }

    public static World getWorld() {
        return instance;
    }

    private final Region[] regions;

    private World(Region[] regions) {
        super("World");
        this.regions = regions;
    }

    public boolean isProductivityFull() { return productivity.isFull(); }
    public boolean isSocialFull() { return social.isFull(); }

    public int getRegionCount() { return regions.length; }
    public Region getRegion(int i) {
        if (i >= 0 && i < regions.length) return regions[i];
        return null;
    }
    public Region[] getRegions() { return regions; } // ?

    public void updateGauges() {
        double prodSum = 0;
        double visiSum = 0;
        double sociSum = 0;

        for (Region r: regions) {
            r.updateGauges();
            prodSum += r.getProductivity();
            visiSum += r.getVisibility();
            sociSum += r.getSocial();
        }

        setProductivity(prodSum / regions.length);
        setVisibility(visiSum / regions.length);
        setSocial(sociSum / regions.length);
    }

    public void updateRatePoints() {
        ratePoints = 0;
        for (Region r: regions) {
            r.updateRatePoints();
            ratePoints += r.getRatePoints();
        }
    }

    public void updateEvents() {
        for (Region r: regions) {
            r.updateEvents();
        }
    }

    public String toString() {
        if (Config.debug) {
            return "["+id+"] | [World] (P:"+getProductivity()+" V:"+getVisibility()+" S:"+getSocial()+") Points: "+getRatePoints();
        }
        return super.toString();
    }
}
