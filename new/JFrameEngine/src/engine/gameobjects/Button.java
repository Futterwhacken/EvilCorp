package engine.gameobjects;

import engine.GameContainer;
import engine.gfx.Image;

public class Button extends GameObject
{
    /* dans le runtime avoir une array avec tous les boutons pour les updater et render */
    private int posX;
    private int posY;
    private int width;
    private int height;

    private boolean clicked = false;

    private Action action;
    private Image image;
    private String label;

    public Button(GameContainer gc, int posX, int posY, int width, int height, Action action, Image image, String label) {
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

        this.label = label;
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

    @Override
    public void render() {
        if (image != null) {
            gc.getRenderer().drawImage(image, posX, posY);
        }
        else if (label != null) {
            // standardiser font, ajouter margin pour texte
            gc.getRenderer().drawText(gc.getStandardFont(), label, posX, posY, 0xffffffff);
        }
    }

    public void setAction(Action action) { this.action = action; }

}
