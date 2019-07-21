package evilcorp.logic.config;

import evilcorp.logic.area.region.*;
import evilcorp.logic.event.*;

import java.io.*;

@SuppressWarnings("Duplicates")
public class ConfigReader
{
    /* Classe statique qui gère l'initialisation des configurations globales,
    des régions et des events du jeu en lisant des fichiers de configuration formattés */

    private static String pass(BufferedReader br) {
        String line;
        do {
            try {
                line = br.readLine();
            }
            catch (IOException ex) {
                return null;
            }
        } while (line.length() == 0 || line.charAt(0) == '#');
        return line;
    }

    public static void readConfig(String filename) {
        String line;
        String[] buffer;

        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            line = pass(reader);
            Config.debug = Boolean.valueOf(line);

            line = pass(reader);
            Config.startingPoints = Integer.valueOf(line);

            line = pass(reader);
            buffer = line.split(" ");

            Config.mod0 = Double.valueOf(buffer[0]);
            Config.mod1 = Double.valueOf(buffer[1]);
            Config.mod2 = Double.valueOf(buffer[2]);

            line = pass(reader);

            Config.maxExpl = Integer.valueOf(line);

            line = reader.readLine();
            Config.dampener = Integer.valueOf(line);

            line = reader.readLine();
            Config.modPoints = Integer.valueOf(line);

            line = reader.readLine();
            Config.scaleSocial0 = Integer.valueOf(line);

            line = reader.readLine();
            Config.scaleSocial1 = Integer.valueOf(line);

            line = reader.readLine();
            Config.scaleSocial2 = Integer.valueOf(line);

            line = reader.readLine();
            Config.scaleSocial3 = Integer.valueOf(line);

            line = reader.readLine();
            Config.strikeRecovery = Double.valueOf(line);

            line = pass(reader);
            buffer = line.split(" ");

            Config.minGauge = Integer.valueOf(buffer[0]);
            Config.maxGauge = Integer.valueOf(buffer[1]);

            line = pass(reader);
            buffer = line.split(" ");

            Config.PrimaryProd = Double.valueOf(buffer[0]);
            Config.PrimaryVisi = Double.valueOf(buffer[1]);
            Config.PrimaryCost = Integer.valueOf(buffer[2]);

            line = reader.readLine();
            buffer = line.split(" ");

            Config.SecondaryProd = Double.valueOf(buffer[0]);
            Config.SecondaryVisi = Double.valueOf(buffer[1]);
            Config.SecondaryCost = Integer.valueOf(buffer[2]);

            line = reader.readLine();
            buffer = line.split(" ");

            Config.TertiaryProd = Double.valueOf(buffer[0]);
            Config.TertiaryVisi = Double.valueOf(buffer[1]);
            Config.TertiaryCost = Integer.valueOf(buffer[2]);

            line = reader.readLine();
            Config.removeMod = Integer.valueOf(line);

            reader.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" +filename + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ filename + "\"");
        }
    }

    public static Region[] readRegions(String filename) {
        String line;
        String[] buffer;

        Region[] regions;

        String name;
        int tA, tE, tO;
        boolean exploitable;

        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            line = pass(reader);

            int regionCount = Integer.valueOf(line);

            regions = new Region[regionCount];

            for (int i = 0; i < regionCount; i++) {
                line = pass(reader);

                name = line;

                line = reader.readLine();
                buffer = line.split(" ");

                tA = Integer.valueOf(buffer[0]);
                tE = Integer.valueOf(buffer[1]);
                tO = Integer.valueOf(buffer[2]);

                line = reader.readLine();

                exploitable = Boolean.valueOf(line);

                regions[i] = new Region(name, new Parameters(tA, tE, tO), exploitable);
            }

            reader.close();

            return regions;
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" +filename + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ filename + "\"");
        }

        return null;
    }

    public static EventTriggerable[] readTriggerableEvents(String filename) {
        String line;
        String[] buffer;

        EventTriggerable[] events;

        String name;
        String description;
        String source;

        int[] effectsParams;
        double[] effectsGauges;

        int duration;
        int uses;

        double[] conditions;
        int[] conditionType;

        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            line = pass(reader);

            int eventCount = Integer.valueOf(line);

            events = new EventTriggerable[eventCount];

            for (int i = 0; i < eventCount; i++) {
                effectsParams = new int[3];
                effectsGauges = new double[3];
                conditions = new double[3];
                conditionType = new int[3];

                line = pass(reader);

                name = line;
                description = reader.readLine();
                source = reader.readLine();

                line = reader.readLine();
                buffer = line.split(" ");

                effectsParams[0] = Integer.valueOf(buffer[0]);
                effectsParams[1] = Integer.valueOf(buffer[1]);
                effectsParams[2] = Integer.valueOf(buffer[2]);

                line = reader.readLine();
                buffer = line.split(" ");

                effectsGauges[0] = Double.valueOf(buffer[0]);
                effectsGauges[1] = Double.valueOf(buffer[1]);
                effectsGauges[2] = Double.valueOf(buffer[2]);

                line = reader.readLine();

                duration = Integer.valueOf(line);

                line = reader.readLine();

                uses = Integer.valueOf(line);

                line = reader.readLine();
                buffer = line.split(" ");

                conditions[0] = Double.valueOf(buffer[0]);
                conditions[1] = Double.valueOf(buffer[1]);
                conditions[2] = Double.valueOf(buffer[2]);

                line = reader.readLine();
                buffer = line.split(" ");

                conditionType[0] = Integer.valueOf(buffer[0]);
                conditionType[1] = Integer.valueOf(buffer[1]);
                conditionType[2] = Integer.valueOf(buffer[2]);

                events[i] = new EventTriggerable(name, description, source, effectsParams, effectsGauges,
                                                duration, uses, null, conditions, conditionType);
            }

            reader.close();

            return events;
        }


        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" +filename + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ filename + "\"");
        }

        return null;
    }

    public static EventBuyable[] readBuyableEvents(String filename) {
        String line;
        String[] buffer;

        EventBuyable[] events;

        String name;
        String description;
        String source;

        int[] effectsParams;
        double[] effectsGauges;

        int duration;
        int uses;

        int cost;
        double risk;


        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            line = pass(reader);

            int eventCount = Integer.valueOf(line);

            events = new EventBuyable[eventCount];

            for (int i = 0; i < eventCount; i++) {
                effectsParams = new int[3];
                effectsGauges = new double[3];

                line = pass(reader);

                name = line;
                description = reader.readLine();
                source = reader.readLine();

                line = reader.readLine();
                buffer = line.split(" ");

                effectsParams[0] = Integer.valueOf(buffer[0]);
                effectsParams[1] = Integer.valueOf(buffer[1]);
                effectsParams[2] = Integer.valueOf(buffer[2]);

                line = reader.readLine();
                buffer = line.split(" ");

                effectsGauges[0] = Double.valueOf(buffer[0]);
                effectsGauges[1] = Double.valueOf(buffer[1]);
                effectsGauges[2] = Double.valueOf(buffer[2]);

                line = reader.readLine();

                duration = Integer.valueOf(line);

                line = reader.readLine();

                uses = Integer.valueOf(line);

                line = reader.readLine();

                cost = Integer.valueOf(line);

                line = reader.readLine();

                risk = Double.valueOf(line);

                events[i] = new EventBuyable(name, description, source, effectsParams, effectsGauges,
                                                duration, uses, null, cost, risk);
            }

            reader.close();

            return events;
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file \"" +filename + "\"");
        }
        catch (IOException ex) {
            System.out.println("Error reading file \""+ filename + "\"");
        }

        return null;
    }
}
