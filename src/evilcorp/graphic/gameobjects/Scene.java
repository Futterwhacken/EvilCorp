package evilcorp.graphic.gameobjects;

import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.logic.area.region.Region;

import java.util.ArrayList;

public class Scene extends GameObject
{
    private Menu mainMenu;
    private Menu currentMenu;
    private Region selectedRegion;

    private final ArrayList<GameObject> gameObjects;

    private final ArrayList<Action> sceneActions;

    public Scene() {
        super();

        this.gameObjects = new ArrayList<>();
        this.sceneActions = new ArrayList<>();
    }

    @Override
    public void update() {

        if (currentMenu != null && selectedRegion != null) {
            currentMenu.update();
        }

        for (Action a : sceneActions) {
            a.exec();
        }

        for (GameObject o : gameObjects) {
            o.update();
        }
    }

    @Override
    public void render() {

        if (currentMenu != null && selectedRegion != null) {
            currentMenu.render();
        }

        for (GameObject o : gameObjects) {
            o.render();
        }
    }

    public void addGameObject(GameObject object) {
        if (object instanceof Menu) {
            setMainMenu((Menu)object);
            return;
        }

        gameObjects.add(object);
    }

    public void addAction(Action action) { sceneActions.add(action); }

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
        if (currentMenu != null) {
            for (Button b : currentMenu.getButtons()) {
                b.execUnHoverAction();
            }
        }
        currentMenu = menu;
    }
}
