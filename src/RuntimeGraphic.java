
import evilcorp.graphic.Engine;
import evilcorp.graphic.config.ConfigReader;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.Scene;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.graphic.gameobjects.text.Text;
import evilcorp.graphic.gameobjects.text.TextArea;
import evilcorp.graphic.gameobjects.visual.Animation;

import evilcorp.graphic.gameobjects.visual.GaugeGraph;
import evilcorp.logic.GameMaster;
import evilcorp.logic.NotificationBus;


public class RuntimeGraphic {
    public static void main(String[] args) {

        // !!! HANDLE EXCEPTIONS !!!

        /* LOADING CONFIG */

        // reworkd engine and gm init
        GameMaster gm = GameMaster.initGameMaster("data/config/logic/"); // could be called when loading GameScene
        Engine engine = Engine.initEngine("data/config/graphic/");


        /* SCENES */

        Scene loadingScene = new Scene();
        Scene splashScene = new Scene();
        Scene menuScene = new Scene();
        Scene creditsScene = new Scene();
        Scene gameScene = new Scene();


        /* LOADING LOADING SCENE */

        GameObject[] loadingSceneObjects = ConfigReader.readScene("data/config/graphic/scenes/loading_scene.cfg");
        for (GameObject o : loadingSceneObjects) {
            loadingScene.addGameObject(o);
        }

        double start = Engine.getTime();
        loadingScene.addAction(() -> {
            if (Engine.getTime() - start >= 3) {
                engine.setCurrentScene(splashScene);
            }
        });

        engine.addScene(loadingScene);


        /* START ENGINE */

        engine.start(); // start engine here, to mask loading of other resources


        /* LOADING SPLASH SCENE */

        GameObject[] splashSceneObjects = ConfigReader.readScene("data/config/graphic/scenes/splash_scene.cfg");

        for (GameObject o : splashSceneObjects) {
            splashScene.addGameObject(o);
        }

        Animation splash = (Animation)ConfigReader.getObject("splash");

        splashScene.addAction(() -> {
            if (splash.isFinished()) {
                engine.setCurrentScene(menuScene);
            }
            if (engine.getCurrentScene() == splashScene && !splash.isActive()) {
                splash.launch();
            }
        });

        engine.addScene(splashScene);


        /* LOADING MENU SCENE */

        GameObject[] menuSceneObjects = ConfigReader.readScene("data/config/graphic/scenes/menu_scene.cfg");

        for (GameObject o : menuSceneObjects) {
            menuScene.addGameObject(o);
        }

        ((Button)ConfigReader.getObject("startGame")).setAction(
                () -> engine.setCurrentScene(gameScene)
        );

        ((Button)ConfigReader.getObject("showCredits")).setAction(
                () -> engine.setCurrentScene(creditsScene)
        );

        ((Button)ConfigReader.getObject("quitGame")).setAction(
                () -> engine.stop()
        );

        engine.addScene(menuScene);


        /* LOADING CREDITS SCENE */

        GameObject[] creditsSceneObjects = ConfigReader.readScene("data/config/graphic/scenes/credits_scene.cfg");

        for (GameObject o : creditsSceneObjects) {
            creditsScene.addGameObject(o);
        }

        ((Button)ConfigReader.getObject("exitCredits")).setAction(
                () -> engine.setCurrentScene(menuScene)
        );

        engine.addScene(creditsScene);


        /* LOADING GAME SCENE */

        GameObject[] gameSceneObjects = ConfigReader.readScene("data/config/graphic/scenes/game_scene.cfg");

        for (GameObject o : gameSceneObjects) {
            gameScene.addGameObject(o);
        }


        ((Button)ConfigReader.getObject("nextTurn")).setAction(
                () -> {
                    NotificationBus.clearWaitingList();
                    NotificationBus.clearImmediateList();
                    gm.nextTurn();
                }
        );

        Text turns = ((Text)ConfigReader.getObject("turns"));
        turns.setAction(() -> turns.setText("TURN: " + gm.getTurn()));

        Text funds = ((Text)ConfigReader.getObject("funds"));
        funds.setAction(() -> funds.setText("FUNDS: " + gm.getPoints() + " $"));


        TextArea hoverTextArea = (TextArea)ConfigReader.getObject("hoverTextArea");
        Menu mainMenu = (Menu)ConfigReader.getObject("mainMenu");
        mainMenu.setButtons(new Action[]{
                () -> engine.getCurrentScene().setCurrentMenu(Menu.buildAddExploitationMenu(mainMenu.getOption(0), mainMenu)),
                () -> engine.getCurrentScene().setCurrentMenu(Menu.buildRemoveExploitationMenu(mainMenu.getOption(1), mainMenu)),
                () -> {
                    engine.getCurrentScene().setCurrentMenu(Menu.buildBuyEventMenu(mainMenu.getOption(2), mainMenu));
                    Menu buyEvntMenu = engine.getCurrentScene().getCurrentMenu();

                    if (buyEvntMenu.getButtons()[0].getHoverAction() == null) {
                        for (int i = 0; i < buyEvntMenu.getButtons().length-2; i++) {
                            int a = i;
                            buyEvntMenu.getButtons()[i].setHoverAction(() -> {
                                if (buyEvntMenu.noneHovered()) {
                                    hoverTextArea.getTexts()[0].setText("");
                                }
                                else if (buyEvntMenu.getButtons()[a].isHovered()) {
                                    hoverTextArea.getTexts()[0].setText(engine.getCurrentScene().getSelectedRegion().getBuyableEvents().get(a).toString());
                                }
                            }, () -> hoverTextArea.getTexts()[0].setText(""));
                        }
                    }
                }
            }
        );


        GaugeGraph production = (GaugeGraph)ConfigReader.getObject("production");
        production.setAction(() -> production.setLevel(gm.getWorld().getProductivity()));

        GaugeGraph visibility = (GaugeGraph)ConfigReader.getObject("pollution");
        visibility.setAction(() -> visibility.setLevel(gm.getWorld().getVisibility()));

        GaugeGraph social = (GaugeGraph)ConfigReader.getObject("social");
        social.setAction(() -> social.setLevel(gm.getWorld().getSocial()));



        Text selectedRegion = (Text)ConfigReader.getObject("!selectedRegion");
        selectedRegion.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                selectedRegion.setText(engine.getCurrentScene().getSelectedRegion().getName());
        });

        Text activityText = (Text)ConfigReader.getObject("!activityText");
        activityText.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                activityText.setText(engine.getCurrentScene().getSelectedRegion().getParams().getActivityToleranceString());
        });

        Text environmentText = (Text)ConfigReader.getObject("!environmentText");
        environmentText.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                environmentText.setText(engine.getCurrentScene().getSelectedRegion().getParams().getEnvironmentToleranceString());
        });

        Text oppositionText = (Text)ConfigReader.getObject("!oppositionText");
        oppositionText.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                oppositionText.setText(engine.getCurrentScene().getSelectedRegion().getParams().getOppositionToleranceString());
        });

        Text productionText = (Text)ConfigReader.getObject("!productionText");
        productionText.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                productionText.setText("PRODUCTION: " + engine.getCurrentScene().getSelectedRegion().getProductivity());
        });

        Text pollutionText = (Text)ConfigReader.getObject("!pollutionText");
        pollutionText.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                pollutionText.setText("POLLUTION: " + engine.getCurrentScene().getSelectedRegion().getVisibility());
        });

        Text socialText = (Text)ConfigReader.getObject("!socialText");
        socialText.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                socialText.setText("SOCIAL: " + engine.getCurrentScene().getSelectedRegion().getSocial());
        });

        Text exploitationText = (Text)ConfigReader.getObject("!exploitationText");
        exploitationText.setAction(() -> {
            if (engine.getCurrentScene().getSelectedRegion() != null)
                exploitationText.setText(""+engine.getCurrentScene().getSelectedRegion().getExploitations().size() + "/" + engine.getCurrentScene().getSelectedRegion().getMaxExpl() + " EXPLOITATIONS");
        });


        gameScene.addAction(() -> {
            switch (gm.checkGameStatus()) {
                case 0:
                    System.out.println("YOU LOST"); // debug, waiting for fonts
                    engine.setCurrentScene(creditsScene);


                case 2:
                    System.out.println("YOU WON"); // debug, waiting for fonts
                    engine.setCurrentScene(creditsScene);
            }
        });


        engine.addScene(gameScene);

    }
}

