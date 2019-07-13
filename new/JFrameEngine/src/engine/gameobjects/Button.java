package engine.gameobjects;

import engine.GameContainer;
import engine.gfx.Image;

public class Button extends GameObject
{
    private int posX;
    private int posY;
    private int width;
    private int height;

    private boolean clicked = false;

    private Action action;
    private Image image;

    public Button(GameContainer gc, int posX, int posY, int width, int height, Action action, Image image) {
        super(gc);

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
    }

    private boolean checkClicked() {

        int mouseX = gc.getInput().getMouseX();
        int mouseY = gc.getInput().getMouseY();

        if ((mouseX >= posX && mouseX <= posX + width) && (mouseY >= posY && mouseY <= posY + height)) {
            if (gc.getInput().isButton(1)) { // 1 = left click, tomod ?
                return true;
            }
        }

        return false;
    }

    @Override
    public void update() {

        if (checkClicked()) {
            if (!clicked) {
                clicked = true;

                action.exec();
            }
        }
        else {
            if (clicked) {
                clicked = false;
            }
        }
    }

    public void render() {
        gc.getRenderer().drawImage(image, posX, posY);
    }

    public void setAction(Action action) { this.action = action; }

}
