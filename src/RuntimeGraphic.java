
import evilcorp.graphic.Engine;
import evilcorp.graphic.config.ConfigReader;
import evilcorp.graphic.gfx.Font;
import evilcorp.graphic.gfx.Image;
import evilcorp.graphic.gameobjects.*;
import evilcorp.graphic.gameobjects.interactive.*;
import evilcorp.graphic.gameobjects.text.*;
import evilcorp.graphic.gameobjects.visual.*;

import evilcorp.logic.GameMaster;
import evilcorp.logic.NotificationBus;
import evilcorp.logic.area.region.Region;
import evilcorp.logic.config.Config;

import java.io.IOException;
import java.lang.invoke.SwitchPoint;


public class RuntimeGraphic {
    private static Engine engine;
    private static GameMaster gm;

    private static final int menuColor = 0xff00ffff;
    private static final int menuAnchorX = 50;
    private static final int menuAnchorY = 335;
    private static final int menuFieldHeight = 20;

    private static final int regionInfoColor = 0xffffffff;
    private static final int regionInfoAnchorX = 50;
    private static final int regionInfoAnchorY = 100;
    private static final int regionInfoWidth = 8*40;
    private static final int regionInfoFieldHeight = 16;
    private static final int regionInfoMaxLines = 15;

    /*private static Menu buildAddExploitationMenu(String label) {
            String[] options = new String[]{
                    "add primary exploitation ("+Config.PrimaryCost+" $)",
                    "add secondary exploitation ("+Config.SecondaryCost+" $)",
                    "add tertiary exploitation ("+Config.TertiaryCost+" $)",
            };
            Action[] actions = new Action[]{
                    () -> engine.getSelectedRegion().buyExploitation(0),
                    () -> engine.getSelectedRegion().buyExploitation(1),
                    () -> engine.getSelectedRegion().buyExploitation(2),
            };

            return buildReturnableMenu(options, actions, label);
    }

    private static Menu buildRemoveExploitationMenu(String label) {
        String[] options = new String[engine.getSelectedRegion().getExploitations().size()];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length; i++) {
            int a = i;
            options[i] = engine.getSelectedRegion().getExploitations().get(i).toString()+" ("+engine.getSelectedRegion().getExploitations().get(i).getRemoveCost() + " $)";
            actions[i] = () -> {
                engine.getSelectedRegion().removeExploitation(a);
                engine.setCurrentMenu(buildRemoveExploitationMenu(label));
            };
        }
        return buildReturnableMenu(options, actions, label);
    }

    private static Menu buildBuyEventMenu(String label) {
        String[] options = new String[engine.getSelectedRegion().getBuyableEvents().size()];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length; i++) {
            int a = i;
            options[i] = engine.getSelectedRegion().getBuyableEvents().get(i).getName();
            actions[i] = () -> {
                if (engine.getSelectedRegion().buyEvent(engine.getSelectedRegion().getBuyableEvents().get(a)))
                    engine.setCurrentMenu(engine.getMainMenu());
            };
        }
        return buildReturnableMenu(options, actions, label);
    }

    private static Menu buildReturnableMenu(String[] options, Action[] actions, String label) {
        String[] finalOptions = new String[options.length + 2];
        Action[] finalActions = new Action[options.length + 2];

        for (int i = 0; i < options.length; i++) {
            finalOptions[i] = options[i];
            finalActions[i] = actions[i];
        }

        finalOptions[finalOptions.length - 2] = " ";
        finalActions[finalActions.length - 2] = () -> {};
        finalOptions[finalOptions.length - 1] = "return";
        finalActions[finalActions.length - 1] = () -> engine.setCurrentMenu(engine.getMainMenu());

        return new Menu(engine, menuAnchorX, menuAnchorY, menuFieldHeight, label, finalOptions, finalActions, menuColor);
    }*/

    public static void main(String[] args) {
        String currentPath = "/";
        try {
            currentPath = new java.io.File(".").getCanonicalPath() + currentPath;
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* LOADING CONFIG */

        gm = GameMaster.initGameMaster("data/config/logic/");
        engine = Engine.initEngine("data/config/graphic/");

        Font[] fonts = ConfigReader.readFonts("data/config/graphic/fonts.cfg", "data/resources/fonts/");
        engine.setStandardFont(fonts[0]);

        Map map = ConfigReader.readMap("data/config/graphic/map.cfg","data/resources/maps/");

        GameObject[] menuObjects = ConfigReader.readMenu("data/config/graphic/menu.cfg");
        Menu mainMenu = (Menu)menuObjects[0];
        TextArea hoverTextArea = (TextArea)menuObjects[1];


        /* APPLYING CONFIG */


        /* SETTING UP ENGINE */


        engine.addGameObject(map);

        engine.setMainMenu(mainMenu);
        engine.addGameObject(hoverTextArea);


        Text selectedRegionText = new Text(engine, "SELECT A REGION", regionInfoColor);
        selectedRegionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionText.setText(engine.getSelectedRegion().getName());
            else
                selectedRegionText.setText(selectedRegionText.getDefaultText());
        });

        Text selectedRegionActivityText = new Text(engine, regionInfoColor);
        selectedRegionActivityText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionActivityText.setText(engine.getSelectedRegion().getParams().getActivityToleranceString());
        });

        Text selectedRegionEnvironmentText = new Text(engine, regionInfoColor);
        selectedRegionEnvironmentText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionEnvironmentText.setText(engine.getSelectedRegion().getParams().getEnvironmentToleranceString());
        });

        Text selectedRegionOppositionText = new Text(engine, regionInfoColor);
        selectedRegionOppositionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionOppositionText.setText(engine.getSelectedRegion().getParams().getOppositionToleranceString());
        });

        Text selectedRegionProductionText = new Text(engine, regionInfoColor);
        selectedRegionProductionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionProductionText.setText("PRODUCTION: " + engine.getSelectedRegion().getProductivity());
        });

        Text selectedRegionPollutionText = new Text(engine, regionInfoColor);
        selectedRegionPollutionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionPollutionText.setText("POLLUTION: " + engine.getSelectedRegion().getVisibility());
        });

        Text selectedRegionSocialText = new Text(engine, regionInfoColor);
        selectedRegionSocialText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionSocialText.setText("SOCIAL: " + engine.getSelectedRegion().getSocial());
        });

        Text selectedRegionExploitationText = new Text(engine, regionInfoColor);
        selectedRegionExploitationText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionExploitationText.setText(""+engine.getSelectedRegion().getExploitations().size() + "/" + Region.getMaxExpl() + " EXPLOITATIONS");
        });

        TextArea regionInfo = new TextArea(engine, regionInfoAnchorX, regionInfoAnchorY, regionInfoWidth,regionInfoFieldHeight, regionInfoMaxLines,
                new Text[]{
                        selectedRegionText,
                        new Text(engine),
                        selectedRegionActivityText,
                        selectedRegionEnvironmentText,
                        selectedRegionOppositionText,
                        new Text(engine),
                        selectedRegionProductionText,
                        selectedRegionPollutionText,
                        selectedRegionSocialText,
                        new Text(engine),
                        selectedRegionExploitationText,
                });

        engine.addGameObject(regionInfo);


        Text turnsText = new Text(engine, 1065, 100, 0xffffffff);
        turnsText.setAction(() -> turnsText.setText("TURN: " + gm.getTurn()));

        Text fundsText = new Text(engine, 1065, 116, 0xffffffff);
        fundsText.setAction(() -> fundsText.setText("FUNDS: " + gm.getPoints() + " $"));

        Button nextTurnButton = new Button(engine, 1025, 148,
                () -> {
                    NotificationBus.clearWaitingList();
                    NotificationBus.clearImmediateList();
                    gm.nextTurn();
                },
                new Image("data/resources/images/next_turn.png"));

        engine.addGameObject(turnsText);
        engine.addGameObject(fundsText);
        engine.addGameObject(nextTurnButton);


        Visual logo = new Visual(engine, (int)(0.5*(engine.getWidth() - 252)), 600, new Image("data/resources/images/logo.png"));

        engine.addGameObject(logo);


        NotificationWaitingArea notificationWaitingArea = new NotificationWaitingArea(engine,850, 318, 8*50,14, 27, 0xffffffff);

        engine.addGameObject(notificationWaitingArea);

        NotificationImmediateArea notificationImmediateArea = new NotificationImmediateArea(engine,50, 510, 8*50,16, 3, 0xffff0000, 5);

        engine.addGameObject(notificationImmediateArea);


        int gaugeWidth = 300;
        int gaugePosX = (int)(0.5*(engine.getWidth() - gaugeWidth));
        int gaugePosY = 375;


        Text productionGaugeText = new Text(engine,gaugePosX - (11 * 8) - 2, gaugePosY, "PRODUCTION ", 0xffffffff);
        Text visibilityGaugeText = new Text(engine,gaugePosX - (10 * 8) - 2, gaugePosY+20, "POLLUTION ", 0xffffffff);
        Text socialGaugeText = new Text(engine,gaugePosX - (7 * 8) - 2, gaugePosY+40, "SOCIAL ", 0xffffffff);

        Text productionLevelText = new Text(engine,gaugePosX + gaugeWidth + 8 + 2, gaugePosY, 0xffffffff);
        productionLevelText.setAction(() -> productionLevelText.setText(""+gm.getWorld().getProductivity()));

        Text visibilityLevelText = new Text(engine,gaugePosX + gaugeWidth + 8 + 2, gaugePosY+20, 0xffffffff);
        visibilityLevelText.setAction(() -> visibilityLevelText.setText(""+gm.getWorld().getVisibility()));

        Text socialLevelText = new Text(engine,gaugePosX + gaugeWidth + 8 + 2, gaugePosY+40, 0xffffffff);
        socialLevelText.setAction(() -> socialLevelText.setText(""+gm.getWorld().getSocial()));

        GaugeGraph productionGauge = new GaugeGraph(engine, gaugePosX, gaugePosY-2, gaugeWidth, 11, true, Config.maxGauge, Config.maxGauge, 0xffff0000);
        productionGauge.setAction(() -> productionGauge.setLevel(gm.getWorld().getProductivity()));

        GaugeGraph visibilityGauge = new GaugeGraph(engine, gaugePosX, gaugePosY+20-2, gaugeWidth, 11, true, Config.maxGauge, Config.maxGauge, 0xff00ff00);
        visibilityGauge.setAction(() -> visibilityGauge.setLevel(gm.getWorld().getVisibility()));

        GaugeGraph socialGauge = new GaugeGraph(engine, gaugePosX, gaugePosY+40-2, gaugeWidth, 11, true, Config.maxGauge, Config.maxGauge, 0xff0000ff);
        socialGauge.setAction(() -> socialGauge.setLevel(gm.getWorld().getSocial()));

        engine.addGameObject(productionGaugeText);
        engine.addGameObject(visibilityGaugeText);
        engine.addGameObject(socialGaugeText);

        engine.addGameObject(productionLevelText);
        engine.addGameObject(visibilityLevelText);
        engine.addGameObject(socialLevelText);

        engine.addGameObject(productionGauge);
        engine.addGameObject(visibilityGauge);
        engine.addGameObject(socialGauge);



        engine.start();
    }
}

