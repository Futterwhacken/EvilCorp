package evilcorp.graphic.gameobjects.text;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gfx.Font;

public class Text extends GameObject
{
    private String text;
    private Action action;

    private final Font font;

    private final int posX;
    private final int posY;
    private final int color;

    public Text(int posX, int posY, String text, int color, Font font) {
        super();

        this.text = text;
        this.font = font;

        if (posX == -1) {
            this.posX = (int)(0.5*(engine.getWidth() - font.textLength(text)));
        }
        else {
            this.posX = posX;
        }

        this.posY = posY;
        this.color = color;
    }

    public Text() {
        this(0, 0, "", 0, Engine.getEngine().getStandardFont());
    }

    public Text(int color) {
        this(0, 0, "", color, Engine.getEngine().getStandardFont());
    }



    @Override
    public void update() {
        if (action != null)
            action.exec();
    }

    @Override
    public void render() {
        engine.getRenderer().drawText(font, text, posX, posY, color);
    }

    public void setText(String text) { this.text = text; }
    public void setAction(Action action) { this.action = action; }

    public String getText() { return text; }

    public int getColor() { return color; }
}
