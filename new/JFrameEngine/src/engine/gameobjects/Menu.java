package engine.gameobjects;

import engine.GameContainer;

import java.util.ArrayList;

public class Menu extends GameObject
{
    private int posX;
    private int posY;
    private int width;
    private int height;

    private ArrayList<Button> options;

    public Menu(GameContainer gc, int posX, int posY, int width, int height) {
        super(gc);


    }

    @Override
    public void update() {

    }
}
