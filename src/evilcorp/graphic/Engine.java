package evilcorp.graphic;

import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.Menu;

import evilcorp.graphic.gfx.Font;

import evilcorp.logic.GameMaster;
import evilcorp.logic.area.region.Region;

import java.util.ArrayList;

public class Engine implements Runnable
{
    private final boolean debug = true;

    private final Thread thread;
    private final Window window;
    private final Renderer renderer;
    private final Input input;

    private final double updateRate;
    private final int width;
    private final int height;
    private final double scale;
    private final String title;

    private final Font standardFont;

    private final ArrayList<GameObject> gameObjects;

    private final GameMaster gm;
    private Region selectedRegion;

    private Menu mainMenu;
    private Menu currentMenu;

    /* make singleton */

    public Engine(String dataPath, GameMaster gm) {
        this.gm = gm;

        this.gameObjects = new ArrayList<>();

        // debug
        this.updateRate = 1.0/30.0;
        this.width = 1280;
        this.height = 720;
        this.scale = 1;
        this.title = "EvilCorp";

        this.standardFont = new Font("/resources/fonts/standard_font.png", 7, 1);

        // ============================

        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);
        thread = new Thread(this);
    }

    public void start() {
        thread.run();
    }

    @Override
    public void run() {
        boolean isRunning = true;
        boolean render;

        double currentTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        while (isRunning) {
            render = false;

            currentTime = System.nanoTime() / 1000000000.0;
            passedTime = currentTime - lastTime;
            lastTime = currentTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= updateRate) { // every updateRate: update game
                render = true;
                unprocessedTime -= updateRate;

                // update game

                input.update();

                for (GameObject o : gameObjects) {
                    o.update();
                }

                if (currentMenu != null && selectedRegion != null)
                    currentMenu.update();

                if (frameTime >= 1.0) { // every 1 sec count frames
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }

            }

            if (render) {
                renderer.clear();

                if (debug) {
                    renderer.drawText(standardFont, "FPS:" + fps, 0, 2, 0xff00ff00);
                    renderer.drawText(standardFont, "X: " + input.getMouseX() + ", Y: " + input.getMouseY(), 70, 2, 0xff00ff00);
                }

                // render game
                for (GameObject o : gameObjects) {
                    o.render();
                }

                if (currentMenu != null && selectedRegion != null)
                    currentMenu.render();

                window.update(); // displays buffered image
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getScale() { return scale; }
    public String getTitle() { return title; }

    public double getUpdateRate() { return updateRate; }

    public Window getWindow() { return window; }
    public Renderer getRenderer() { return renderer; }
    public Input getInput() { return input; }

    // ============================

    public void addGameObject(GameObject o) { gameObjects.add(o); } // ?

    public Font getStandardFont() { return standardFont; }

    public Region getSelectedRegion() { return selectedRegion; }
    public void setSelectedRegion(Region region) {
        this.selectedRegion = region;
        currentMenu = mainMenu;
    }

    public Menu getMainMenu() { return mainMenu; }
    public void setMainMenu(Menu menu) { mainMenu = menu; }

    public Menu getCurrentMenu() { return currentMenu; }
    public void setCurrentMenu(Menu menu) {
        /* ptn ça c'est vraiment moche j'aime pas ! */
        if (currentMenu != null) {
            for (Button b : currentMenu.getButtons()) {
                b.execUnHoverAction();
            }
        }
        currentMenu = menu;
    }

    public Region getRegion(String regionName) {
        for (Region r : gm.getWorld().getRegions()) {
            if (r.getName().equals(regionName)) {
                return r;
            }
        }
        return null;
    }

}
