
import evilcorp.graphic.Engine;
import evilcorp.graphic.config.ConfigReader;
import evilcorp.graphic.gameobjects.visual.Animation;
import evilcorp.graphic.gfx.Font;
import evilcorp.graphic.gfx.Image;

import evilcorp.graphic.gameobjects.Scene;
import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.text.Text;
import evilcorp.graphic.gameobjects.visual.Visual;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.Map;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.graphic.gameobjects.text.TextArea;

import evilcorp.logic.GameMaster;


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

        Map map = ConfigReader.readMap("data/config/graphic/toProcess/map.cfg");

        GameObject[] menuObjects = ConfigReader.readMenu("data/config/graphic/toProcess/menu.cfg");
        Menu mainMenu = (Menu)menuObjects[0];
        TextArea hoverTextArea = (TextArea)menuObjects[1];

        TextArea regionInfo = ConfigReader.readInfoArea("data/config/graphic/toProcess/info_area.cfg");

        GameObject[] gaugeObjects = ConfigReader.readGauges("data/config/graphic/toProcess/gauges.cfg");

        GameObject[] nextObjects = ConfigReader.readNextArea("data/config/graphic/toProcess/next_area.cfg");

        GameObject[] notifObjects = ConfigReader.readNotifArea("data/config/graphic/toProcess/notif_area.cfg");


        gameScene.addGameObject(map);

        gameScene.setMainMenu(mainMenu);
        gameScene.addGameObject(hoverTextArea);

        gameScene.addGameObject(regionInfo);

        for (GameObject o : gaugeObjects) {
            gameScene.addGameObject(o);
        }

        for (GameObject o : nextObjects) {
            gameScene.addGameObject(o);
        }

        for (GameObject o : notifObjects) {
            gameScene.addGameObject(o);
        }

        gameScene.addAction(() -> {
            switch (gm.checkGameStatus()) {
                case 0:
                    System.out.println("YOU LOST"); // debug, waiting for fonts
                    engine.setCurrentScene(creditsScene);


                case 2:
                    System.out.println("YOU WON"); // debug, waiting for fonts
                    engine.setCurrentScene(creditsScene);

                default: break;
            }
        });

        engine.addScene(gameScene);


    }
}

