import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.*;
import evilcorp.graphic.gfx.Image;


import evilcorp.logic.GameMaster;
import evilcorp.logic.area.region.Region;


public class RuntimeGraphic {
    private static Engine engine;
    private static GameMaster gm;

    private static double mapScale = 1.75;
    private static int mapPosX;
    private static int mapPosY;

    private static int menuColor = 0xff00ffff;
    private static int menuAnchorX = 50;
    private static int menuAnchorY = 335;
    private static int menuWidth = 100;
    private static int menuFieldHeight = 21;

    private static int regionInfoColor = 0xffffffff;
    private static int regionInfoAnchorX = 50;
    private static int regionInfoAnchorY = 100;
    private static int regionInfoWidth = 8*40;
    private static int regionInfoFieldHeight = 16;

    private static Region getRegion(String regionName) {
        for (Region r : gm.getWorld().getRegions()) {
            if (r.getName().equals(regionName)) {
                return r;
            }
        }

        return null;
    }

    private static Menu buildRemoveExploitationMenu() {
        String[] options = new String[engine.getSelectedRegion().getExploitations().size() + 2];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length - 2; i++) {
            int a = i;
            options[i] = ""+engine.getSelectedRegion().getExploitations().get(i);
            actions[i] = () -> {
                engine.getSelectedRegion().removeExploitation(a);
                engine.setCurrentMenu(buildRemoveExploitationMenu());
            };
        }
        return buildReturnableMenu(options, actions);
    }

    private static Menu buildBuyEventMenu() {
        String[] options = new String[engine.getSelectedRegion().getBuyableEvents().size() + 2];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length - 2; i++) {
            int a = i;
            options[i] = ""+engine.getSelectedRegion().getBuyableEvents().get(i).getName();
            actions[i] = () -> {
                engine.getSelectedRegion().buyEvent(engine.getSelectedRegion().getBuyableEvents().get(a));
                engine.setCurrentMenu(buildBuyEventMenu());
            };
        }
        return buildReturnableMenu(options, actions);
    }

    private static Menu buildReturnableMenu(String[] options, Action[] actions) {
        options[options.length - 2] = " ";
        actions[options.length - 2] = () -> {};
        options[options.length - 1] = "return";
        actions[options.length - 1] = () -> {engine.setCurrentMenu(engine.getMainMenu());};

        return new Menu(engine, menuAnchorX, menuAnchorY, menuWidth, menuFieldHeight, options, actions, menuColor);
    }

    public static void main(String[] args) {

        engine = new Engine("");
        gm = GameMaster.initGameMaster("data/config/logic/");

        mapPosX = (int)(0.5*(engine.getWidth() - (370 * mapScale)));
        mapPosY = 0;

        Image mapImage = new Image("/resources/maps/map.png", mapScale);
        Image mapAfricaImage = new Image("/resources/maps/map-africa.png", mapScale);
        Image mapAsiaImage = new Image("/resources/maps/map-asia.png", mapScale);
        Image mapEuropeImage = new Image("/resources/maps/map-europe.png", mapScale);
        Image mapNorthAmericaImage = new Image("/resources/maps/map-northamerica.png", mapScale);
        Image mapSouthAmericaImage = new Image("/resources/maps/map-southamerica.png", mapScale);

        //Visual logo = new Visual(engine, new Image("/resources/logo/logo.png"), 266, 300);

        Visual map = new Visual(engine, mapImage, mapPosX, mapPosY);

        // link region logic config with region graphic config through the name (string) of the region
        Button africaButton = new Button(engine, (int)(mapPosX + 160 * mapScale),(int)(mapPosY + 92 * mapScale), (int)(69 * mapScale), (int)(72 * mapScale),
                () -> {
                map.setVisual(mapAfricaImage);
                engine.setSelectedRegion(getRegion("Africa"));
                });
        Button asiaButton = new Button(engine, (int)(mapPosX + 230 * mapScale), (int)(mapPosY + 38 * mapScale), (int)(124 * mapScale), (int)(136 * mapScale),
                () -> {
                map.setVisual(mapAsiaImage);
                engine.setSelectedRegion(getRegion("Asia"));
                });
        Button europeButton = new Button(engine, (int)(mapPosX + 165 * mapScale), (int)(mapPosY + 34 * mapScale), (int)(62 * mapScale), (int)(53 * mapScale),
                () -> {
                map.setVisual(mapEuropeImage);
                engine.setSelectedRegion(getRegion("Europe"));
                });
        Button northAmericaButton = new Button(engine, (int)(mapPosX + 11 * mapScale), (int)(mapPosY + 22 * mapScale), (int)(123 * mapScale), (int)(87 * mapScale),
                () -> {
                map.setVisual(mapNorthAmericaImage);
                engine.setSelectedRegion(getRegion("North America"));
                });
        Button southAmericaButton = new Button(engine, (int)(mapPosX + 89 * mapScale), (int)(mapPosY + 117 * mapScale), (int)(42 * mapScale), (int)(71 * mapScale),
                () -> {
                map.setVisual(mapSouthAmericaImage);
                engine.setSelectedRegion(getRegion("South America"));
                });


        /*
        r.buyExploitation(select-1);
        displayImmediateNotifications();

        show buy and rmv cost
        show event status and cost in event buy menu
        show event descr and other hover/selected information (such as region details) in notif area (or alt dedicated area)

        add menu label

        add Text Area object (?)

        add Scene object (splash screen, menu, game, credits)

        button text margin !!!

        standardiser ordre des arguments
        */

        Menu addExploitationMenu = new Menu(engine, menuAnchorX, menuAnchorY, menuWidth, menuFieldHeight,
                new String[]{
                        "add primary exploitation",
                        "add secondary exploitation",
                        "add tertiary exploitation",
                        " ",
                        "return"
                },
                new Action[]{
                        () -> {engine.getSelectedRegion().buyExploitation(0);},
                        () -> {engine.getSelectedRegion().buyExploitation(1);},
                        () -> {engine.getSelectedRegion().buyExploitation(2);},
                        () -> {},
                        () -> {engine.setCurrentMenu(engine.getMainMenu());}
                }, menuColor);

        Menu mainMenu = new Menu(engine, menuAnchorX, menuAnchorY, menuWidth, menuFieldHeight,
                new String[]{
                    "add exploitation",
                    "remove exploitation",
                    "buy action"
                },
                new Action[]{
                        () -> {engine.setCurrentMenu(addExploitationMenu);},
                        () -> {engine.setCurrentMenu(buildRemoveExploitationMenu());},
                        () -> {engine.setCurrentMenu(buildBuyEventMenu());}
                }, menuColor);

        engine.setCurrentMenu(mainMenu);
        engine.setMainMenu(mainMenu);

        engine.addGameObject(map);
        engine.addGameObject(africaButton);
        engine.addGameObject(asiaButton);
        engine.addGameObject(europeButton);
        engine.addGameObject(northAmericaButton);
        engine.addGameObject(southAmericaButton);


        //engine.addGameObject(logo);

        Text fundsText = new Text(engine, "FUNDS: ", 0, 350, 0xffffffff);
        fundsText.setAction(() -> {fundsText.setText(fundsText.getDefaultText() + gm.getPoints());});


        Text selectedRegionText = new Text(engine, "SELECT A REGION", 0, 0, regionInfoColor);
        selectedRegionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionText.setText(engine.getSelectedRegion().getName());
            else
                selectedRegionText.setText(selectedRegionText.getDefaultText());
        });

        Text selectedRegionActivityText = new Text(engine, "", 0, 0, regionInfoColor);
        selectedRegionActivityText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionActivityText.setText(engine.getSelectedRegion().getParams().getActivityToleranceString());
        });

        Text selectedRegionEnvironmentText = new Text(engine, "", 0, 0, regionInfoColor);
        selectedRegionEnvironmentText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionEnvironmentText.setText(engine.getSelectedRegion().getParams().getEnvironmentToleranceString());
        });

        Text selectedRegionOppositionText = new Text(engine, "", 0, 0, regionInfoColor);
        selectedRegionOppositionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionOppositionText.setText(engine.getSelectedRegion().getParams().getOppositionToleranceString());
        });

        Text selectedRegionProductionText = new Text(engine, "", 0,0, regionInfoColor);
        selectedRegionProductionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionProductionText.setText("PRODUCTION: " + engine.getSelectedRegion().getProductivity());
        });

        Text selectedRegionPollutionText = new Text(engine, "", 0,0, regionInfoColor);
        selectedRegionPollutionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionPollutionText.setText("POLLUTION: " + engine.getSelectedRegion().getVisibility());
        });

        Text selectedRegionSocialText = new Text(engine, "", 0,0, regionInfoColor);
        selectedRegionSocialText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionSocialText.setText("SOCIAL: " + engine.getSelectedRegion().getSocial());
        });

        Text selectedRegionExploitationText = new Text(engine, "", 0, 0, regionInfoColor);
        selectedRegionExploitationText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionExploitationText.setText(""+engine.getSelectedRegion().getExploitations().size() + "/" + Region.getMaxExpl() + " EXPLOITATIONS");
        });

        Text regionMenuTitle = new Text(engine, "", 0,0, menuColor);
        regionMenuTitle.setAction(() -> {
            if (engine.getSelectedRegion() != null) {
                regionMenuTitle.setText("REGION MENU");
            }
        });


        TextArea regionInfo = new TextArea(engine, regionInfoAnchorX, regionInfoAnchorY, regionInfoWidth,regionInfoFieldHeight, 15,
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
        //engine.addGameObject(fundsText);

        engine.start();
    }
}


        /*Text test1 = new Text(engine, "AAAAAAAAAA", 10, 200, 0xffffffff);
        Text test2 = new Text(engine, "BBBBBBBBBB", 10, 210, 0xffffffff);
        Text test3 = new Text(engine, "CCCCCCCCCC", 10, 220, 0xffffffff);
        Text test4 = new Text(engine, "OOOOOOOOOO", 10, 230, 0xffffffff);
        Text test5 = new Text(engine, "IIIIIIIIII", 10, 240, 0xffffffff);
        Text test6 = new Text(engine, "AAAAAAAAAA", 10, 200, 0xffffffff);
        Text test7 = new Text(engine, "AAAAAAAAAA", 10, 200, 0xffffffff);
        Text test8 = new Text(engine, "AAAAAAAAAA", 10, 200, 0xffffffff);
        Text test9 = new Text(engine, "AAAAAAAAAA", 10, 200, 0xffffffff);

        engine.addGameObject(test1);
        engine.addGameObject(test2);
        engine.addGameObject(test3);
        engine.addGameObject(test4);
        engine.addGameObject(test5);*/

