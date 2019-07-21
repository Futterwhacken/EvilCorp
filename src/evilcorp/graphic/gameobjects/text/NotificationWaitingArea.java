package evilcorp.graphic.gameobjects.text;

import evilcorp.graphic.Engine;
import evilcorp.logic.NotificationBus;

import java.util.ArrayList;

public class NotificationWaitingArea extends TextArea
{
    private final int color;

    // réduire le nb de lignes
    public NotificationWaitingArea(Engine engine, int posX, int posY, int width, int lineHeight, int maxLines, int color) {
        super(engine, posX, posY, width, lineHeight, maxLines);
        this.color = color;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        int usedLines = 0;
        ArrayList<String> notifications = NotificationBus.getWaitingList();

        for (int i = notifications.size() - 1; i >= 0 && usedLines < maxLines; i--) {

            String[] stringsArray = notifications.get(i).split("\n");

            ArrayList<String> strings = new ArrayList<>();
            for (String s : stringsArray) {
                strings.addAll(processString(s));
            }
            strings.add(" ");

            for (int j = strings.size() - 1; j >= 0 && usedLines < maxLines; j--) {
                engine.getRenderer().drawText(engine.getStandardFont(), strings.get(j), posX, posY + (lineHeight*maxLines) - (lineHeight*usedLines), color);
                usedLines++;
            }
        }
    }
}
