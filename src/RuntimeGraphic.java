
import evilcorp.graphic.Engine;
import evilcorp.graphic.config.ConfigReader;
import evilcorp.graphic.gfx.Font;

import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.Map;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.graphic.gameobjects.text.TextArea;

import evilcorp.logic.GameMaster;


public class RuntimeGraphic {
    public static void main(String[] args) {

        /* LOADING CONFIG */

        GameMaster.initGameMaster("data/config/logic/");
        Engine engine = Engine.initEngine("data/config/graphic/");

        Font[] fonts = ConfigReader.readFonts("data/config/graphic/fonts.cfg", "data/resources/fonts/");
        engine.setStandardFont(fonts[0]);

        Map map = ConfigReader.readMap("data/config/graphic/map.cfg","data/resources/maps/");

        GameObject[] menuObjects = ConfigReader.readMenu("data/config/graphic/menu.cfg");
        Menu mainMenu = (Menu)menuObjects[0];
        TextArea hoverTextArea = (TextArea)menuObjects[1];

        TextArea regionInfo = ConfigReader.readInfoArea("data/config/graphic/info_area.cfg");

        GameObject[] gaugeObjects = ConfigReader.readGauges("data/config/graphic/gauges.cfg");

        GameObject[] nextObjects = ConfigReader.readNextArea("data/config/graphic/next_area.cfg", "data/resources/images/");

        GameObject[] notifObjects = ConfigReader.readNotifArea("data/config/graphic/notif_area.cfg", "data/resources/images/");

        /* SETTING UP ENGINE */

        engine.addGameObject(map);

        engine.setMainMenu(mainMenu);
        engine.addGameObject(hoverTextArea);

        engine.addGameObject(regionInfo);

        for (GameObject o : gaugeObjects) {
            engine.addGameObject(o);
        }

        for (GameObject o : nextObjects) {
            engine.addGameObject(o);
        }

        for (GameObject o : notifObjects) {
            engine.addGameObject(o);
        }

        engine.start();
    }
}

