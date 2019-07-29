
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

        /* LOADING CONFIG */

        GameMaster gm = GameMaster.initGameMaster("data/config/logic/"); // ceci peu arriver quand on load la GameScene
        Engine engine = Engine.initEngine("data/config/graphic/");

        Font[] fonts = ConfigReader.readFonts("data/config/graphic/fonts.cfg");
        engine.setStandardFont(fonts[0]);
        engine.setStandardFontBig(fonts[1]);

        /* SCENES */

        Scene menuScene = new Scene(engine);
        Scene gameScene = new Scene(engine);
        Scene creditsScene = new Scene(engine);

        Scene lostScene = new Scene(engine); // ?
        Scene wonScene = new Scene(engine); // ?

        /* LOADING MENU SCENE */

        /*String logoPath = "data/resources/images/logo.png";

        Image[] images = new Image[60];
        for (int i = 0; i < 60; i++) {
            images[i] = new Image(logoPath, (i+1)/20.0);
        }

        Animation animation = new Animation(engine, engine.getWidth()/2, engine.getHeight()/2,
                images, 2);

        menuScene.addGameObject(animation);

        engine.addScene(menuScene);
        animation.launch();*/

        Image logoImage = new Image("data/resources/images/logo.png", 2);
        Visual menuLogo = new Visual(engine, -1, 75, logoImage);

        String startLabel = "START GAME";
        Button startGame = new Button(engine,
                -1,
                350,
                () -> engine.setCurrentScene(gameScene),
                startLabel, 0xffffffff, engine.getStandardFontBig());

        String creditsLabel = "CREDITS";
        Button creditsButton = new Button(engine,
                -1,
                390,
                () -> engine.setCurrentScene(creditsScene),
                creditsLabel, 0xffffffff, engine.getStandardFontBig());

        String quitLabel = "QUIT GAME";
        Button quitGame = new Button(engine,
                -1,
                430,
                () -> engine.stop(),
                quitLabel, 0xffffffff, engine.getStandardFontBig());

        menuScene.addGameObject(menuLogo);
        menuScene.addGameObject(startGame);
        menuScene.addGameObject(creditsButton);
        menuScene.addGameObject(quitGame);

        engine.addScene(menuScene);


        /* LOADING GAME SCENE */

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

        /* LOADING CREDITS SCENE */

        // take logoImage
        Visual creditsLogo = new Visual(engine, -1, 75, logoImage);

        String credits = "CREATED BY HARMED-CHRONOGRAM & SPIEGELEISEN";
        Text creditsText = new Text(engine,
                -1,
                340,
                credits,
                0xffffffff,
                engine.getStandardFontBig()
        );


        String returnLabel = "RETURN TO MAIN MENU";
        Button returnMainMenu = new Button(engine,
                -1,
                420,
                () -> engine.setCurrentScene(menuScene),
                returnLabel, 0xffffffff, engine.getStandardFontBig());

        creditsScene.addGameObject(creditsLogo);
        creditsScene.addGameObject(creditsText);
        creditsScene.addGameObject(returnMainMenu);

        engine.addScene(creditsScene);





        engine.start();
    }
}

