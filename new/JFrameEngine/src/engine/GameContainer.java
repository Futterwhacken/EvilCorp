package engine;


import engine.gfx.Image;
import engine.gfx.Font;
import engine.gameobjects.Button;
import engine.gameobjects.Action;

public class GameContainer implements Runnable
{
    // debug
    private Image image;
    private Font font;
    private Button button;
    // ==================

    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;

    private boolean isRunning = false;
    private final static double UPDATE_CAP = 1.0/60.0; // tomod ?

    // to mod
    private int width = 640, height = 360;
    private float scale = 2f;
    private String title = "EvilCorp";

    public GameContainer() {
        // debug
        image = new Image("/test.png"); // relatif à l'emplacement de Image.java
        font = new Font("/font.png");

        // =================
    }

    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);

        // ===== debug
        button = new Button(this, 100, 100, 50, 50, () -> { System.out.println("button clicked"); }, new Image("/button200.png"));
        //=============

        thread = new Thread(this);
        thread.run();
    }

    public void stop() {

    }

    @Override
    public void run() {
        isRunning = true;

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

            while (unprocessedTime >= UPDATE_CAP) { // every update cap update game
                render = true;
                unprocessedTime -= UPDATE_CAP;

                // update game

                input.update();

                // update clickables ?
                button.update();

                if (frameTime >= 1.0) { // every 1 sec count frames
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            if (render) {
                renderer.clear();

                // render game

                // debug
                // render clickables if they have an image
                button.render();


                renderer.drawText(font, "FPS: "+fps, 0, 0, 0xff00ff00, 2);
                //renderer.drawImage(image, input.getMouseX() - 150, input.getMouseY() - 150);
                // ===============

                window.update();
                frames++;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        dispose();
    }

    private void dispose() {

    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getScale() { return scale; }
    public String getTitle() { return title; }

    public Window getWindow() { return window; }
    public Renderer getRenderer() { return renderer; }
    public Input getInput() { return input; }

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setScale(float scale) { this.scale = scale; }
    public void setTitle(String title) { this.title = title; }
}
