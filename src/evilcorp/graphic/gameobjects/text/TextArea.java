package evilcorp.graphic.gameobjects.text;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.GameObject;

import java.util.ArrayList;

public class TextArea extends GameObject
{
    private final Text[] texts;

    protected final int posX;
    protected final int posY;
    protected final int lineHeight;

    protected final int maxChars;
    protected final int maxLines;

    // allow inArea Offset

    public TextArea(Engine engine, int posX, int posY, int width, int lineHeight, int maxLines, Text[] texts) {
        super(engine);

        this.posX = posX;
        this.posY = posY;
        this.lineHeight = lineHeight;

        this.maxLines = maxLines;
        this.maxChars = width / engine.getStandardFont().getTextUnitWidth();

        this.texts = texts;
    }

    protected TextArea(Engine engine, int posX, int posY, int width, int lineHeight, int maxLines) {
        this(engine, posX, posY, width, lineHeight, maxLines, null);
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

            String[] stringsArray = texts[i].getText().split("\n");

            ArrayList<String> strings = new ArrayList<>();
            for (String s : stringsArray) {
                strings.addAll(processString(s));
            }

            for (int j = 0; j < strings.size() && usedLines < maxLines; j++) {
                engine.getRenderer().drawText(engine.getStandardFont(), strings.get(j), posX, posY + (lineHeight * usedLines), texts[i].getColor());
                usedLines++;
            }
        }
    }


    protected ArrayList<String> processString(String s) {
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
