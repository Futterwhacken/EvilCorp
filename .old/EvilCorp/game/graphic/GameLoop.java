package game.graphic;

import game.graphic.gfx.Image;
import game.graphic.Display;

public class GameLoop implements Runnable {

    private Thread thread;
    private Window window;
    private Display display;
    private Input input;

	private int fps = 0;

	private int compteur = 0;

    private AbstractGame game;

    private boolean running = false;
    private final double HERTZ = 1.0 / 60.0;

    private final static int width = 500, height = 300;
    private final static float scale = 3f;
    private final String title = "EvilCorp";

    public GameLoop(AbstractGame game) {
        this.game = game;
    }

    public void start() {
        window = new Window(this);
        display = new Display(this);
        input = new Input(this);

        thread = new Thread(this);
        thread.run();
    }

    public void stop() {

    }

    public void run() {
        running = true;

        double currentTime = 0;
        double previousTime = System.nanoTime() / 1000000000.0;
        double unprocessedTime = 0;

        double framesTime = 0;
        int frames = 0;


        while (running) {
            boolean render = false;

            currentTime = System.nanoTime() / 1000000000.0;
            unprocessedTime += currentTime - previousTime;
            framesTime += currentTime - previousTime;
            previousTime = currentTime;


            while (unprocessedTime >= HERTZ) {

                unprocessedTime -= HERTZ;
                render = true;

                game.update(this, HERTZ);

                window.update();


                if (framesTime > 1.0) {
					framesTime = 0;
                    fps = frames;
                    frames = 0;

					input.update();

                    System.out.println("FPS: "+ fps);
                }
            }

            if (render) {
                display.clear();

                display.drawText("FPS: " + fps, 0, 00, 0xff0000ff);

                game.display(this, display);

                window.update();
                frames++;
            }
            else {
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        dispose();
    }

    private void dispose() {

    }

	public int getFps() {
		return fps;
	}

	public double getHERTZ() {
		return HERTZ;
	}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }

    public String getTitle() {
        return title;
    }

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public Display getDisplay() {
        return display;
    }
}
