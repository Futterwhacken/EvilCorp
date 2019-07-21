package evilcorp.graphic.gameobjects.interactive;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;

public class Menu extends GameObject
{
    private final Button[] buttons;

    public Menu(Engine engine, int posX, int posY, int fieldHeight, String[] options, Action[] actions, int color) {
        super(engine);

        this.buttons = new Button[actions.length];

        // implement margin

        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new Button(engine, posX, posY + i * fieldHeight, options[i].length() * engine.getStandardFont().getTextUnitWidth(), fieldHeight, actions[i], options[i], color);
        }
    }

    @Override
    public void update() {
        for (Button b : buttons) {
            b.update();
        }
    }

    @Override
    public void render() {
        for (Button b : buttons) {
            b.render();
        }
    }

    public Button[] getButtons() { return buttons; }

    public boolean noneHovered() {
        for (Button b : buttons) {
            if (b.isHovered()) {
                return false;
            }
        }

        return true;
    }
}
