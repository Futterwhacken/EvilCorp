package engine.gameobjects;

import engine.Engine;
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

    public Button(Engine engine, int posX, int posY, int width, int height, Action action, Image image, String label) {
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
            engine.getRenderer().drawImage(image, posX, posY);
        }
        else if (label != null) {
            // standardiser font, ajouter margin pour texte
            engine.getRenderer().drawText(engine.getStandardFont(), label, posX, posY, 0xffffffff);
        }
    }

    public void setAction(Action action) { this.action = action; }

}
