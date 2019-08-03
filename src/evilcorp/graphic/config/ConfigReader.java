package evilcorp.graphic.config;

import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Action;
import evilcorp.graphic.gameobjects.GameObject;
import evilcorp.graphic.gameobjects.interactive.Button;
import evilcorp.graphic.gameobjects.interactive.Map;
import evilcorp.graphic.gameobjects.interactive.Menu;
import evilcorp.graphic.gameobjects.text.NotificationImmediateArea;
import evilcorp.graphic.gameobjects.text.NotificationWaitingArea;
import evilcorp.graphic.gameobjects.text.Text;
import evilcorp.graphic.gameobjects.text.TextArea;
import evilcorp.graphic.gameobjects.visual.Animation;
import evilcorp.graphic.gameobjects.visual.GaugeGraph;
import evilcorp.graphic.gameobjects.visual.Visual;
import evilcorp.graphic.gfx.Font;
import evilcorp.graphic.gfx.Image;

import evilcorp.logic.GameMaster;
import evilcorp.logic.NotificationBus;
import evilcorp.logic.config.Config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("Duplicates")
public class ConfigReader
{
    private static final String mapsPath = "data/resources/maps/";
    private static final String imagesPath = "data/resources/images/";

    private static HashMap<String, Object> createdObjects = new HashMap<>();

    public static Object getObject(String id) { return createdObjects.get(id); }


    private static String pass(BufferedReader br) throws IOException {
        String line;
        do {
            line = br.readLine();
        } while (line != null && (line.length() == 0 || line.charAt(0) == '#'));
        return line;
    }


    private static Object createObject(String config) {
        String[] tmp = config.split(" ");

        String type = tmp[0];
        String id = tmp[1];
        String[] params = new String[tmp.length - 2];

        Object object = null;

        System.arraycopy(tmp, 2, params, 0, tmp.length - 2);


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
                        (Image) getObject(params[2])
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


        }

        createdObjects.put(id, object);

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
                    Double.valueOf(buffer[4]),
                    Boolean.valueOf(buffer[5])
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



    public static Map readMap(String configPath) {

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

            posX = Integer.valueOf(buffer[0]);
            posY = Integer.valueOf(buffer[1]);

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

            return new Map(posX, posY, width, height, scale, regionNames, imagesPaths, parameters);
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

            TextArea hoverTextArea = new TextArea(hoverPosX, hoverPosY, hoverWidth, hoverLineHeight, hoverMaxLines, new Text[]{new Text(hoverColor)});
            Menu tmp = new Menu(menuPosX, menuPosY, menuFieldHeight, menuColor);
            Menu mainMenu = new Menu(menuPosX, menuPosY, menuFieldHeight, labels[0],
                    new String[]{
                            labels[1],
                            labels[2],
                            labels[3]
                    },
                    new Action[]{
                            () -> engine.getCurrentScene().setCurrentMenu(Menu.buildAddExploitationMenu(labels[1], tmp)),
                            () -> engine.getCurrentScene().setCurrentMenu(Menu.buildRemoveExploitationMenu(labels[2], tmp)),
                            () -> {
                                engine.getCurrentScene().setCurrentMenu(Menu.buildBuyEventMenu(labels[3], tmp));
                                Menu buyEvntMenu = engine.getCurrentScene().getCurrentMenu();

                                if (buyEvntMenu.getButtons()[0].getHoverAction() == null) {
                                    for (int i = 0; i < buyEvntMenu.getButtons().length-2; i++) {
                                        int a = i;
                                        buyEvntMenu.getButtons()[i].setHoverAction(() -> {
                                            if (buyEvntMenu.noneHovered()) {
                                                hoverTextArea.getTexts()[0].setText("");
                                            }
                                            else if (buyEvntMenu.getButtons()[a].isHovered()) {
                                                hoverTextArea.getTexts()[0].setText(engine.getCurrentScene().getSelectedRegion().getBuyableEvents().get(a).toString());
                                            }
                                        }, () -> hoverTextArea.getTexts()[0].setText(""));
                                    }
                                }
                            }
                    }, menuColor);

            reader.close();

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

    public static TextArea readInfoArea(String configPath) {

        Engine engine = Engine.getEngine();

        String line;
        String[] buffer;


        int posX, posY, width, lineHeight, maxLines, color;


        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            // area parameters
            line = pass(reader);
            buffer = line.split(" ");

            posX = Integer.valueOf(buffer[0]);
            posY = Integer.valueOf(buffer[1]);
            width = Integer.valueOf(buffer[2]);
            lineHeight = Integer.valueOf(buffer[3]);
            maxLines = Integer.valueOf(buffer[4]);
            color = (int)Long.parseLong(buffer[5], 16);

            // building text area

            // modulariser tout ça ?
            Text selectedRegionText = new Text("SELECT A REGION", color);
            selectedRegionText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionText.setText(engine.getCurrentScene().getSelectedRegion().getName());
                else
                    selectedRegionText.setText(selectedRegionText.getDefaultText());
            });

            Text selectedRegionActivityText = new Text(color);
            selectedRegionActivityText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionActivityText.setText(engine.getCurrentScene().getSelectedRegion().getParams().getActivityToleranceString());
            });

            Text selectedRegionEnvironmentText = new Text(color);
            selectedRegionEnvironmentText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionEnvironmentText.setText(engine.getCurrentScene().getSelectedRegion().getParams().getEnvironmentToleranceString());
            });

            Text selectedRegionOppositionText = new Text(color);
            selectedRegionOppositionText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionOppositionText.setText(engine.getCurrentScene().getSelectedRegion().getParams().getOppositionToleranceString());
            });

            Text selectedRegionProductionText = new Text(color);
            selectedRegionProductionText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionProductionText.setText("PRODUCTION: " + engine.getCurrentScene().getSelectedRegion().getProductivity());
            });

            Text selectedRegionPollutionText = new Text(color);
            selectedRegionPollutionText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionPollutionText.setText("POLLUTION: " + engine.getCurrentScene().getSelectedRegion().getVisibility());
            });

            Text selectedRegionSocialText = new Text(color);
            selectedRegionSocialText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionSocialText.setText("SOCIAL: " + engine.getCurrentScene().getSelectedRegion().getSocial());
            });

            Text selectedRegionExploitationText = new Text(color);
            selectedRegionExploitationText.setAction(() -> {
                if (engine.getCurrentScene().getSelectedRegion() != null)
                    selectedRegionExploitationText.setText(""+engine.getCurrentScene().getSelectedRegion().getExploitations().size() + "/" + engine.getCurrentScene().getSelectedRegion().getMaxExpl() + " EXPLOITATIONS");
            });

            TextArea regionInfo = new TextArea(posX, posY, width, lineHeight, maxLines,
                    new Text[]{
                            selectedRegionText,
                            new Text(),
                            selectedRegionActivityText,
                            selectedRegionEnvironmentText,
                            selectedRegionOppositionText,
                            new Text(),
                            selectedRegionProductionText,
                            selectedRegionPollutionText,
                            selectedRegionSocialText,
                            new Text(),
                            selectedRegionExploitationText,
                    });

            reader.close();

            return regionInfo;
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }

    public static GameObject[] readGauges(String configPath) {

        Engine engine = Engine.getEngine();
        GameMaster gm = GameMaster.getGameMaster();

        String line;
        String[] buffer;

        boolean horizontal;
        int width, height, posX, posY, borderSize, textColor, prodColor, pollColor, socialColor;
        String prod, poll, social;

        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            // area parameters
            line = pass(reader);
            buffer = line.split(" ");

            horizontal = Boolean.valueOf(buffer[0]);
            width = Integer.valueOf(buffer[1]);
            height = Integer.valueOf(buffer[2]);

            if (Integer.valueOf(buffer[3]) == -1) {
                posX = (int)(0.5*(engine.getWidth() - width));
            }
            else {
                posX = Integer.valueOf(buffer[3]);
            }

            posY = Integer.valueOf(buffer[4]);
            borderSize = Integer.valueOf(buffer[5]);
            textColor = (int)Long.parseLong(buffer[6], 16);

            // strings

            line = pass(reader);
            buffer = line.split(" ");

            prod = buffer[0];
            poll = buffer[1];
            social = buffer[2];

            // colors

            line = pass(reader);
            buffer = line.split(" ");

            prodColor = (int)Long.parseLong(buffer[0], 16);
            pollColor = (int)Long.parseLong(buffer[1], 16);
            socialColor = (int)Long.parseLong(buffer[2], 16);


            // building text area

            // modulariser horizontal/vertical

            int charWidth = engine.getStandardFont().getTextUnitWidth();
            int offset = (height-engine.getStandardFont().getCharHeight())/2;
            int field = borderSize;

            Text productionGaugeText = new Text(posX - ((prod.length()+1) * charWidth) - borderSize, posY, prod, textColor);
            Text visibilityGaugeText = new Text(posX - ((poll.length()+1) * charWidth) - borderSize, posY + height*2, poll, textColor);
            Text socialGaugeText = new Text(posX - ((social.length()+1) * charWidth) - borderSize, posY + height*4, social, textColor);

            Text productionLevelText = new Text(posX + width + charWidth + borderSize, posY, textColor);
            productionLevelText.setAction(() -> productionLevelText.setText(""+gm.getWorld().getProductivity()));

            Text visibilityLevelText = new Text(posX + width + charWidth + borderSize, posY + height*2, textColor);
            visibilityLevelText.setAction(() -> visibilityLevelText.setText(""+gm.getWorld().getVisibility()));

            Text socialLevelText = new Text(posX + width + charWidth + borderSize, posY + height*4, textColor);
            socialLevelText.setAction(() -> socialLevelText.setText(""+gm.getWorld().getSocial()));


            GaugeGraph productionGauge = new GaugeGraph(posX, posY + height*field*0 - offset, width, height, horizontal, Config.minGauge, Config.maxGauge, borderSize, prodColor, textColor);
            productionGauge.setAction(() -> productionGauge.setLevel(gm.getWorld().getProductivity()));

            GaugeGraph visibilityGauge = new GaugeGraph(posX, posY + height*field*1 - offset, width, height, horizontal, Config.minGauge, Config.maxGauge, borderSize, pollColor, textColor);
            visibilityGauge.setAction(() -> visibilityGauge.setLevel(gm.getWorld().getVisibility()));

            GaugeGraph socialGauge = new GaugeGraph(posX, posY + height*field*2 - offset, width, height, horizontal, Config.minGauge, Config.maxGauge, borderSize, socialColor, textColor);
            socialGauge.setAction(() -> socialGauge.setLevel(gm.getWorld().getSocial()));

            reader.close();

            return new GameObject[]{
                    productionGaugeText, visibilityGaugeText, socialGaugeText,
                    productionLevelText, visibilityLevelText, socialLevelText,
                    productionGauge, visibilityGauge, socialGauge
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

    public static GameObject[] readNextArea(String configPath) {

        Engine engine = Engine.getEngine();
        GameMaster gm = GameMaster.getGameMaster();

        String line;
        String[] buffer;


        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            // turns text
            line = pass(reader);
            buffer = line.split(" ");

            Text turnsText = new Text(Integer.valueOf(buffer[0]), Integer.valueOf(buffer[1]), (int)Long.parseLong(buffer[2], 16));
            turnsText.setAction(() -> turnsText.setText("TURN: " + gm.getTurn()));

            // funds text
            line = pass(reader);
            buffer = line.split(" ");

            Text fundsText = new Text(Integer.valueOf(buffer[0]), Integer.valueOf(buffer[1]), (int)Long.parseLong(buffer[2], 16));
            fundsText.setAction(() -> fundsText.setText("FUNDS: " + gm.getPoints() + " $"));

            // next button
            line = pass(reader);
            buffer = line.split(" ");

            // debug
            /*Button nextTurnButton = new Button(engine, Integer.valueOf(buffer[0]), Integer.valueOf(buffer[1]),
                    () -> {
                        NotificationBus.clearWaitingList();
                        NotificationBus.clearImmediateList();
                        gm.nextTurn();
                    },
                    new Image(imagesPath+buffer[2])
            );*/

            Button nextTurnButton = new Button(Integer.valueOf(buffer[0])+13, Integer.valueOf(buffer[1]),
                    () -> {
                        NotificationBus.clearWaitingList();
                        NotificationBus.clearImmediateList();
                        gm.nextTurn();
                    },
                    "NEXT TURN", 0xff00ffff, engine.getFont(1)
            );

            reader.close();

            return new GameObject[]{turnsText, fundsText, nextTurnButton};
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" + configPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ configPath + "\"");
        }

        return null;
    }

    public static GameObject[] readNotifArea(String configPath) {

        Engine engine = Engine.getEngine();

        String line;
        String[] buffer;


        try {
            FileReader fileReader = new FileReader(configPath);
            BufferedReader reader = new BufferedReader(fileReader);

            // waiting notif
            line = pass(reader);
            buffer = line.split(" ");

            NotificationWaitingArea notificationWaitingArea = new NotificationWaitingArea(
                    Integer.valueOf(buffer[0]),
                    Integer.valueOf(buffer[1]),
                    Integer.valueOf(buffer[2]),
                    Integer.valueOf(buffer[3]),
                    Integer.valueOf(buffer[4]),
                    (int)Long.parseLong(buffer[5], 16)
            );

            // immediate notif
            line = pass(reader);
            buffer = line.split(" ");

            NotificationImmediateArea notificationImmediateArea = new NotificationImmediateArea(
                    Integer.valueOf(buffer[0]),
                    Integer.valueOf(buffer[1]),
                    Integer.valueOf(buffer[2]),
                    Integer.valueOf(buffer[3]),
                    Integer.valueOf(buffer[4]),
                    (int)Long.parseLong(buffer[5], 16),
                    Integer.valueOf(buffer[6])
            );

            // logo
            line = pass(reader);
            buffer = line.split(" ");

            Image image = new Image(imagesPath+buffer[2]);
            Visual logo = new Visual(Integer.valueOf(buffer[0]), Integer.valueOf(buffer[1]), image);

            reader.close();

            return new GameObject[]{notificationWaitingArea, notificationImmediateArea, logo};
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


