package engine.gfx;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Image
{
    private int width;
    private int height;
    private int[] pixels;

    public Image(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(path));

            width = image.getWidth();
            height = image.getHeight();
            pixels = image.getRGB(0, 0, width, height, null, 0, width);

            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[] getPixels() { return pixels; }

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setPixels(int[] pixels) { this.pixels = pixels; }
}
