package evilcorp.graphic.config;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.Map;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.graphic.gameobjects.text.Text;
import evilcorp.graphic.gameobjects.text.TextArea;
import evilcorp.graphic.gfx.Font;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("Duplicates")
public class ConfigReader
{

    private static String pass(BufferedReader br) throws IOException {
        String line;
        do {
                line = br.readLine();
        } while (line.length() == 0 || line.charAt(0) == '#');
        return line;
    }

    public static Object[] readEngine(String configPath) {
        String line;
        String[] buffer;

        String title;
        int width, height;
        double scale, updateCap;
        boolean debug;

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            line = pass(reader);

            // debug
            debug = Boolean.valueOf(line);

            // dimensions
            line = pass(reader);
            buffer = line.split(" ");

            width = Integer.valueOf(buffer[0]);
            height = Integer.valueOf(buffer[1]);
            scale = Double.valueOf(buffer[2]);

            // update cap
            line = pass(reader);

            updateCap = Double.valueOf(line);

            // title
            line = pass(reader);

            title = line;

            reader.close();

            return new Object[]{title, width, height, scale, updateCap, debug};

        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }

    public static Font[] readFonts(String configPath, String fontsPath) {
        String line;
        String[] buffer;

        Font[] fonts;

        String path;
        int width, height, interspace;

        int fontCount;

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            // nb fonts
            line = pass(reader);

            fontCount = Integer.valueOf(line);

            fonts = new Font[fontCount];

            // fonts
            for (int i = 0; i < fontCount; i++) {
                // path
                line = pass(reader);

                path = fontsPath+line;

                // params
                line = reader.readLine();
                buffer = line.split(" ");

                width = Integer.valueOf(buffer[0]);
                height = Integer.valueOf(buffer[1]);
                interspace = Integer.valueOf(buffer[2]);

                fonts[i] = new Font(path, width, height, interspace);
            }

            reader.close();

            return fonts;

        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }

    public static Map readMap(String configPath, String mapsPath) {

        Engine engine = Engine.getEngine();

        String line;
        String[] buffer;

        int posX, posY, width, height;
        double scale;

        String[] regionNames;
        String[] imagesPaths;
        int[][] parameters;

        int regionCount;

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            // dimensions
            line = pass(reader);
            buffer = line.split(" ");

            width = Integer.valueOf(buffer[0]);
            height = Integer.valueOf(buffer[1]);
            scale = Double.valueOf(buffer[2]);

            // position
            line = pass(reader);
            buffer = line.split(" ");

            if (Integer.valueOf(buffer[0]) == -1) {
                posX = (int)(0.5*(engine.getWidth() - (width * scale)));
                posY = 0;
            }
            else {
                posX = Integer.valueOf(buffer[0]);
                posY = Integer.valueOf(buffer[1]);
            }

            // nb regions
            line = pass(reader);

            regionCount = Integer.valueOf(line);

            regionNames = new String[regionCount];
            imagesPaths = new String[regionCount+1];
            parameters = new int[regionCount][];

            // map image
            line = pass(reader);

            imagesPaths[0] = mapsPath+line;

            // regions
            for (int i = 0; i < regionCount; i++) {
                // name
                line = pass(reader);

                regionNames[i] = line;

                // path
                line = reader.readLine();

                imagesPaths[i+1] = mapsPath+line;

                // params
                line = reader.readLine();
                buffer = line.split(" ");

                parameters[i] = new int[]{
                        Integer.valueOf(buffer[0]),
                        Integer.valueOf(buffer[1]),
                        Integer.valueOf(buffer[2]),
                        Integer.valueOf(buffer[3])
                };
            }

            reader.close();

            return new Map(engine, posX, posY, width, height, scale, regionNames, imagesPaths, parameters);
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }

    public static GameObject[] readMenu(String configPath) {

        Engine engine = Engine.getEngine();

        /*

        TextArea hoverTextArea = new TextArea(engine, (int)menuConfig[8], (int)menuConfig[9],(int)menuConfig[10], (int)menuConfig[11], (int)menuConfig[12], new Text[]{new Text(engine, (int)menuConfig[13])});
        Menu mainMenu = new Menu(engine, (int)menuConfig[0], (int)menuConfig[1], (int)menuConfig[2], menuConfig[4].toString(),
                new String[]{
                        menuConfig[5].toString(),
                        menuConfig[6].toString(),
                        menuConfig[7].toString()
                },
                new Action[]{
                        () -> engine.setCurrentMenu(buildAddExploitationMenu(menuConfig[5].toString())),
                        () -> engine.setCurrentMenu(buildRemoveExploitationMenu(menuConfig[6].toString())),
                        () -> {
                            engine.setCurrentMenu(buildBuyEventMenu(menuConfig[7].toString()));
                            Menu buyEvntMenu = engine.getCurrentMenu();

                            if (buyEvntMenu.getButtons()[0].getHoverAction() == null) {
                                for (int i = 0; i < buyEvntMenu.getButtons().length-2; i++) {
                                    int a = i;
                                    buyEvntMenu.getButtons()[i].setHoverAction(() -> {
                                        if (buyEvntMenu.noneHovered()) {
                                            hoverTextArea.getTexts()[0].setText("");
                                        }
                                        else if (buyEvntMenu.getButtons()[a].isHovered()) {
                                            hoverTextArea.getTexts()[0].setText(engine.getSelectedRegion().getBuyableEvents().get(a).toString());
                                        }
                                    }, () -> hoverTextArea.getTexts()[0].setText(""));
                                }
                            }
                        }
                }, (int)menuConfig[3]);

         */
        String line;
        String[] buffer;

        int menuPosX, menuPosY, menuFieldHeight, menuColor;
        int hoverPosX, hoverPosY, hoverWidth, hoverLineHeight, hoverMaxLines, hoverColor;
        String[] labels;

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            // menu parameters
            line = pass(reader);
            buffer = line.split(" ");

            menuPosX = Integer.valueOf(buffer[0]);
            menuPosY = Integer.valueOf(buffer[1]);
            menuFieldHeight = Integer.valueOf(buffer[2]);
            menuColor = (int)Long.parseLong(buffer[3], 16);

            // menu labels
            line = reader.readLine();
            buffer = line.split(" ");

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = buffer[i].replace("_", " ");
            }

            labels = buffer;

            // hover parameters
            line = pass(reader);
            buffer = line.split(" ");

            hoverPosX = Integer.valueOf(buffer[0]);
            hoverPosY = Integer.valueOf(buffer[1]);;
            hoverWidth = Integer.valueOf(buffer[2]);
            hoverLineHeight = Integer.valueOf(buffer[3]);
            hoverMaxLines = Integer.valueOf(buffer[4]);
            hoverColor = (int)Long.parseLong(buffer[5], 16);


            // building objects

            TextArea hoverTextArea = new TextArea(engine, hoverPosX, hoverPosY, hoverWidth, hoverLineHeight, hoverMaxLines, new Text[]{new Text(engine, hoverColor)});
            Menu tmp = new Menu(engine, menuPosX, menuPosY, menuFieldHeight, menuColor);
            Menu mainMenu = new Menu(engine, menuPosX, menuPosY, menuFieldHeight, labels[0],
                    new String[]{
                            labels[1],
                            labels[2],
                            labels[3]
                    },
                    new Action[]{
                            () -> engine.setCurrentMenu(engine.buildAddExploitationMenu(labels[1], tmp)),
                            () -> engine.setCurrentMenu(engine.buildRemoveExploitationMenu(labels[2], tmp)),
                            () -> {
                                engine.setCurrentMenu(engine.buildBuyEventMenu(labels[3], tmp));
                                Menu buyEvntMenu = engine.getCurrentMenu();

                                if (buyEvntMenu.getButtons()[0].getHoverAction() == null) {
                                    for (int i = 0; i < buyEvntMenu.getButtons().length-2; i++) {
                                        int a = i;
                                        buyEvntMenu.getButtons()[i].setHoverAction(() -> {
                                            if (buyEvntMenu.noneHovered()) {
                                                hoverTextArea.getTexts()[0].setText("");
                                            }
                                            else if (buyEvntMenu.getButtons()[a].isHovered()) {
                                                hoverTextArea.getTexts()[0].setText(engine.getSelectedRegion().getBuyableEvents().get(a).toString());
                                            }
                                        }, () -> hoverTextArea.getTexts()[0].setText(""));
                                    }
                                }
                            }
                    }, menuColor);

            return new GameObject[]{mainMenu, hoverTextArea};
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