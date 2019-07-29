package evilcorp.graphic.gameobjects.text;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gfx.Font;

public class Text extends GameObject
{
    private String text;
    private Action action;
    private final String defaultText;
    private final Font font;

    private final int posX;
    private final int posY;
    private final int color;

    public Text(Engine engine, int posX, int posY, String text, Action action, int color, Font font) {
        super(engine);

        this.text = text;
        this.defaultText = text;
        this.action = action;
        this.font = font;

        this.posX = posX;
        this.posY = posY;
        this.color = color;
    }

    public Text(Engine engine) {
        this(engine, 0xffffffff);
    }

    public Text(Engine engine, int color) {
        this(engine, 0, 0, "", color);
    }

    public Text(Engine engine, String text, int color) {
        this(engine, 0, 0, text, null, color, engine.getStandardFont());
    }

    public Text(Engine engine, int posX, int posY, int color) {
        this(engine, posX, posY, "", color);
    }

    public Text(Engine engine, int posX, int posY, String text, int color) {
        this(engine, posX, posY, text, null, color, engine.getStandardFont());
    }

    public Text(Engine engine, int posX, int posY, String text, int color, Font font) {
        this(engine, posX, posY, text, null, color, font);
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
    public String getDefaultText() { return defaultText; }

    public int getColor() { return color; }
}
