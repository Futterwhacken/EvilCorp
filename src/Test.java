import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Button;
import evilcorp.graphic.gameobjects.Text;
import evilcorp.graphic.gameobjects.Visual;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.Menu;
import evilcorp.graphic.gfx.Image;


import evilcorp.logic.GameMaster;
import evilcorp.logic.area.region.Region;


public class Test {
    public static Engine engine;
    public static GameMaster gm;

    public static int menuColor = 0xff00ff00;
    public static int menuAnchorX = 10;
    public static int menuAnchorY = 100;

    public static Region getRegion(String regionName) {
        for (Region r : gm.getWorld().getRegions()) {
            if (r.getName().equals(regionName)) {
                return r;
            }
        }

        return null;
    }

    public static Menu buildRemoveExploitationMenu() {
        String[] options = new String[engine.getSelectedRegion().getExploitations().size() + 1];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length - 1; i++) {
            int a = i;
            options[i] = ""+engine.getSelectedRegion().getExploitations().get(i);
            actions[i] = () -> {
                engine.getSelectedRegion().removeExploitation(a);
                engine.setCurrentMenu(buildRemoveExploitationMenu());
            };
        }
        options[options.length - 1] = "return";
        actions[options.length - 1] = () -> {engine.setCurrentMenu(engine.getMainMenu());};

        return new Menu(engine, menuAnchorX, menuAnchorY, 100, 10, options, actions, menuColor);
    }

    public static Menu buildBuyEventMenu() {
        String[] options = new String[engine.getSelectedRegion().getBuyableEvents().size() + 1];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length - 1; i++) {
            int a = i;
            options[i] = ""+engine.getSelectedRegion().getBuyableEvents().get(i).getName();
            actions[i] = () -> {
                engine.getSelectedRegion().buyEvent(engine.getSelectedRegion().getBuyableEvents().get(a));
                engine.setCurrentMenu(buildBuyEventMenu());
            };
        }
        options[options.length - 1] = "return";
        actions[options.length - 1] = () -> {engine.setCurrentMenu(engine.getMainMenu());};

        return new Menu(engine, menuAnchorX, menuAnchorY, 100, 10, options, actions, menuColor);
    }

    public static void main(String[] args) {

        engine = new Engine("");
        gm = GameMaster.initGameMaster("data/config/logic/");

        Image mapImage = new Image("/resources/maps/map.png");
        Image mapAfricaImage = new Image("/resources/maps/map-africa.png");
        Image mapAsiaImage = new Image("/resources/maps/map-asia.png");
        Image mapEuropeImage = new Image("/resources/maps/map-europe.png");
        Image mapNorthAmericaImage = new Image("/resources/maps/map-northamerica.png");
        Image mapSouthAmericaImage = new Image("/resources/maps/map-southamerica.png");



        Visual map = new Visual(engine, mapImage, 135, 0);

        // add color parameter for label (or directly take text ?), add simplified constructors
        // link region logic config with region graphic config through the name (string) of the region
        Button africaButton = new Button(engine, 296,90, 77, 85,
                () -> {
                map.setVisual(mapAfricaImage);
                engine.setSelectedRegion(getRegion("Africa"));
                });
        Button asiaButton = new Button(engine, 374, 36, 140, 144,
                () -> {
                map.setVisual(mapAsiaImage);
                engine.setSelectedRegion(getRegion("Asia"));
                });
        Button europeButton = new Button(engine, 306, 22, 67, 67,
                () -> {
                map.setVisual(mapEuropeImage);
                engine.setSelectedRegion(getRegion("Europe"));
                });
        Button northAmericaButton = new Button(engine, 136, 21, 153, 92,
                () -> {
                map.setVisual(mapNorthAmericaImage);
                engine.setSelectedRegion(getRegion("North America"));
                });
        Button southAmericaButton = new Button(engine, 215, 115, 63, 87,
                () -> {
                map.setVisual(mapSouthAmericaImage);
                engine.setSelectedRegion(getRegion("South America"));
                });

        Text selectedRegionText = new Text(engine, "Select a region", 10, 10, 0xffffffff);
        selectedRegionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionText.setText(engine.getSelectedRegion().getName());
            else
                selectedRegionText.setText(selectedRegionText.getDefaultText());
            });

        Text fundsText = new Text(engine, "FUNDS: ", 0, 350, 0xffffffff);
        fundsText.setAction(() -> {fundsText.setText(fundsText.getDefaultText() + gm.getPoints());});

        /*
        r.buyExploitation(select-1);
        displayImmediateNotifications();

        show buy and rmv cost
        show event status and cost in event buy menu
         */

        Menu addExploitationMenu = new Menu(engine, menuAnchorX, menuAnchorY, 100, 10,
                new String[]{
                        "add primary exploitation",
                        "add secondary exploitation",
                        "add tertiary exploitation",
                        "return"
                },
                new Action[]{
                        () -> {engine.getSelectedRegion().buyExploitation(0);},
                        () -> {engine.getSelectedRegion().buyExploitation(1);},
                        () -> {engine.getSelectedRegion().buyExploitation(2);},
                        () -> {engine.setCurrentMenu(engine.getMainMenu());}
                }, menuColor);

        Menu mainMenu = new Menu(engine, menuAnchorX, menuAnchorY, 100, 10,
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
        engine.addGameObject(selectedRegionText);

        engine.addGameObject(fundsText);



        engine.start();
    }
}
