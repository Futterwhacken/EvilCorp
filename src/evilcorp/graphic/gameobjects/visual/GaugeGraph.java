package evilcorp.graphic.gameobjects.visual;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;

public class GaugeGraph extends GameObject
{
    private final boolean horizontal;

    private final int posX;
    private final int posY;
    private final int color;

    private final int dimensionStatic;
    private final int dimensionDynamicMax;

    private final double max;

    private final int borderSize;
    private final int borderColor;

    private double level;
    private int dimensionDynamic;

    private Action action;

    public GaugeGraph(Engine engine, int posX, int posY, int width, int height, boolean horizontal, double level, double max, int borderSize, int color, int borderColor) {
        super(engine);

        this.posX = posX;
        this.posY = posY;

        this.horizontal = horizontal;
        this.level = level;
        this.max = max;
        this.color = color;

        this.borderSize = borderSize;
        this.borderColor = borderColor;

        if (horizontal) {
            this.dimensionStatic = height;
            this.dimensionDynamicMax = width;
        }
        else {
            this.dimensionStatic = width;
            this.dimensionDynamicMax = height;
        }
        this.dimensionDynamic = (int)(dimensionDynamicMax * level/max);
    }

    @Override
    public void update() {
        if (action != null) {
            action.exec();
        }
        dimensionDynamic = (int)(dimensionDynamicMax * level/max);
    }

    @Override
    public void render() {
        if (horizontal) {
            engine.getRenderer().drawRectangle(posX, posY, dimensionDynamic, dimensionStatic, color);
            engine.getRenderer().drawRectangle(posX - borderSize, posY, borderSize, dimensionStatic, borderColor);
            engine.getRenderer().drawRectangle(posX + dimensionDynamicMax + borderSize, posY, borderSize, dimensionStatic, borderColor);
        }
        else {
            engine.getRenderer().drawRectangle(posX, posY, dimensionStatic, dimensionDynamic, color);
            engine.getRenderer().drawRectangle(posX, posY - borderSize, dimensionStatic, borderSize, borderColor);
            engine.getRenderer().drawRectangle(posX, posY + dimensionDynamicMax + borderSize, dimensionStatic, borderSize, borderColor);
        }


    }

    public double getLevel() { return level; }
    public void setLevel(double level) { this.level = level; }

    public void setAction(Action action) { this.action = action; }
}
