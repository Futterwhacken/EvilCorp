package evilcorp.graphic.gameobjects.interactive;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.visual.Visual;
import evilcorp.graphic.gfx.Image;

import evilcorp.logic.GameMaster;
import evilcorp.logic.area.region.Region;

public class Map extends GameObject
{
    private final Visual map;
    private final Button[] buttons;

    public Map(Engine engine, int posX, int posY, int width, int height, double scale, String[] names, String[] imagesPath, int[][] params) {
        super(engine);

        if (posX == -1) {
            posX = (int)(0.5*(engine.getWidth() - (width * scale)));
        }

        this.map = new Visual(engine, posX, posY, new Image(imagesPath[0], scale));
        this.buttons = new Button[names.length];

        for (int i = 0; i < names.length; i++) {
            int[] current = params[i];
            int a = i;

            buttons[i] = new Button(engine, (int)(posX + current[0] * scale),(int)(posY + current[1] * scale), (int)(current[2] * scale), (int)(current[3] * scale),
                    () -> {
                        map.setImage(new Image(imagesPath[a+1], scale));
                        engine.getCurrentScene().setSelectedRegion(getRegion(names[a]));
                    });
        }
    }

    @Override
    public void update() {
        map.update();
        for (Button b : buttons) {
            b.update();
        }
    }

    @Override
    public void render() {
        map.render();
        for (Button b : buttons) {
            b.render();
        }
    }

    private Region getRegion(String regionName) {
        for (Region r : GameMaster.getGameMaster().getWorld().getRegions()) {
            if (r.getName().equals(regionName)) {
                return r;
            }
        }
        return null;
    }
}
