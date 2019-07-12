package game.graphic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements MouseListener, MouseMotionListener {

	private GameLoop gameLoop;

	private final int NUM_BUTTONS = 5;
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

	private int mouseX, mouseY;

	public Input(GameLoop gameLoop)
	{
		this.gameLoop = gameLoop;
		mouseX = 0;
		mouseY = 0;

		gameLoop.getWindow().getCanvas().addMouseMotionListener(this);
		gameLoop.getWindow().getCanvas().addMouseListener(this);
	}

	public void update()
	{
		for (int i = 0; i < NUM_BUTTONS; i++) {
			buttonsLast[i] = buttons[i];
		}
	}

	public boolean isButton(int button) {
		return buttons[button];
	}

	public boolean isButtonUp(int button) {
		return !buttons[button] && buttonsLast[button];
	}

	public boolean isButtonDown(int button) { return buttons[button] && !buttonsLast[button]; }


	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = (int) (e.getX() / gameLoop.getScale());
		mouseY = (int) (e.getY() / gameLoop.getScale());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = (int) (e.getX() / gameLoop.getScale());
		mouseY = (int) (e.getY() / gameLoop.getScale());
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}
}
