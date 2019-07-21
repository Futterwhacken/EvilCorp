package evilcorp.graphic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Window
{
    private JFrame frame;
    private BufferedImage image;
    private BufferStrategy buffer;
    private Canvas canvas;
    private Graphics graphics;

    public Window(Engine engine) {
        image = new BufferedImage(engine.getWidth(), engine.getHeight(), BufferedImage.TYPE_INT_RGB);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension((int)(engine.getWidth() * engine.getScale()), (int)(engine.getHeight() * engine.getScale())));

        frame = new JFrame(engine.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        //frame.setSize(new Dimension((int)(engine.getWidth() * engine.getScale())+50, (int)(engine.getHeight() * engine.getScale())+50));

        canvas.createBufferStrategy(1);
        buffer = canvas.getBufferStrategy();
        graphics = buffer.getDrawGraphics();
    }

    public void update()
    {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        buffer.show();
    }

    public BufferedImage getImage() { return image; }
    public Canvas getCanvas() { return canvas; }
}
