package evilcorp.graphic;

import evilcorp.graphic.config.ConfigReader;
import evilcorp.graphic.gameobjects.Scene;

import evilcorp.graphic.gfx.Font;

import java.util.ArrayList;

public class Engine implements Runnable
{

    private static Engine instance = null;

    public static Engine initEngine(String configPath) {
        if (instance == null) {
            Object[] config = ConfigReader.readEngine(configPath+"engine.cfg");
            instance = new Engine(config[0].toString(), (int)config[1], (int)config[2], (double)config[3], (double)config[4], (boolean)config[5]);
        }
        return instance;
    }

    public static Engine getEngine() {
        return instance;
    }

    private final Thread thread;
    private final Window window;
    private final Renderer renderer;
    private final Input input;

    private final String title;
    private final int width;
    private final int height;
    private final double scale;
    private final double updateRate;

    private final boolean debug;

    private Font standardFont;
    private Font standardFontBig;

    private final ArrayList<Scene> scenes;
    private Scene currentScene;

    private Engine(String title, int width, int height, double scale, double updateCap, boolean debug) {
        this.scenes = new ArrayList<>();

        this.title = title;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.updateRate = 1.0/updateCap;

        this.debug = debug;

        this.window = new Window(this);
        this.renderer = new Renderer(this);
        this.input = new Input(this);
        this.thread = new Thread(this);

    }

    public void start() {
        if (currentScene == null) {
            currentScene = scenes.get(0);
        }
        thread.run();
    }

    public void stop() {
        System.exit(0);
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

                currentScene.update();

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

                currentScene.render();

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

    public String getTitle() { return title; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getScale() { return scale; }
    public double getUpdateRate() { return updateRate; }

    public Window getWindow() { return window; }
    public Renderer getRenderer() { return renderer; }
    public Input getInput() { return input; }

    public Font getStandardFont() { return standardFont; }
    public void setStandardFont(Font font) { standardFont = font; }

    public Font getStandardFontBig() { return standardFontBig; }
    public void setStandardFontBig(Font font) { standardFontBig = font; }

    public Scene getCurrentScene() { return currentScene; }
    public void setCurrentScene(Scene scene) {
        currentScene = scene;
    }
    public void nextScene() {
        for (int i = 0; i < scenes.size(); i++) {
            if (scenes.get(i) == currentScene && i+1 < scenes.size()) {
                setCurrentScene(scenes.get(i+1));
            }
        }
    }
    public void addScene(Scene scene) {
        scenes.add(scene);
    }

}
