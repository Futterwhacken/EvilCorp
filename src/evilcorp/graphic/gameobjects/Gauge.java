package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;

public class Gauge extends GameObject
{
    //private String label; // ??
    private final int color;
    private final double max = 100;

    private final int posX;
    private final int posY;
    private final int width;
    private final int maxHeight;

    private double level;
    private int height;

    // gerer jauge horizontale, boolean
    public Gauge(Engine engine, double level, int posX, int posY, int width, int maxHeight, int color) {
        super(engine);

        this.level = level;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = (int)(maxHeight * level/max);
        this.maxHeight = maxHeight;
        this.color = color;

    }

    @Override
    public void update() { // to use only when nextTurn
        height = (int)(maxHeight * level/max);
    }

    @Override
    public void render() {
        engine.getRenderer().drawRectangle(posX, posY + (maxHeight - height), width, height, color);
    }

    public double getLevel() { return level; }
    public void setLevel(double level) { this.level = level; }
    public void updateLevel(double amount) { level += amount; }
}
