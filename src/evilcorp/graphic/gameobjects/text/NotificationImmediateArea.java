package evilcorp.graphic.gameobjects.text;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gfx.Font;
import evilcorp.logic.NotificationBus;

import java.util.ArrayList;

public class NotificationImmediateArea extends TextArea
{
    private final int color;
    private final double delay;

    private ArrayList<String> notifications;
    private ArrayList<Double> timers;

    public NotificationImmediateArea(int posX, int posY, int width, int lineHeight, int maxLines, int color, Font font, double delay) {
        super(posX, posY, width, lineHeight, maxLines, font);

        this.color = color;
        this.delay = delay;

        this.notifications = new ArrayList<>();
        this.timers = new ArrayList<>();
    }

    @Override
    public void update() {
        notifications = NotificationBus.getImmediateList();

        if (notifications.size() == 0) {
            timers = new ArrayList<>();
        }

        while (timers.size() < notifications.size()) {
            timers.add(Engine.getTime());
        }

        for (int i = 0; i < timers.size(); i++) {
            if (Engine.getTime() - timers.get(i) >= delay) {
                timers.remove(i);
                NotificationBus.getImmediateList().remove(i);
                //break;
            }
        }
    }


    @Override @SuppressWarnings("Duplicates")
    public void render() {
        int lines = 0;

        for (int i = notifications.size() - 1; i >= 0 && lines < maxLines; i--) {

            String[] stringsArray = notifications.get(i).split("\n");

            ArrayList<String> strings = new ArrayList<>();
            for (String s : stringsArray) {
                strings.addAll(processString(s));
            }

            for (int j = strings.size() - 1; j >= 0 && lines < maxLines; j--) {
                engine.getRenderer().drawText(engine.getStandardFont(), strings.get(j), posX, posY + (lineHeight*maxLines) - (lineHeight*lines), color);
                lines++;
            }
        }
    }
}
