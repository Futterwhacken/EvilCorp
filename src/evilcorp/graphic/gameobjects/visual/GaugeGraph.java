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

    private double level;
    private int dimensionDynamic;

    private Action action;

    public GaugeGraph(Engine engine, int posX, int posY, int width, int height, boolean horizontal, double level, double max, int color) {
        super(engine);

        this.posX = posX;
        this.posY = posY;

        this.horizontal = horizontal;
        this.level = level;
        this.max = max;
        this.color = color;

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

    public GaugeGraph(Engine engine, int posX, int posY, int width, int height, double max, int color) {
        this(engine, posX, posY, width, height, true, 0, max, color);
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
        if (horizontal)
            engine.getRenderer().drawRectangle(posX, posY, dimensionDynamic, dimensionStatic, color);
        else
            engine.getRenderer().drawRectangle(posX, posY, dimensionStatic, dimensionDynamic, color);

    }

    public double getLevel() { return level; }
    public void setLevel(double level) { this.level = level; }

    public void setAction(Action action) { this.action = action; }
}
