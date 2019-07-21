package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;

public class Text extends GameObject
{
    private String text;
    private Action action;
    private final String defaultText;

    private final int posX;
    private final int posY;
    private final int color;

    public Text(Engine engine, int posX, int posY, String text, Action action, int color) {
        super(engine);

        this.text = text;
        this.defaultText = text;
        this.action = action;

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
        this(engine, 0, 0, text, null, color);
    }

    public Text(Engine engine, int posX, int posY, int color) {
        this(engine, posX, posY, "", color);
    }

    public Text(Engine engine, int posX, int posY, String text, int color) {
        this(engine, posX, posY, text, null, color);
    }


    @Override
    public void update() {
        if (action != null)
            action.exec();
    }

    @Override
    public void render() {
        engine.getRenderer().drawText(engine.getStandardFont(), text, posX, posY, color);
    }

    public void setText(String text) { this.text = text; }
    public void setAction(Action action) { this.action = action; }

    public String getText() { return text; }
    public String getDefaultText() { return defaultText; }

    public int getColor() { return color; }
}
