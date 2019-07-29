package evilcorp.graphic.gfx;

public class Font
{
    public int textLength(String text) {
        text = text.toUpperCase();
        int specials = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == 'I' || c == 'T' || c == 'Y') {
                specials++;
            }
        }

        return ((charWidth + interspace) * text.length()) - (specials * interspace);
    }

    private final Image fontImage;
    private final int[] offsets;
    private final int[] widths;

    private final int charWidth;
    private final int charHeight;
    private final int interspace;

    public Font(String path, int charWidth, int charHeight, int interspace) {
        this.fontImage = new Image(path);

        this.offsets = new int[59];
        this.widths = new int[59];

        this.charWidth = charWidth;
        this.charHeight = charHeight;
        this.interspace = interspace; // use interspace in renderer

        int unicode = 0;

        for (int i = 0; i < fontImage.getWidth(); i++) {
            if (fontImage.getPixels()[i] == 0xff0000ff) { // blue
                offsets[unicode] = i;
            }

            if (fontImage.getPixels()[i] == 0xffffff00) { // yellow
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getFontImage() { return fontImage; }
    public int[] getOffsets() { return offsets; }
    public int[] getWidths() { return widths; }

    public int getCharWidth() { return charWidth; }
    public int getCharHeight() { return charHeight; }
    public int getInterspace() { return interspace; }

    public int getTextUnitWidth() { return charWidth + interspace; }
}
