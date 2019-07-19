package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;

public class Menu extends GameObject
{
    private Button[] buttons;

    public Menu(Engine engine, int posX, int posY, int width, int fieldHeight, String[] options, Action[] actions, int color) {
        super(engine);

        this.buttons = new Button[actions.length];

        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new Button(engine, posX, posY + i * fieldHeight, width, fieldHeight, actions[i], options[i], color);
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
}
