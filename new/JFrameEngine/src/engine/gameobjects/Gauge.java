package engine.gameobjects;

import engine.GameContainer;

public class Gauge extends GameObject
{
    //private String label; // ??
    private int color;
    private double level;
    private final double max = 100;

    private int posX;
    private int posY;
    private int width;
    private int height;
    private int maxHeight;

    // gerer jauge horizontale, boolean
    public Gauge(GameContainer gc, double level, int posX, int posY, int width, int maxHeight, int color) {
        super(gc);

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
        gc.getRenderer().drawRectangle(posX, posY + (maxHeight - height), width, height, color);
    }

    public void updateLevel(double amount) {
        level += amount;
    }

    public double getLevel() { return level; }
    public void setLevel(double level) { this.level = level; }
}
