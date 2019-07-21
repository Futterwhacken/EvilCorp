package game.graphic;

import game.graphic.gfx.Image;
import game.graphic.gfx.Font;
import java.awt.image.DataBufferInt;

public class Display {

    private int pWidth, pHeight;
    private int[] pixels;
    private Image roundImage = null;

    private Font font = Font.DEFAULT;

    public Display(GameLoop gameLoop) {
        pWidth = gameLoop.getWidth();
        pHeight = gameLoop.getHeight();

        pixels = ((DataBufferInt) gameLoop.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
             pixels[i] = 0xff000000;
        }
    }

    public void setPixels(int x, int y, int value) {
        if ((x < 0 || x >= this.pWidth || y < 0 || y >= this.pHeight) || value == 0xffff00ff)
            return;

        pixels[x + y * pWidth] = value;
    }

    public void drawText(String text, int locX, int locY, int color) {
        font = Font.DEFAULT;
        text = text.toUpperCase();
        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < font.getFontImage().getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[unicode]; x++) {
                    if (font.getFontImage().getPixels()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()] == 0xffffffff) { // 0xffffffff == -1
                        setPixels(x + offset + locX, y + locY, color);
                    }
                }
            }

            offset += font.getWidths()[unicode];
        }
    }

    public void drawGlobalRates(Gauges gauge) {
        font = Font.GRAPH;

        int heightProductivity = (int) (gauge.getPercentageProductivity() * 3.0 / 4.0);
        int heightSocial = (int) (gauge.getPercentageSocial() * 3.0 / 4.0);
        int heightVisibility = (int) (gauge.getPercentageVisibility() * 3.0 / 4.0);


        // prod
        for (int i = 0; i < heightProductivity; i++) {
            int unicode = 0;

            for (int y = 0; y < font.getFontImage().getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[0]; x++) {
                    if (font.getFontImage().getPixels()[(x + font.getOffsets()[0]) + y * font.getFontImage().getWidth()] == 0xffffffff) { // 0xffffffff == -1
                        setPixels(x + 430, y - i + 270, 0xffff0000);
                    }
                }
            }
        }

        // soc
        for (int i = 0; i < heightSocial; i++) {
            for (int y = 0; y < font.getFontImage().getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[0]; x++) {
                    if (font.getFontImage().getPixels()[(x + font.getOffsets()[0]) + y * font.getFontImage().getWidth()] == 0xffffffff) { // 0xffffffff == -1
                        setPixels(x + 350, y - i + 270, 0xff00ff00);
                    }
                }
            }
        }

        // visi
        for (int i = 0; i < heightVisibility; i++) {
            for (int y = 0; y < font.getFontImage().getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[0]; x++) {
                    if (font.getFontImage().getPixels()[(x + font.getOffsets()[0]) + y * font.getFontImage().getWidth()] == 0xffffffff) { // 0xffffffff == -1
                        setPixels(x + 390, y - i + 270, 0xff00ffff);
                    }
                }
            }
        }
    }

	public String[] splitNotification(String notif) {
		return notif.split("\n");
	}

	public void drawNotification(String[] notif, int locX, int locY) {
		for (int i = 0; i < notif.length; i++) {
			drawText(notif[i], locX, locY + i * 10, 0xffffff00);
		}
	}

    public void drawGaugeMini(Gauges gauge, int locX, int locY) {
        int offset = 0;
        int sum = gauge.getPercentageProductivity() + gauge.getPercentageSocial() + gauge.getPercentageVisibility();
		double rateProductivity = 0.0;
		double rateVisibility = 0.0;
		if (sum != 0) {
			rateProductivity = (gauge.getPercentageProductivity() * 100.0) / sum;
	        rateVisibility = (gauge.getPercentageVisibility()* 100.0) / sum;
		}

        int unicode1 = 0;
        int unicode2 = 0;

        if (rateProductivity > 06) unicode1 = 1;
        if (rateProductivity > 19) unicode1 = 2;
        if (rateProductivity > 31) unicode1 = 3;
        if (rateProductivity > 43) unicode1 = 4;
        if (rateProductivity > 56) unicode1 = 5;
        if (rateProductivity > 69) unicode1 = 6;
        if (rateProductivity > 81) unicode1 = 7;
        if (rateProductivity > 93) unicode1 = 8;
        if (rateVisibility > 06) unicode2 = 1;
        if (rateVisibility > 19) unicode2 = 2;
        if (rateVisibility > 31) unicode2 = 3;
        if (rateVisibility > 43) unicode2 = 4;
        if (rateVisibility > 56) unicode2 = 5;
        if (rateVisibility > 69) unicode2 = 6;
        if (rateVisibility > 81) unicode2 = 7;
        if (rateVisibility > 93) unicode2 = 8;

        font = Font.ROUND12;
        // social
		if (gauge.getPercentageSocial() > 40) {
			for (int y = 0; y < font.getFontImage().getHeight(); y++) {
	            for (int x = 0; x < font.getWidths()[8]; x++) {
	                if (font.getFontImage().getPixels()[(x + font.getOffsets()[8]) + y * font.getFontImage().getWidth()] == 0xffffffff) {
	                    setPixels(x + offset + locX, y + locY, 0xff00ff00);
	                }
	            }
	        }
		}

        // prod
        for (int y = 0; y < font.getFontImage().getHeight(); y++) {
            for (int x = 0; x < font.getWidths()[unicode1]; x++) {
                if (font.getFontImage().getPixels()[(x + font.getOffsets()[unicode1]) + y * font.getFontImage().getWidth()] == 0xffffffff) {
                    setPixels(x + offset + locX, y + locY, 0xffff0000);
                }
            }
        }

        // vis
        font = Font.ROUND3;
        for (int y = 0; y < font.getFontImage().getHeight(); y++) {
            for (int x = 0; x < font.getWidths()[unicode2]; x++) {
                if (font.getFontImage().getPixels()[(x + font.getOffsets()[unicode2]) + y * font.getFontImage().getWidth()] == 0xffffffff) {
                    setPixels(x + offset + locX, y + locY, 0xff00ffff);
                }
            }
        }
    }

    public void drawImage(Image image, int locX, int locY) {
        font = Font.DEFAULT;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                setPixels(x + locX, y + locY, image.getPixels()[x + y * image.getWidth()]);
            }
        }
    }
}
