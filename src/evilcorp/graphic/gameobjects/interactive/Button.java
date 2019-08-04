package evilcorp.graphic.gameobjects.interactive;

import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gfx.Font;

public class Button extends GameObject
{
    private final int posX;
    private final int posY;
    private final int width;
    private final int height;

    private Action action;
    private Action hoverAction;
    private Action unHoverAction;

    private final String label;
    private final int labelColor;
    private final Font font;

    private boolean hovered = false;
    private boolean clicked = false;

    private Button(int posX, int posY, int width, int height, Action action, String label, int labelColor, Font font) {


        if (label != null && posX == -1) {
            this.posX = (int)(0.5*(engine.getWidth() - font.textLength(label)));
        }
        else {
            this.posX = posX;
        }

        this.posY = posY;
        this.width = width;
        this.height = height;

        this.action = action;

        this.label = label;
        this.labelColor = labelColor;
        this.font = font;
    }

    // string label
    public Button(int posX, int posY, Action action, String label, int labelColor, Font font) {
        this(posX, posY, font.textLength(label), font.getCharHeight(), action, label, labelColor, font);
    }
    public Button(int posX, int posY, String label, int labelColor, Font font) {
        this(posX, posY, font.textLength(label), font.getCharHeight(), null, label, labelColor, font);
    }

    // invisible
    public Button(int posX, int posY, int width, int height, Action action) {
        this(posX, posY, width, height, action, null, 0, null);
    }


    private boolean checkClicked() {

        int mouseX = engine.getInput().getMouseX();
        int mouseY = engine.getInput().getMouseY();

        if ((mouseX >= posX && mouseX < posX + width) && (mouseY >= posY && mouseY < posY + height)) {
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

                if (action != null)
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
        if (label != null) {
            //engine.getRenderer().drawRectangle(posX, posY, width, height, 0xFFFF0000); // debug
            engine.getRenderer().drawText(font, label, posX, posY, labelColor);
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
