package evilcorp.logic.config;

public class Config {
    /* Classe statique de configuration dans laquelle sont rangées
    des valeurs globales du jeu, dans laquelle les différentes classes iront chercher
    la valeur dont elles sont besoin, elle est initialisée par ConfigReader.readConfig(String filename) */

    // debug
    public static boolean debug;

    // gamemaster
    public static int startingPoints;

    // params
    public static double mod0;
    public static double mod1;
    public static double mod2;

    // regions
    public static int maxExpl;
    public static int dampener;
    public static int modPoints;
    public static int scaleSocial0;
    public static int scaleSocial1;
    public static int scaleSocial2;
    public static int scaleSocial3;
    public static double strikeRecovery;

    // gauges
    public static double minGauge;
    public static double maxGauge;

    // exploitations
    public static double PrimaryProd;
    public static double PrimaryVisi;
    public static int PrimaryCost;

    public static double SecondaryProd;
    public static double SecondaryVisi;
    public static int SecondaryCost;

    public static double TertiaryProd;
    public static double TertiaryVisi;
    public static int TertiaryCost;

    public static int removeMod;
}
