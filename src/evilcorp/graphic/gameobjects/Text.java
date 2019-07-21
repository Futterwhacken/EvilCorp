package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;

public class Text extends GameObject
{
    private String text;
    private String defaultText;
    private Action action;

    private int posX;
    private int posY;
    private int color;

    private Text(Engine engine, String text, Action action, int posX, int posY, int color) {
        super(engine);

        this.text = text;
        this.defaultText = text;
        this.action = action;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
    }

    public Text(Engine engine) {
        this(engine, "", () -> {}, 0 ,0 ,0xffffffff);
    }

    public Text(Engine engine, String text, int posX, int posY, int color) {
        this(engine, text, null, posX, posY, color);
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
