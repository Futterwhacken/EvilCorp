package engine;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;
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


    public Window(GameContainer gc) {
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);

        Dimension size = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));

        canvas = new Canvas();
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);

        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
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
