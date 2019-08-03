package evilcorp.graphic.gameobjects.text;

import evilcorp.graphic.Engine;
import evilcorp.logic.NotificationBus;

import java.util.ArrayList;

public class NotificationImmediateArea extends TextArea
{
    private final int color;
    private final int delay;
    private final int[] counters;

    public NotificationImmediateArea(int posX, int posY, int width, int lineHeight, int maxLines, int color, int delay) {
        super(posX, posY, width, lineHeight, maxLines);

        this.color = color;
        this.delay = delay;
        this.counters = new int[maxLines];

        for (int i = 0; i < counters.length; i++) {
            counters[i] = -1;
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < NotificationBus.getImmediateList().size() && i < counters.length; i++) {
            if (counters[i] == -1) counters[i] = 0;

            counters[i]++;

            if (counters[i] > (1/engine.getUpdateRate()) * delay) {
                NotificationBus.getImmediateList().remove(i);
                counters[i] = -1;
            }
        }
    }


    @Override @SuppressWarnings("Duplicates")
    public void render() {
        int usedLines = 0;
        ArrayList<String> notifications = NotificationBus.getImmediateList();

        for (int i = notifications.size() - 1; i >= 0 && usedLines < maxLines; i--) {

            String[] stringsArray = notifications.get(i).split("\n");

            ArrayList<String> strings = new ArrayList<>();
            for (String s : stringsArray) {
                strings.addAll(processString(s));
            }

            for (int j = strings.size() - 1; j >= 0 && usedLines < maxLines; j--) {
                engine.getRenderer().drawText(engine.getStandardFont(), strings.get(j), posX, posY + (lineHeight*maxLines) - (lineHeight*usedLines), color);
                usedLines++;
            }
        }
    }
}
