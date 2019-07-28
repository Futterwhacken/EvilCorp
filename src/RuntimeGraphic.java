
import evilcorp.graphic.Engine;
import evilcorp.graphic.config.ConfigReader;
import evilcorp.graphic.gameobjects.Scene;
import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.visual.Visual;
import evilcorp.graphic.gfx.Font;

import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.Map;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.graphic.gameobjects.text.TextArea;

import evilcorp.graphic.gfx.Image;
import evilcorp.logic.GameMaster;


public class RuntimeGraphic {
    public static void main(String[] args) {

        /* LOADING CONFIG */

        GameMaster.initGameMaster("data/config/logic/"); // ceci peu arriver quand on load la GameScene
        Engine engine = Engine.initEngine("data/config/graphic/");

        Font[] fonts = ConfigReader.readFonts("data/config/graphic/fonts.cfg");
        engine.setStandardFont(fonts[0]);

        /* LOADING MENU SCENE */

        Scene menuScene = new Scene(engine);

        Image logoImage = new Image("data/resources/images/logo.png", 2);
        Visual menuLogo = new Visual(engine, (int)(0.5*(engine.getWidth() - logoImage.getWidth())), 75, logoImage);

        Image startImage = new Image("data/resources/images/start_game.png");
        Button startGame = new Button(engine,
                (int)(0.5*(engine.getWidth() - startImage.getWidth())),
                350,
                engine::nextScene,
                startImage);

        Image quitImage = new Image("data/resources/images/quit_game.png");
        Button quitGame = new Button(engine,
                (int)(0.5*(engine.getWidth() - quitImage.getWidth())),
                390,
                engine::stop,
                quitImage);

        menuScene.addGameObject(menuLogo);
        menuScene.addGameObject(startGame);
        menuScene.addGameObject(quitGame);

        engine.addScene(menuScene);


        /* LOADING GAME SCENE */

        Scene gameScene = new Scene(engine);

        Map map = ConfigReader.readMap("data/config/graphic/map.cfg");

        GameObject[] menuObjects = ConfigReader.readMenu("data/config/graphic/menu.cfg");
        Menu mainMenu = (Menu)menuObjects[0];
        TextArea hoverTextArea = (TextArea)menuObjects[1];

        TextArea regionInfo = ConfigReader.readInfoArea("data/config/graphic/info_area.cfg");

        GameObject[] gaugeObjects = ConfigReader.readGauges("data/config/graphic/gauges.cfg");

        GameObject[] nextObjects = ConfigReader.readNextArea("data/config/graphic/next_area.cfg");

        GameObject[] notifObjects = ConfigReader.readNotifArea("data/config/graphic/notif_area.cfg");


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

        engine.addScene(gameScene);



        engine.start();
    }
}

