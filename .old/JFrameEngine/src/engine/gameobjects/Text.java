package engine.gameobjects;

import engine.Engine;

public class Text extends GameObject
{
    private String text;
    private Action action;
    private int posX;
    private int posY;
    private int color;

    public Text(Engine engine, String text, Action action, int posX, int posY, int color) {
        super(engine);

        this.text = text;
        this.action = action;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
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
}
