package evilcorp.graphic.config;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.*;
import evilcorp.graphic.gameobjects.text.*;
import evilcorp.graphic.gameobjects.visual.*;
import evilcorp.graphic.gfx.*;

import evilcorp.logic.config.Config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigReader
{

    private static final HashMap<String, Object> createdObjects = new HashMap<>();

    public static Object getObject(String id) { return createdObjects.get(id); }


    private static String pass(BufferedReader br) throws IOException {
        String line;
        do {
            line = br.readLine();
        } while (line != null && (line.length() == 0 || line.charAt(0) == '#'));
        return line;
    }


    private static Object createObject(String line) {
        String[] tmp = line.split(" ");

        String type = tmp[0];
        String id = tmp[1];
        String[] params = new String[tmp.length - 2];
        System.arraycopy(tmp, 2, params, 0, tmp.length - 2);

        Object object = null;

        int cur;

        switch (type) {

            case "IMAGE":
                object = new Image(params[0], Double.valueOf(params[1]));
                break;

            case "FONT":
                object = new Font(
                        params[0],
                        Integer.valueOf(params[1]),
                        Integer.valueOf(params[2]),
                        Integer.valueOf(params[3])
                );
                break;

            case "VISUAL":
                object = new Visual(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        (Image)getObject(params[2])
                );
                break;

            case "TEXT":
                if (params.length == 5) {
                    object = new Text(
                            Integer.valueOf(params[0]),
                            Integer.valueOf(params[1]),
                            params[2].replace("_", " "),
                            (int) Long.parseLong(params[3], 16),
                            Engine.getEngine().getFont(Integer.valueOf(params[4]))
                    );
                }

                else if (params.length == 1) {
                    object = new Text((int)Long.parseLong(params[0], 16));
                }

                else {
                    object = new Text();
                }

                break;

            case "TEXTAREA":
                Text[] texts = new Text[Integer.valueOf(params[6])];

                cur = 7;
                for (int i = 0; i < texts.length; i++) {
                    texts[i] = (Text)getObject(params[cur++]);
                }

                object = new TextArea(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        Integer.valueOf(params[2]),
                        Integer.valueOf(params[3]),
                        Integer.valueOf(params[4]),
                        texts,
                        Engine.getEngine().getFont(Integer.valueOf(params[5]))
                );

                break;

            case "ANIMATION":
                String path = params[2];
                int nbImages = Integer.valueOf(params[3]);

                // modulariser pour avoir différentes mouvements d'animation
                Image[] images = new Image[nbImages];
                for (int i = 0; i < nbImages; i++) {
                    double scale = ((nbImages / 10.0) * 2) / ((i + 1) * 0.1);
                    images[i] = new Image(path, scale);
                }

                object = new Animation(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        images,
                        Double.valueOf(params[4])
                );
                break;

            case "BUTTON":
                if (params.length == 5) { // handle different kinds of buttons
                    object = new Button(
                            Integer.valueOf(params[0]),
                            Integer.valueOf(params[1]),
                            params[2].replace("_", " "),
                            (int) Long.parseLong(params[3], 16),
                            Engine.getEngine().getFont(Integer.valueOf(params[4]))
                    );
                }
                break;

            case "NOTW":
                object = new NotificationWaitingArea(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        Integer.valueOf(params[2]),
                        Integer.valueOf(params[3]),
                        Integer.valueOf(params[4]),
                        (int)Long.parseLong(params[5], 16),
                        Engine.getEngine().getFont(Integer.valueOf(params[6]))
                );

                break;

            case "NOTI":
                object = new NotificationImmediateArea(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        Integer.valueOf(params[2]),
                        Integer.valueOf(params[3]),
                        Integer.valueOf(params[4]),
                        (int)Long.parseLong(params[5], 16),
                        Engine.getEngine().getFont(Integer.valueOf(params[6])),
                        Double.valueOf(params[7])
                );

                break;

            case "MAP":
                int nbRegions = Integer.valueOf(params[3]);

                String[] names = new String[nbRegions];
                String[] paths = new String[nbRegions+1];
                int[][] config = new int[nbRegions][];

                paths[0] = params[4];

                cur = 5;
                for (int i = 0; i < nbRegions; i++) {
                    names[i] = params[cur++].replace("_", " ");
                    paths[i+1] = params[cur++];
                    config[i] = new int[]{
                            Integer.valueOf(params[cur++]),
                            Integer.valueOf(params[cur++]),
                            Integer.valueOf(params[cur++]),
                            Integer.valueOf(params[cur++])
                    };

                }


                object = new Map(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        Double.valueOf(params[2]),
                        names,
                        paths,
                        config
                );

                break;

            case "MENU":

                String[] options = new String[Integer.valueOf(params[6])];

                cur = 7;
                for (int i = 0; i < options.length; i++) {
                    options[i] = params[cur++].replace("_", " ");
                }

                object = new Menu(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        Integer.valueOf(params[2]),
                        params[3].replace("_", " "),
                        options,
                        null,
                        (int)Long.parseLong(params[4], 16),
                        Engine.getEngine().getFont(Integer.valueOf(params[5]))
                );

                break;

            case "GAUGE":
                // new GaugeGraph(posX, posY + height*field*0, width, height, Config.minGauge, Config.maxGauge, prodColor, borderSize, "PRODUCTION", 0xffffffff, engine.getFont(0));
                object = new GaugeGraph(
                        Integer.valueOf(params[0]),
                        Integer.valueOf(params[1]),
                        Integer.valueOf(params[2]),
                        Integer.valueOf(params[3]),
                        Config.minGauge,
                        Config.maxGauge,
                        (int)Long.parseLong(params[4], 16),
                        Integer.valueOf(params[5]),
                        params[6].replace("_", " "),
                        (int)Long.parseLong(params[7], 16),
                        Engine.getEngine().getFont(Integer.valueOf(params[8]))
                );

                break;
        }

        createdObjects.put(id, object);

        if (id.charAt(0) == '!') {
            return new Object();
        }

        return object;
    }


    public static Object[] readEngine(String configPath) {
        String[] buffer;

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            buffer = pass(reader).split(" ");
            reader.close();

            return new Object[]{
                    buffer[0],
                    Integer.valueOf(buffer[1]),
                    Integer.valueOf(buffer[2]),
                    Double.valueOf(buffer[3]),
                    Boolean.valueOf(buffer[4]),
                    Double.valueOf(buffer[5]),
                    Boolean.valueOf(buffer[6])
            };

        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }


    public static Font[] readFonts(String configPath) {
        String line;

        ArrayList<Font> fonts = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            while ((line = pass(reader)) != null) {
                fonts.add((Font)createObject(line));
            }

            reader.close();

            return fonts.toArray(new Font[]{});

        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }


    public static GameObject[] readScene(String configPath) {
        String line;

        ArrayList<GameObject> objects = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            while ((line = pass(reader)) != null) {

                Object current = createObject(line);
                if (current instanceof GameObject) {
                    objects.add((GameObject)current);
                }

            }

            reader.close();

            return objects.toArray(new GameObject[]{});

        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }
}


