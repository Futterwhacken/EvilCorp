
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


public class RuntimeGraphic {
    private static Engine engine;
    private static GameMaster gm;

    public static void main(String[] args) {

        /* LOADING CONFIG */

        gm = GameMaster.initGameMaster("data/config/logic/");
        engine = Engine.initEngine("data/config/graphic/");

        Font[] fonts = ConfigReader.readFonts("data/config/graphic/fonts.cfg", "data/resources/fonts/");
        engine.setStandardFont(fonts[0]);

        Map map = ConfigReader.readMap("data/config/graphic/map.cfg","data/resources/maps/");

        GameObject[] menuObjects = ConfigReader.readMenu("data/config/graphic/menu.cfg");
        Menu mainMenu = (Menu)menuObjects[0];
        TextArea hoverTextArea = (TextArea)menuObjects[1];

        TextArea regionInfo = ConfigReader.readInfoArea("data/config/graphic/info_area.cfg");

        GameObject[] gaugeObjects = ConfigReader.readGauges("data/config/graphic/gauges.cfg");


        /* SETTING UP ENGINE */

        engine.addGameObject(map);

        engine.setMainMenu(mainMenu);
        engine.addGameObject(hoverTextArea);

        engine.addGameObject(regionInfo);

        for (GameObject o : gaugeObjects) {
            engine.addGameObject(o);
        }


        // defer to ConfigReader

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

        Visual logo = new Visual(engine, (int)(0.5*(engine.getWidth() - 252)), 600, new Image("data/resources/images/logo.png"));

        NotificationWaitingArea notificationWaitingArea = new NotificationWaitingArea(engine,850, 318, 8*50,14, 27, 0xffffffff);

        NotificationImmediateArea notificationImmediateArea = new NotificationImmediateArea(engine,50, 510, 8*50,16, 3, 0xffff0000, 5);

        engine.addGameObject(turnsText);
        engine.addGameObject(fundsText);
        engine.addGameObject(nextTurnButton);
        engine.addGameObject(logo);
        engine.addGameObject(notificationWaitingArea);
        engine.addGameObject(notificationImmediateArea);


        engine.start();
    }
}

