package engine;

import java.awt.image.DataBufferInt;

import engine.gfx.Image;
import engine.gfx.Font;

public class Renderer
{
    private int width, height;
    private int[] pixels;

    public Renderer(GameContainer gc)
    {
        width = gc.getWidth();
        height = gc.getHeight();
        pixels = ((DataBufferInt)(gc.getWindow().getImage().getRaster().getDataBuffer())).getData();
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public void setPixel(int x, int y, int value) {
        if ((x < 0 || x >= width || y < 0 || y >= height) || value == 0xffff00ff) return; // mod alpha channel
        pixels[x + y * width] = value;
    }

    public void drawImage(Image image, int offX, int offY) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                setPixel(x + offX, y + offY, image.getPixels()[x + y * image.getWidth()]);
            }
        }
    }

    public void drawText(Font font, String text, int offX, int offY, int color, int scale) {
        Image imageFont = font.getFontImage();

        text = text.toUpperCase();

        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < imageFont.getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[unicode]; x++) {
                    if (imageFont.getPixels()[(x + font.getOffsets()[unicode]) + y * imageFont.getWidth()] == 0xffffffff) {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }

            offset += font.getWidths()[unicode];
        }
    }

    // todo: drawRectangle, drawCircle (?)
}
