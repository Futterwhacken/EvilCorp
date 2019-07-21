import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.*;


import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.interactive.Map;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.graphic.gameobjects.text.NotificationWaitingArea;
import evilcorp.graphic.gameobjects.text.Text;
import evilcorp.graphic.gameobjects.text.TextArea;
import evilcorp.graphic.gameobjects.visual.Visual;
import evilcorp.graphic.gfx.Image;
import evilcorp.logic.GameMaster;
import evilcorp.logic.NotificationBus;
import evilcorp.logic.area.region.Region;
import evilcorp.logic.exploitation.*;


public class RuntimeGraphic {
    private static Engine engine;
    private static GameMaster gm;

    private static final int menuColor = 0xff00ffff;
    private static final int menuAnchorX = 50;
    private static final int menuAnchorY = 335;
    private static final int menuFieldHeight = 21;

    private static final int regionInfoColor = 0xffffffff;
    private static final int regionInfoAnchorX = 50;
    private static final int regionInfoAnchorY = 100;
    private static final int regionInfoWidth = 8*40;
    private static final int regionInfoFieldHeight = 16;
    private static final int regionInfoMaxLines = 15;

    private static Menu buildAddExploitationMenu() {
            String[] options = new String[]{
                    "add primary exploitation ("+ExploitationPrimary.COST+" $)",
                    "add secondary exploitation ("+ExploitationSecondary.COST+" $)",
                    "add tertiary exploitation ("+ExploitationTertiary.COST+" $)",
            };
            Action[] actions = new Action[]{
                    () -> engine.getSelectedRegion().buyExploitation(0),
                    () -> engine.getSelectedRegion().buyExploitation(1),
                    () -> engine.getSelectedRegion().buyExploitation(2),
            };

            return buildReturnableMenu(options, actions);
    }

    private static Menu buildRemoveExploitationMenu() {
        String[] options = new String[engine.getSelectedRegion().getExploitations().size()];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length; i++) {
            int a = i;
            options[i] = engine.getSelectedRegion().getExploitations().get(i).toString()+" ("+engine.getSelectedRegion().getExploitations().get(i).getRemoveCost() + " $)";
            actions[i] = () -> {
                engine.getSelectedRegion().removeExploitation(a);
                engine.setCurrentMenu(buildRemoveExploitationMenu());
            };
        }
        return buildReturnableMenu(options, actions);
    }

    private static Menu buildBuyEventMenu() {
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
        return buildReturnableMenu(options, actions);
    }

    private static Menu buildReturnableMenu(String[] options, Action[] actions) {
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

        return new Menu(engine, menuAnchorX, menuAnchorY, menuFieldHeight, finalOptions, finalActions, menuColor);
    }

    public static void main(String[] args) {

        // pass game master to engine (get region -> ???)
        gm = GameMaster.initGameMaster("data/config/logic/");
        engine = new Engine("", gm);

        Map map = new Map(engine, 370, 195, 1.75,
                new String[]{
                        "Africa",
                        "Asia",
                        "Europe",
                        "North America",
                        "South America"
                },
                new String[]{
                        "/resources/maps/map.png",
                        "/resources/maps/map-africa.png",
                        "/resources/maps/map-asia.png",
                        "/resources/maps/map-europe.png",
                        "/resources/maps/map-northamerica.png",
                        "/resources/maps/map-southamerica.png"
                },
                new int[][]{
                        new int[]{160,92,69,72},
                        new int[]{230,38,124,136},
                        new int[]{165,34,62,53},
                        new int[]{11,22,123,87},
                        new int[]{89,117,42,71},
                });

        engine.addGameObject(map);


        Text hoverText = new Text(engine, 0xffffffff);
        TextArea hoverTextArea = new TextArea(engine, 50, 600,8*40, 14, 15, new Text[]{hoverText});

        engine.addGameObject(hoverTextArea);


        Menu mainMenu = new Menu(engine, menuAnchorX, menuAnchorY, menuFieldHeight,
                new String[]{
                    "add exploitation",
                    "remove exploitation",
                    "buy action"
                },
                new Action[]{
                        () -> engine.setCurrentMenu(buildAddExploitationMenu()),
                        () -> engine.setCurrentMenu(buildRemoveExploitationMenu()),
                        () -> {
                            engine.setCurrentMenu(buildBuyEventMenu());
                            Menu buyEvntMenu = engine.getCurrentMenu();

                            if (buyEvntMenu.getButtons()[0].getHoverAction() == null) {
                                for (int i = 0; i < buyEvntMenu.getButtons().length-2; i++) {
                                    int a = i;
                                    buyEvntMenu.getButtons()[i].setHoverAction(() -> {
                                        if (buyEvntMenu.noneHovered()) {
                                            hoverText.setText("");
                                        }
                                        else if (buyEvntMenu.getButtons()[a].isHovered()) {
                                            hoverText.setText(engine.getSelectedRegion().getBuyableEvents().get(a).toString());
                                        }
                                    }, () -> hoverText.setText(""));
                                }
                            }
                        }
                }, menuColor);

        engine.setCurrentMenu(mainMenu); // encapsuler
        engine.setMainMenu(mainMenu);


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

        Text regionMenuTitle = new Text(engine, menuColor);
        regionMenuTitle.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                regionMenuTitle.setText("REGION MENU");
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
                        new Text(engine),
                        new Text(engine),
                        regionMenuTitle
                });

        engine.addGameObject(regionInfo);


        Text turnsText = new Text(engine, 1025, 100, 0xff00ffff);
        turnsText.setAction(() -> turnsText.setText("TURN: " + gm.getTurn()));

        Text fundsText = new Text(engine, 1025, 116, 0xff00ffff);
        fundsText.setAction(() -> fundsText.setText("FUNDS: " + gm.getPoints() + " $"));

        Button nextTurnButton = new Button(engine, 1026, 148,
                () -> {
                    NotificationBus.clearWaitingList();
                    gm.nextTurn();
                },
                new Image("/resources/images/next_turn.png"));

        engine.addGameObject(turnsText);
        engine.addGameObject(fundsText);
        engine.addGameObject(nextTurnButton);


        Visual logo = new Visual(engine, (int)(0.5*(engine.getWidth() - 252)), 600, new Image("/resources/images/logo.png"));

        engine.addGameObject(logo);


        NotificationWaitingArea notificationWaitingArea = new NotificationWaitingArea(engine,850, 318, 8*50,14, 27, 0xffffffff);

        engine.addGameObject(notificationWaitingArea);


        engine.start();
    }
}

