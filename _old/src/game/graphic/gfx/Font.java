package game.graphic.gfx;

import game.graphic.gfx.Image;

public class Font {

    public static final Font DEFAULT = new Font("/data/resources/fonts/font.png");
    public static final Font ROUND12 = new Font("/data/resources/fonts/round12.png");
    public static final Font ROUND3 = new Font("/data/resources/fonts/round3.png");
    public static final Font GRAPH = new Font("/data/resources/fonts/graph.png");
    private Image fontImage;
    private int[] offsets;
    private int[] widths;

    public Font(String path) {

        fontImage = new Image(path);

        offsets = new int[59];
        widths = new int[59];

        int unicode = 0;

        for (int i = 0; i < fontImage.getWidth(); i++) {
            if (fontImage.getPixels()[i] == 0xff0000ff) {
                offsets[unicode] = i;
            }
            if (fontImage.getPixels()[i] == 0xffffff00) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getFontImage() {
        return fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public int[] getWidths() {
        return widths;
    }
}
