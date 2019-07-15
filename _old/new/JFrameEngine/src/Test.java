import engine.Engine;
import engine.gameobjects.Button;

public class Test {
    public static void main(String[] args) {

        Engine engine = new Engine("");
        // GameMaster gm = GameMaster.initGameMaster(configpath);

        /*Button nextTurnButton = new Button(
                engine,
                360,
                180,
                40,
                10,
                () -> { System.out.println("next turn"); },
                null,
                "next turn");*/

        //engine.addGameObject(nextTurnButton);

        engine.start();
    }
}
