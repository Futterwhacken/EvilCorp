package evilcorp.graphic;

import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.Menu;

import evilcorp.graphic.gfx.Font;

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
    private final float scale;
    private final String title;

    private Font standardFont;

    private ArrayList<GameObject> gameObjects;

    private Region selectedRegion; // getSelectedRegion depuis runtime pour programmer les actions

    private Menu mainMenu;
    private Menu currentMenu;
    private Menu previousMenu;

    /* make singleton, initialized with config path, constructor loads config */

    public Engine(String dataPath) {
        this.gameObjects = new ArrayList<GameObject>();

        // load configuration and resources here

        // debug
        this.updateRate = 1.0/30.0;
        this.width = 640;
        this.height = 360;
        this.scale = 2;
        this.title = "EvilCorp";

        this.standardFont = new Font("/resources/fonts/standard_font.png");

        // ============================

        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);
        thread = new Thread(this);
    }

    public void start() {
        thread.run();
    }

    public void stop() {

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

                if (debug)
                    renderer.drawText(standardFont, "FPS : " + fps, 0,0,0xff00ff00);

                // render game
                for (GameObject o : gameObjects) {
                    o.render();
                }

                if (currentMenu != null && selectedRegion != null)
                    currentMenu.render();

                window.update(); // displays buffered image
                frames++;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        dispose();
    }

    private void dispose() {}

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getScale() { return scale; }
    public String getTitle() { return title; }

    public Window getWindow() { return window; }
    public Renderer getRenderer() { return renderer; }
    public Input getInput() { return input; }

    // ============================


    public Font getStandardFont() { return standardFont; }

    public void addGameObject(GameObject o) { gameObjects.add(o); }

    public Region getSelectedRegion() { return selectedRegion; }
    public void setSelectedRegion(Region region) { this.selectedRegion = region; }

    public Menu getMainMenu() { return mainMenu; }
    public void setMainMenu(Menu menu) { mainMenu = menu; }

    public Menu getCurrentMenu() { return currentMenu; }
    public void setCurrentMenu(Menu menu) {
        previousMenu = currentMenu;
        currentMenu = menu;
    }

    public Menu getPreviousMenu() { return previousMenu; }
    public void setPreviousMenu(Menu menu) { previousMenu = menu; }
}