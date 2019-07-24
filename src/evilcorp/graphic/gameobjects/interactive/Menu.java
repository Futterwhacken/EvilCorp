package evilcorp.graphic.gameobjects.interactive;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;

public class Menu extends GameObject
{

    private final int posX;
    private final int posY;
    private final int fieldHeight;
    private final int color;
    private final String label;
    private final Button[] buttons;

    public Menu(Engine engine, int posX, int posY, int fieldHeight, int color) {
        this(engine, posX, posY, fieldHeight, null, null, null, color);
    }

    public Menu(Engine engine, Menu template, String label, String[] options, Action[] actions) {
        this(engine, template.posX, template.posY, template.fieldHeight, label, options, actions, template.color);
    }

    public Menu(Engine engine, int posX, int posY, int fieldHeight, String label, String[] options, Action[] actions, int color) {
        super(engine);

        this.posX = posX;
        this.posY = posY;
        this.fieldHeight = fieldHeight;
        this.color = color;
        this.label = label;

        if (actions != null) {
            this.buttons = new Button[actions.length];

            // implement margin

            for (int i = 0; i < actions.length; i++) {
                buttons[i] = new Button(engine, posX, posY + i * fieldHeight, options[i].length() * engine.getStandardFont().getTextUnitWidth(), fieldHeight, actions[i], options[i], color);
            }
        }
        else {
            buttons = new Button[0];
        }
    }

    @Override
    public void update() {
        for (Button b : buttons) {
            b.update();
        }
    }

    @Override
    public void render() {
        if (label != null) {
            engine.getRenderer().drawText(engine.getStandardFont(), label, posX, posY - (int)(fieldHeight*1.5), color);
        }
        for (Button b : buttons) {
            b.render();
        }
    }

    public Button[] getButtons() { return buttons; }

    public boolean noneHovered() {
        for (Button b : buttons) {
            if (b.isHovered()) {
                return false;
            }
        }

        return true;
    }
}
