package engine.gameobjects;

import engine.GameContainer;

import java.util.ArrayList;

public class Menu extends GameObject
{

    /* dans le runtime, avoir un pointeur sur le menu courant, quand on ouvre un menu on change
    ce pointeur
     */

    private int posX;
    private int posY;
    private int width;
    private int height;

    private final int fieldHeight = 10;

    private Button[] buttons;

    public Menu(GameContainer gc, int posX, int posY, int width, int height, String[] options, Action[] actions) {
        super(gc);
        this.buttons = new Button[actions.length];

        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new Button(gc, posX, posY + i * fieldHeight, width, fieldHeight, actions[i],null, options[i]);
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
