package evilcorp.graphic.gameobjects.interactive;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gfx.Image;

public class Button extends GameObject
{
    private final int posX;
    private final int posY;
    private final int width;
    private final int height;

    private Action action;
    private Action hoverAction;
    private Action unHoverAction;

    private final Image image;
    private final String label;
    private final int labelColor;

    private boolean hovered = false;
    private boolean clicked = false;

    private Button(Engine engine, int posX, int posY, int width, int height, Action action, Image image, String label, int labelColor) {
        super(engine);

        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.action = action;

        this.image = image;

        this.label = label;
        this.labelColor = labelColor;
    }

    // image
    public Button(Engine engine, int posX, int posY, Action action, Image image) {
        this(engine, posX, posY, image.getWidth(), image.getHeight(), action, image, null, 0);
    }

    // string label
    public Button(Engine engine, int posX, int posY, int width, int height, Action action, String label, int labelColor) {
        this(engine, posX, posY, width, height, action, null, label, labelColor);
    }

    // invisible
    public Button(Engine engine, int posX, int posY, int width, int height, Action action) {
        this(engine, posX, posY, width, height, action, null, null, 0);
    }


    private boolean checkClicked() {

        int mouseX = engine.getInput().getMouseX();
        int mouseY = engine.getInput().getMouseY();

        if ((mouseX >= posX && mouseX <= posX + width) && (mouseY >= posY && mouseY <= posY + height)) {
            hovered = true;
            return engine.getInput().isButton(1); // 1 = left click, tomod ?
        }

        hovered = false;
        return false;
    }

    @Override
    public void update() {

        if (hoverAction != null) {
            hoverAction.exec();
        }

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
    public void setHoverAction(Action hoverAction, Action unHoverAction) { this.hoverAction = hoverAction; this.unHoverAction = unHoverAction; }
    public Action getHoverAction() { return hoverAction; }

    public void execUnHoverAction() {
        if (unHoverAction != null)
            unHoverAction.exec();
    }
    public boolean isHovered() { return hovered; }
}