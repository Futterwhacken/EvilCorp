package evilcorp.graphic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Window
{
    private Engine engine;
    private JFrame frame;
    private final BufferedImage image;
    private BufferStrategy buffer;
    private Canvas canvas;
    private Graphics graphics;

    public Window(Engine engine) {

        this.image = new BufferedImage(engine.getWidth(), engine.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.engine = engine;

        setup();
    }

    public void update()
    {
        //graphics.drawImage(image, 0, 0, null);
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        buffer.show();
    }

    public BufferedImage getImage() { return image; }
    public Canvas getCanvas() { return canvas; }

    public void setup() {
        if (frame != null)
            frame.dispose();

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension((int)(engine.getWidth() * engine.getScale()), (int)(engine.getHeight() * engine.getScale())));

        frame = new JFrame(engine.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas);

        frame.setResizable(false);

        if (engine.isFullscreen()) {
            frame.setUndecorated(true);
        }
        else {
            frame.setUndecorated(false);
        }

        frame.setVisible(true);
        frame.pack();

        frame.setLocationRelativeTo(null);

        canvas.createBufferStrategy(1);
        buffer = canvas.getBufferStrategy();
        graphics = buffer.getDrawGraphics();
    }
}
