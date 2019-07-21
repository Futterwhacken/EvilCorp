package game.graphic;

import game.graphic.gfx.Font;
import game.graphic.gfx.Image;

import java.awt.event.MouseEvent;

public class Mouseable {

    private int x1, y1, x2, y2;

    /*    (x1, y1)  ___________   (x2, y1)
     *             |           |
     *             |           |
     *    (x1, y2) |___________|  (x2, y2)
     */


    public Mouseable(int x1, int y1, int x2, int y2) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }


}
