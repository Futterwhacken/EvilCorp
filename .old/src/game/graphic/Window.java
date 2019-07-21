package game.graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

   private JFrame frame;
   private BufferedImage image;
   private Canvas canvas;
   private BufferStrategy bufferStrategy;
   private Graphics graphics;

   public  Window(GameLoop gameLoop) {

      image = new BufferedImage(gameLoop.getWidth(), gameLoop.getHeight(), BufferedImage.TYPE_INT_RGB);
      canvas = new Canvas();
      Dimension dim = new Dimension((int) (gameLoop.getWidth() * gameLoop.getScale()), (int) (gameLoop.getHeight() * gameLoop.getScale()));
      canvas.setPreferredSize(dim);
      canvas.setMaximumSize(dim);
      canvas.setMinimumSize(dim);

      frame = new JFrame(gameLoop.getTitle());
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      frame.add(canvas, BorderLayout.CENTER);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setVisible(true);

      canvas.createBufferStrategy(2);
      bufferStrategy = canvas.getBufferStrategy();
      graphics = bufferStrategy.getDrawGraphics();
   }

   public void update() {
      graphics.drawImage(image,0, 0, canvas.getWidth(), canvas.getHeight(), null);
      bufferStrategy.show();
   }

   public BufferedImage getImage() {
      return image;
   }

   public Canvas getCanvas() {
      return canvas;
   }

}