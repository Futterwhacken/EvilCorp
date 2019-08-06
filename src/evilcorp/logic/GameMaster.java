package evilcorp.logic;

import evilcorp.logic.config.*;
import evilcorp.logic.area.World;
import evilcorp.logic.area.region.*;
import evilcorp.logic.event.*;

public final class GameMaster extends GameLogic
{
    /* Classe permettant d'intéragir avec le moteur logique qui sera appelée par l'executable et le moteur graphique */
    /* On en a fait un Singleton */

    private static GameMaster instance = null;
    private static String configPath;

    public static GameMaster initGameMaster(String configPath) {
        if (instance == null) {
            instance = new GameMaster(configPath);
            GameMaster.configPath = configPath;
        }
        return instance;
    }

    public static void resetGameMaster() {
        instance = new GameMaster(configPath);
        NotificationBus.clearImmediateList();
        NotificationBus.clearWaitingList();
    }

    public static GameMaster getGameMaster() {
        return instance;
    }

    private final World world;

    private int turnCount;
    private int points;
    private int ratePoints; // gain de points par tour

    private GameMaster(String configPath) { // path relatif à l'endroit où est appelé GameMaster
        super();

        // on initialise le jeu en lisant les différents fichiers de config
        ConfigReader.readConfig(configPath+"config.cfg");
        Region[] regions = ConfigReader.readRegions(configPath+"regions.cfg");
        EventTriggerable[] eventsT = ConfigReader.readTriggerableEvents(configPath+"events_triggerable.cfg");
        EventBuyable[] eventsB = ConfigReader.readBuyableEvents(configPath+"events_buyable.cfg");

        // on link les events avec leur région
        for (Region r: regions) {
            for (EventTriggerable et: eventsT) {
                Event newE = et.clone();
                newE.setRegion(r);
                r.addEvent(newE);
            }
            for (EventBuyable eb: eventsB) {
                Event newE = eb.clone();
                newE.setRegion(r);
                r.addEvent(newE);
            }
        }

        // initilisation du monde à partir des régions, aussi un Singleton
        world = World.initWorld(regions);

        points = Config.startingPoints;
        ratePoints = 0;
        turnCount = 0;
    }

    public World getWorld() { return world; }
    public int getTurn() { return turnCount; }
    public int getPoints() { return points; }
    public int getRatePoints() { return ratePoints; }

    public int checkGameStatus() { // 0 = lost, 1 = normal, 2 = won
        if (world.isSocialFull()) return 0;
        if (world.isProductivityFull()) return 2;
        return 1;
    }

    public boolean spendPoints(int amount) {
        if (points >= amount) {
            points -= amount;
            return true;
        }
        return false;
    }

    public void nextTurn() {
        world.updateEvents();
        world.updateGauges();
        world.updateRatePoints();
        updatePoints();
        turnCount++;
    }

    private void updatePoints() {
        updateRatePoints();
        points += ratePoints;
    }

    private void updateRatePoints() {
        ratePoints = world.getRatePoints();
    }

    public String toString() {
        if (Config.debug) {
            return super.toString() + " | [GameMaster]";
        }
        return "";
    }
}
