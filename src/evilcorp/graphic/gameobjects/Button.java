package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gfx.Image;

public class Button extends GameObject
{
    private int posX;
    private int posY;
    private int width;
    private int height;

    private boolean clicked = false;

    private Action action;
    private Image image;
    private String label;
    private int labelColor;

    public Button(Engine engine, int posX, int posY, int width, int height, Action action, Image image, String label, int labelColor) {
        super(engine);

        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.action = action;

        this.image = image;

        if (image != null) {
            image.setWidth(width);
            image.setHeight(height);
        }

        this.label = label;
        this.labelColor = labelColor;
    }

    public Button(Engine engine, int posX, int posY, int width, int height, Action action, Image image) {
        this(engine, posX, posY, width, height, action, image, null, 0);
    }

    public Button(Engine engine, int posX, int posY, int width, int height, Action action, String label, int labelColor) {
        this(engine, posX, posY, width, height, action, null, label, labelColor);
    }

    public Button(Engine engine, int posX, int posY, int width, int height, Action action) {
        this(engine, posX, posY, width, height, action, null, null, 0);
    }


    private boolean checkClicked() {

        int mouseX = engine.getInput().getMouseX();
        int mouseY = engine.getInput().getMouseY();

        if ((mouseX >= posX && mouseX <= posX + width) && (mouseY >= posY && mouseY <= posY + height)) {
            if (engine.getInput().isButton(1)) { // 1 = left click, tomod ?
                return true;
            }
        }

        return false;
    }

    @Override
    public void update() {

        if (checkClicked() && !engine.getInput().getHasClicked()) {
            if (!clicked) {
                clicked = true;
                engine.getInput().setHasClicked(true);

                action.exec();
            }
        }
        else {
            if (clicked) {
                clicked = false;
            }
        }
    }

    @Override
    public void render() {
        if (image != null) {
            engine.getRenderer().drawImage(image, posX, posY);
        }
        else if (label != null) {
            // ajouter margin pour texte
            engine.getRenderer().drawText(engine.getStandardFont(), label, posX, posY, labelColor);
        }
    }

    public void setAction(Action action) { this.action = action; }
}
