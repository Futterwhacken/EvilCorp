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

    // to create template menu
    public Menu(int posX, int posY, int fieldHeight, int color) {
        this(posX, posY, fieldHeight, null, null, null, color);
    }

    // to create menu from template
    public Menu(Menu template, String label, String[] options, Action[] actions) {
        this(template.posX, template.posY, template.fieldHeight, label, options, actions, template.color);
    }

    public Menu(int posX, int posY, int fieldHeight, String label, String[] options, Action[] actions, int color) {
        super();

        this.posX = posX;
        this.posY = posY;
        this.fieldHeight = fieldHeight;
        this.color = color;
        this.label = label;

        if (actions != null) {
            this.buttons = new Button[actions.length];

            for (int i = 0; i < actions.length; i++) {
                buttons[i] = new Button(posX, posY + i * fieldHeight, actions[i], options[i], color);
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

    public static Menu buildAddExploitationMenu(String label, Menu template) {
        Engine engine = Engine.getEngine();
        String[] options = new String[]{
                "add primary exploitation ("+ engine.getCurrentScene().getSelectedRegion().getPrimaryCost()+" $)",
                "add secondary exploitation ("+engine.getCurrentScene().getSelectedRegion().getSecondaryCost()+" $)",
                "add tertiary exploitation ("+engine.getCurrentScene().getSelectedRegion().getTertiaryCost()+" $)",
        };
        Action[] actions = new Action[]{
                () -> engine.getCurrentScene().getSelectedRegion().buyExploitation(0),
                () -> engine.getCurrentScene().getSelectedRegion().buyExploitation(1),
                () -> engine.getCurrentScene().getSelectedRegion().buyExploitation(2),
        };

        return buildReturnableMenu(options, actions, label, template);
    }


    public static Menu buildRemoveExploitationMenu(String label, Menu template) {
        Engine engine = Engine.getEngine();
        String[] options = new String[engine.getCurrentScene().getSelectedRegion().getExploitations().size()];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length; i++) {
            int a = i;
            options[i] = engine.getCurrentScene().getSelectedRegion().getExploitations().get(i).toString()+" ("+engine.getCurrentScene().getSelectedRegion().getExploitations().get(i).getRemoveCost() + " $)";
            actions[i] = () -> {
                engine.getCurrentScene().getSelectedRegion().removeExploitation(a);
                engine.getCurrentScene().setCurrentMenu(buildRemoveExploitationMenu(label, template));
            };
        }
        return buildReturnableMenu(options, actions, label, template);
    }

    public static Menu buildBuyEventMenu(String label, Menu template) {
        Engine engine = Engine.getEngine();
        String[] options = new String[engine.getCurrentScene().getSelectedRegion().getBuyableEvents().size()];
        Action[] actions = new Action[options.length];

        for (int i = 0; i < options.length; i++) {
            int a = i;
            options[i] = engine.getCurrentScene().getSelectedRegion().getBuyableEvents().get(i).getName();
            actions[i] = () -> {
                if (engine.getCurrentScene().getSelectedRegion().buyEvent(engine.getCurrentScene().getSelectedRegion().getBuyableEvents().get(a)))
                    engine.getCurrentScene().setCurrentMenu(engine.getCurrentScene().getMainMenu());
            };
        }
        return buildReturnableMenu(options, actions, label, template);
    }

    public static Menu buildReturnableMenu(String[] options, Action[] actions, String label, Menu template) {
        Engine engine = Engine.getEngine();
        String[] finalOptions = new String[options.length + 2];
        Action[] finalActions = new Action[options.length + 2];

        for (int i = 0; i < options.length; i++) {
            finalOptions[i] = options[i];
            finalActions[i] = actions[i];
        }

        finalOptions[finalOptions.length - 2] = " ";
        finalActions[finalActions.length - 2] = () -> {};
        finalOptions[finalOptions.length - 1] = "RETURN";
        finalActions[finalActions.length - 1] = () -> engine.getCurrentScene().setCurrentMenu(engine.getCurrentScene().getMainMenu());

        return new Menu(template, label, finalOptions, finalActions);
    }
}
