package evilcorp.graphic;

import evilcorp.graphic.config.ConfigReader;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.Menu;

import evilcorp.graphic.gfx.Font;

import evilcorp.logic.GameMaster;
import evilcorp.logic.area.region.Region;
import evilcorp.logic.config.Config;

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

    private final ArrayList<GameObject> gameObjects;

    private final String title;
    private final int width;
    private final int height;
    private final double scale;
    private final double updateRate;

    private final boolean debug;

    private Font standardFont;

    private Region selectedRegion;
    private Menu mainMenu;
    private Menu currentMenu;

    private Engine(String title, int width, int height, double scale, double updateCap, boolean debug) {

        this.gameObjects = new ArrayList<>();

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

    public String getTitle() { return title; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getScale() { return scale; }
    public double getUpdateRate() { return updateRate; }

    public Window getWindow() { return window; }
    public Renderer getRenderer() { return renderer; }
    public Input getInput() { return input; }

    // ============================

    public void addGameObject(GameObject o) { gameObjects.add(o); } // ?

    public Font getStandardFont() { return standardFont; }
    public void setStandardFont(Font font) { standardFont = font; }

    public Region getSelectedRegion() { return selectedRegion; }
    public void setSelectedRegion(Region region) {
        this.selectedRegion = region;
        currentMenu = mainMenu;
    }

    public Menu getMainMenu() { return mainMenu; }
    public void setMainMenu(Menu menu) {
        mainMenu = menu;
        setCurrentMenu(mainMenu);
    }

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
        for (Region r : GameMaster.getGameMaster().getWorld().getRegions()) {
            if (r.getName().equals(regionName)) {
                return r;
            }
        }
        return null;
    }

    public Menu buildAddExploitationMenu(String label, Menu template) {
        String[] options = new String[]{
                "add primary exploitation ("+ Config.PrimaryCost+" $)",
                "add secondary exploitation ("+Config.SecondaryCost+" $)",
                "add tertiary exploitation ("+Config.TertiaryCost+" $)",
        };
        Action[] actions = new Action[]{
                () -> selectedRegion.buyExploitation(0),
                () -> selectedRegion.buyExploitation(1),
                () -> selectedRegion.buyExploitation(2),
        };

        return buildReturnableMenu(options, actions, label, template);
    }

    public Menu buildRemoveExploitationMenu(String label, Menu template) {
        String[] options = new String[selectedRegion.getExploitations().size()];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length; i++) {
            int a = i;
            options[i] = selectedRegion.getExploitations().get(i).toString()+" ("+selectedRegion.getExploitations().get(i).getRemoveCost() + " $)";
            actions[i] = () -> {
                selectedRegion.removeExploitation(a);
                setCurrentMenu(buildRemoveExploitationMenu(label, template));
            };
        }
        return buildReturnableMenu(options, actions, label, template);
    }

    public Menu buildBuyEventMenu(String label, Menu template) {
        String[] options = new String[selectedRegion.getBuyableEvents().size()];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length; i++) {
            int a = i;
            options[i] = selectedRegion.getBuyableEvents().get(i).getName();
            actions[i] = () -> {
                if (selectedRegion.buyEvent(getSelectedRegion().getBuyableEvents().get(a)))
                    setCurrentMenu(getMainMenu());
            };
        }
        return buildReturnableMenu(options, actions, label, template);
    }

    public Menu buildReturnableMenu(String[] options, Action[] actions, String label, Menu template) {
        String[] finalOptions = new String[options.length + 2];
        Action[] finalActions = new Action[options.length + 2];

        for (int i = 0; i < options.length; i++) {
            finalOptions[i] = options[i];
            finalActions[i] = actions[i];
        }

        finalOptions[finalOptions.length - 2] = " ";
        finalActions[finalActions.length - 2] = () -> {};
        finalOptions[finalOptions.length - 1] = "RETURN";
        finalActions[finalActions.length - 1] = () -> setCurrentMenu(getMainMenu());

        return new Menu(this, template, label, finalOptions, finalActions);
    }


}
