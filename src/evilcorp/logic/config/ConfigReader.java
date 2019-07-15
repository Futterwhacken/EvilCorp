package evilcorp.logic.config;

import evilcorp.logic.area.region.*;
import evilcorp.logic.event.*;

import java.io.*;

public class ConfigReader
{
    /* Classe statique qui gère l'initialisation des configurations globales,
    des régions et des events du jeu en lisant des fichiers de configuration formattés */

    public static String pass(BufferedReader br) {
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
            Config.debug = (Boolean.valueOf(line)).booleanValue();

            line = pass(reader);
            Config.startingPoints = (Integer.valueOf(line)).intValue();

            line = pass(reader);
            buffer = line.split(" ");

            Config.mod0 = (Double.valueOf(buffer[0])).doubleValue();
            Config.mod1 = (Double.valueOf(buffer[1])).doubleValue();
            Config.mod2 = (Double.valueOf(buffer[2])).doubleValue();

            line = pass(reader);

            Config.maxExpl = (Integer.valueOf(line)).intValue();

            line = reader.readLine();
            Config.dampener = (Integer.valueOf(line)).intValue();

            line = reader.readLine();
            Config.modPoints = (Integer.valueOf(line)).intValue();

            line = reader.readLine();
            Config.scaleSocial0 = (Integer.valueOf(line)).intValue();

            line = reader.readLine();
            Config.scaleSocial1 = (Integer.valueOf(line)).intValue();

            line = reader.readLine();
            Config.scaleSocial2 = (Integer.valueOf(line)).intValue();

            line = reader.readLine();
            Config.scaleSocial3 = (Integer.valueOf(line)).intValue();

            line = reader.readLine();
            Config.strikeRecovery = (Double.valueOf(line)).doubleValue();

            line = pass(reader);
            buffer = line.split(" ");

            Config.minGauge = (Integer.valueOf(buffer[0])).intValue();
            Config.maxGauge = (Integer.valueOf(buffer[1])).intValue();

            line = pass(reader);
            buffer = line.split(" ");

            Config.PrimaryProd = (Double.valueOf(buffer[0])).doubleValue();
            Config.PrimaryVisi = (Double.valueOf(buffer[1])).doubleValue();
            Config.PrimaryCost = (Integer.valueOf(buffer[2])).intValue();

            line = reader.readLine();
            buffer = line.split(" ");

            Config.SecondaryProd = (Double.valueOf(buffer[0])).doubleValue();
            Config.SecondaryVisi = (Double.valueOf(buffer[1])).doubleValue();
            Config.SecondaryCost = (Integer.valueOf(buffer[2])).intValue();

            line = reader.readLine();
            buffer = line.split(" ");

            Config.TertiaryProd = (Double.valueOf(buffer[0])).doubleValue();
            Config.TertiaryVisi = (Double.valueOf(buffer[1])).doubleValue();
            Config.TertiaryCost = (Integer.valueOf(buffer[2])).intValue();

            line = reader.readLine();
            Config.removeMod = (Integer.valueOf(line)).intValue();

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

            int regionCount = (Integer.valueOf(line)).intValue();

            regions = new Region[regionCount];

            for (int i = 0; i < regionCount; i++) {
                line = pass(reader);

                name = line;

                line = reader.readLine();
                buffer = line.split(" ");

                tA = (Integer.valueOf(buffer[0])).intValue();
                tE = (Integer.valueOf(buffer[1])).intValue();
                tO = (Integer.valueOf(buffer[2])).intValue();

                line = reader.readLine();

                exploitable = (Boolean.valueOf(line)).booleanValue();

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

            int eventCount = (Integer.valueOf(line)).intValue();

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

                effectsParams[0] = (Integer.valueOf(buffer[0])).intValue();
                effectsParams[1] = (Integer.valueOf(buffer[1])).intValue();
                effectsParams[2] = (Integer.valueOf(buffer[2])).intValue();

                line = reader.readLine();
                buffer = line.split(" ");

                effectsGauges[0] = (Double.valueOf(buffer[0])).doubleValue();
                effectsGauges[1] = (Double.valueOf(buffer[1])).doubleValue();
                effectsGauges[2] = (Double.valueOf(buffer[2])).doubleValue();

                line = reader.readLine();

                duration = (Integer.valueOf(line)).intValue();

                line = reader.readLine();

                uses = (Integer.valueOf(line)).intValue();

                line = reader.readLine();
                buffer = line.split(" ");

                conditions[0] = (Double.valueOf(buffer[0])).doubleValue();
                conditions[1] = (Double.valueOf(buffer[1])).doubleValue();
                conditions[2] = (Double.valueOf(buffer[2])).doubleValue();

                line = reader.readLine();
                buffer = line.split(" ");

                conditionType[0] = (Integer.valueOf(buffer[0])).intValue();
                conditionType[1] = (Integer.valueOf(buffer[1])).intValue();
                conditionType[2] = (Integer.valueOf(buffer[2])).intValue();

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

            int eventCount = (Integer.valueOf(line)).intValue();

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

                effectsParams[0] = (Integer.valueOf(buffer[0])).intValue();
                effectsParams[1] = (Integer.valueOf(buffer[1])).intValue();
                effectsParams[2] = (Integer.valueOf(buffer[2])).intValue();

                line = reader.readLine();
                buffer = line.split(" ");

                effectsGauges[0] = (Double.valueOf(buffer[0])).doubleValue();
                effectsGauges[1] = (Double.valueOf(buffer[1])).doubleValue();
                effectsGauges[2] = (Double.valueOf(buffer[2])).doubleValue();

                line = reader.readLine();

                duration = (Integer.valueOf(line)).intValue();

                line = reader.readLine();

                uses = (Integer.valueOf(line)).intValue();

                line = reader.readLine();

                cost = (Integer.valueOf(line)).intValue();

                line = reader.readLine();

                risk = (Double.valueOf(line)).doubleValue();

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
