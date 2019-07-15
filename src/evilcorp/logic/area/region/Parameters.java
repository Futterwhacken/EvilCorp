package evilcorp.logic.area.region;

import evilcorp.logic.GameLogic;
import evilcorp.logic.config.Config;

public final class Parameters extends GameLogic
{
    private static double[] mods = {Config.mod0, Config.mod1, Config.mod2};

    /* Les trois paramètres permettent de déterminer quels modificateurs (mod0, mod1, mod2)
    seront appliqués au différents taux de variation des jauges d'une région */

    /* Regarder le toString() pour explicitation des paramètres */

    // prennent valeurs 0, 1, 2, EVENT INFLUENT CA
    private int tActivity; // t = tolérance
    private int tEnvironment;
    private int tOpposition;

    public Parameters(int tA, int tE, int tO) {
        super();

        tActivity = tA;
        tEnvironment = tE;
        tOpposition = tO;

        if (tActivity < 0 || tActivity > 2) {
            tActivity = 1;
        }

        if (tEnvironment < 0 || tEnvironment > 2) {
            tEnvironment = 1;
        }

        if (tOpposition < 0 || tOpposition > 2) {
            tOpposition = 1;
        }
    }

    public int getActivityTolerance() { return tActivity; }
    public int getEnvironmentTolerance() { return tEnvironment; }
    public int getOppositionTolerance() { return tOpposition; }

    public void setActivityTolerance(int tA) {
        tActivity = tA;
        if (tActivity < 0) tActivity = 0;
        else if (tActivity > 2) tActivity = 2;
    }
    public void setEnvironmentTolerance(int tE) {
        tEnvironment = tE;
        if (tEnvironment < 0) tEnvironment = 0;
        else if (tEnvironment > 2) tEnvironment = 2;
    }
    public void setOppositionTolerance(int tO) {
        tOpposition = tO;
        if (tOpposition < 0) tOpposition = 0;
        else if (tOpposition > 2) tOpposition = 2;
    }

    public double getModActivityPrimary() {
        switch (tActivity) {
            case 0: return Parameters.mods[2]; // likes primary
            case 1: return Parameters.mods[1]; // indifferent
            case 2: return Parameters.mods[0]; // dislikes

            default: return Parameters.mods[1];
        }
    }
    public double getModActivitySecondary() {
        switch (tActivity) {
            case 0: return Parameters.mods[1]; // indifferent
            case 1: return Parameters.mods[2]; // likes secondary
            case 2: return Parameters.mods[1]; // indifferent

            default: return Parameters.mods[1];
        }
    }
    public double getModActivityTertiary() {
        switch (tActivity) {
            case 0: return Parameters.mods[0]; // dislikes
            case 1: return Parameters.mods[1]; // indifferent
            case 2: return Parameters.mods[2]; // likes tertiary

            default: return Parameters.mods[1];
        }
    }

    public double getModEnvironment() {
        switch (tEnvironment) {
            case 0: return Parameters.mods[2]; // peu de normes
            case 1: return Parameters.mods[1]; // moyen
            case 2: return Parameters.mods[0]; // bcp de normes

            default: return Parameters.mods[1];
        }
    }
    public double getModOpposition() {
        switch (tOpposition) {
            case 0: return Parameters.mods[0]; // peu de tolérance opposition
            case 1: return Parameters.mods[1]; // moyen
            case 2: return Parameters.mods[2]; // bcp de tolérance opposition

            default: return Parameters.mods[1];
        }
    }

    public String toString() {
        if (Config.debug) {
            return super.toString() + " | [Parameters] tA: "+tActivity+" tE: "+tEnvironment+" tO: " + tOpposition;
        }

        String s = "";
        switch (tActivity) {
            case 0: s += "\t- Likes primary sector\n"; break;
            case 1: s += "\t- Likes secondary sector\n"; break;
            case 2: s += "\t- Likes tertiary sector\n"; break;
        }
        switch (tEnvironment) {
            case 0: s += "\t- Weak environmental norms\n"; break;
            case 1: s += "\t- Medium environmental norms\n"; break;
            case 2: s += "\t- Strong environmental norms\n"; break;
        }
        switch (tOpposition) {
            case 0: s += "\t- Weak tolerance towards opposition"; break;
            case 1: s += "\t- Medium tolerance towards opposition"; break;
            case 2: s += "\t- Strong tolerance towars opposition"; break;
        }
        return s;
    }
}
