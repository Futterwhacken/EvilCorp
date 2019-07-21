package evilcorp.graphic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener
{
    private final Engine engine;

    private final int NUM_KEYS = 256;
    private final boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];

    private final int NUM_BUTTONS = 7;
    private final boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;

    private boolean hasClicked = false;

    public Input(Engine engine) {
        this.engine = engine;
        mouseX = 0;
        mouseY = 0;

        engine.getWindow().getCanvas().addKeyListener(this);
        engine.getWindow().getCanvas().addMouseListener(this);
        engine.getWindow().getCanvas().addMouseMotionListener(this);

    }

    public void update() {
        keysLast = keys.clone();
        buttonsLast = buttons.clone();
    }

    public boolean isKey(int keyCode) { return keys[keyCode]; }
    public boolean isKeyUp(int keyCode) { return !keys[keyCode] && keysLast[keyCode]; }
    public boolean isKeyDown(int keyCode) { return keys[keyCode] && !keysLast[keyCode]; }

    public boolean isButton(int buttonCode) { return buttons[buttonCode]; }
    public boolean isButtonUp(int buttonCode) { return !buttons[buttonCode] && buttonsLast[buttonCode]; }
    public boolean isButtonDown(int buttonCode) { return buttons[buttonCode] && !buttonsLast[buttonCode]; }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / engine.getScale());
        mouseY = (int)(e.getY() / engine.getScale());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / engine.getScale());
        mouseY = (int)(e.getY() / engine.getScale());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
        setHasClicked(false);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }

    public boolean[] getButtons() { return buttons; }
    public boolean[] getKeys() { return keys; }

    public boolean getHasClicked() { return hasClicked; }
    public void setHasClicked(boolean b) { hasClicked = b; }


    /* leftovers */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
