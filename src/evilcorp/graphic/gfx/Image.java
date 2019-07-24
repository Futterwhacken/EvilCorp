package evilcorp.graphic.gfx;

import evilcorp.graphic.Input;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Image
{
    private int width;
    private int height;
    private int[] pixels;

    public Image(String path, double scale) {
        BufferedImage image;
        try {
            image = ImageIO.read(new FileInputStream(path));
            if (scale != 1)
                image = scale(image, scale);

            width = image.getWidth();
            height = image.getHeight();
            pixels = image.getRGB(0, 0, width, height, null, 0, width);

            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image(String path) {
        this(path, 1);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[] getPixels() { return pixels; }

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }

    private BufferedImage scale(BufferedImage image, double scale) {

        int scaledWidth = (int)(image.getWidth() * scale);
        int scaleHeight = (int)(image.getHeight() * scale);

        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, scaledWidth, scaleHeight, null);

        graphics2D.dispose();

        return scaledImage;
    }

}
