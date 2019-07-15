package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;

public class Menu extends GameObject
{
    /*private int posX;
    private int posY;
    private int width;
    private int height;

    private final int fieldHeight = 10;*/

    private Button[] buttons;

    public Menu(Engine engine, int posX, int posY, int width, int fieldHeight, String[] options, Action[] actions) {
        super(engine);
        this.buttons = new Button[actions.length];

        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new Button(engine, posX, posY + i * fieldHeight, width, fieldHeight, actions[i],null, options[i]);
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

    public void open() {
        // gc.setCurrentMenu(this);
    }
}
