package game.graphic;

import game.graphic.gfx.Image;
import java.util.ArrayList;

public abstract class AbstractGame {

	protected Mouseable[] clicMouseableRegion;
	protected Mouseable[] clicMouseableMenu;
	protected Mouseable[] clicMouseableChoice;

	protected Mouseable clicMouseableNextTurn;
	protected Mouseable clicMouseableExit;

	protected boolean clic;

	protected ArrayList<Gauges> rendererGauges;
	protected static final int[] posXGauges = {200, 70, 125, 210, 290};
	protected static final int[] posYGauges = {50, 50, 120, 90, 65};

	protected ArrayList<String> immediateNotifications;
	protected ArrayList<String> waitingNotifications;

	protected String cursorZone;
	protected double cursorProd;
	protected double cursorVisi;
	protected double cursorSoc;

	protected String tour;

    protected ArrayList<Image> rendererImage;
    protected ArrayList<Integer> rendererXImage;
    protected ArrayList<Integer> rendererYImage;

    protected ArrayList<String> rendererText;
    protected ArrayList<Integer> rendererTextColor;
    protected ArrayList<Integer> rendererXText;
    protected ArrayList<Integer> rendererYText;

	protected ArrayList<String> rendererTextMenu;
	protected ArrayList<Integer> rendererTextColorMenu;
	protected ArrayList<Integer> rendererXTextMenu;
	protected ArrayList<Integer> rendererYTextMenu;

	protected ArrayList<String> rendererTextAddExploitation;
	protected ArrayList<Integer> rendererTextColorAddExploitation;
	protected ArrayList<Integer> rendererXTextAddExploitation;
	protected ArrayList<Integer> rendererYTextAddExploitation;

	protected ArrayList<String> rendererTextRemoveExploitation;
	protected ArrayList<Integer> rendererTextColorRemoveExploitation;
	protected ArrayList<Integer> rendererXTextRemoveExploitation;
	protected ArrayList<Integer> rendererYTextRemoveExploitation;

	protected ArrayList<String> rendererTextBuyAction;
	protected ArrayList<Integer> rendererTextColorBuyAction;
	protected ArrayList<Integer> rendererXTextBuyAction;
	protected ArrayList<Integer> rendererYTextBuyAction;




    public abstract void update(GameLoop gameLoop, double updateHertz);

    public abstract void display(GameLoop gameLoop, Display disp);
}
