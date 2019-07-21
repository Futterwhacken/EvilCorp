package evilcorp.graphic.gameobjects;

import evilcorp.graphic.Engine;

import java.util.ArrayList;

public class TextArea extends GameObject
{
    /* static version */

    private final Text[] texts;

    private final int posX;
    private final int posY;
    private final int lineHeight;

    private final int maxChars;
    private final int maxLines;

    private final int charWidth = 7+1;

    // allow inArea Offset

    public TextArea(Engine engine, int posX, int posY, int width, int lineHeight, int maxLines, Text[] texts) {
        super(engine);

        this.posX = posX;
        this.posY = posY;
        this.lineHeight = lineHeight;

        this.maxLines = maxLines;
        this.maxChars = width / charWidth;

        this.texts = texts;
    }

    @Override
    public void update() {
        for (Text t : texts) {
            t.update();
        }
    }

    @Override
    public void render() {

        int usedLines = 0;

        for (int i = 0; i < texts.length && usedLines < maxLines; i++) {

            ArrayList<String> strings = processString(texts[i].getText());

            for (int j = 0; j < strings.size() && usedLines < maxLines; j++) {
                engine.getRenderer().drawText(engine.getStandardFont(), strings.get(j), posX, posY + (lineHeight * usedLines), texts[i].getColor());
                usedLines++;
            }
        }
    }

    private ArrayList<String> processString(String s) {
        ArrayList<String> strings = new ArrayList<>();

        if (s.length() > maxChars) {

            int split = 0;

            for (int j = maxChars - 1; j > 0; j--) {
                if (s.charAt(j) == ' ') {
                    split = j;
                    break;
                }
            }

            if (split == 0) {
                split = maxChars;
            }

            strings.add(s.substring(0, split));
            strings.addAll(processString(s.substring(split)));

            return strings;
        }

        strings.add(s);
        return strings;
    }
}
