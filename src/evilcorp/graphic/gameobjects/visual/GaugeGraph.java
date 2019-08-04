package evilcorp.graphic.gameobjects.visual;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.text.Text;
import evilcorp.graphic.gfx.Font;

public class GaugeGraph extends GameObject
{
    private final int posX;
    private final int posY;
    private final int gaugeColor;

    private final int height;
    private final int width;

    private final double max;

    private final int borderSize;
    private final Text label;
    private final Text counter;
    private final int textColor;

    private double level;
    private int currentWidth;

    private Action action;

    public GaugeGraph(int posX, int posY, int width, int height, double level, double max, int gaugeColor, int borderSize, String label, int textColor, Font font) {
        super();

        if (posX == -1) {
            this.posX = (Engine.getEngine().getWidth() - width)/2;
        }
        else {
            this.posX = posX;
        }

        this.posY = posY;

        this.level = level;
        this.max = max;
        this.gaugeColor = gaugeColor;

        this.borderSize = borderSize;
        this.textColor = textColor;

        this.height = height;
        this.width = width;

        this.currentWidth = (int)(width * level/max);

        int offsetY = (height-font.getCharHeight())/2;

        this.label = new Text(this.posX - (font.textLength(label) + font.getCharWidth()) - borderSize, posY + offsetY, label, textColor, font);
        this.counter = new Text(this.posX + width + font.getCharWidth() + borderSize, posY + offsetY, "", textColor, font);

        counter.setAction(() -> counter.setText(""+this.level));
    }

    @Override
    public void update() {
        if (action != null) {
            action.exec();
        }
        currentWidth = (int)(width * level/max);

        label.update();
        counter.update();
    }

    @Override
    public void render() {
        engine.getRenderer().drawRectangle(posX, posY, currentWidth, height, gaugeColor);
        engine.getRenderer().drawRectangle(posX - borderSize, posY, borderSize, height, textColor);
        engine.getRenderer().drawRectangle(posX + width + borderSize, posY, borderSize, height, textColor);

        label.render();
        counter.render();

    }

    public double getLevel() { return level; }
    public void setLevel(double level) { this.level = level; }

    public void setAction(Action action) { this.action = action; }
}
