package game.graphic;

import game.graphic.gfx.Image;

public class Gauges {

    private int percentageProductivity;
    private int percentageSocial;
    private int percentageVisibility;

    boolean isRound;


    public Gauges(int percentageProductivity, int percentageSocial, int percentageVisibility, boolean isRound) {

        this.percentageProductivity = percentageProductivity;
        this.percentageSocial = percentageSocial;
        this.percentageVisibility = percentageVisibility;
        this.isRound = isRound;
    }

    public int getPercentageProductivity() {
        return percentageProductivity;
    }

    public void setPercentageProductivity(int percentageProductivity) {
        this.percentageProductivity = percentageProductivity;
    }

    public int getPercentageSocial() {
        return percentageSocial;
    }

    public void setPercentageSocial(int percentageSocial) {
        this.percentageSocial = percentageSocial;
    }

    public int getPercentageVisibility() {
        return percentageVisibility;
    }

    public void setPercentageVisibility(int percentageVisibility) {
        this.percentageVisibility = percentageVisibility;
    }

    public boolean isRound() {
        return isRound;
    }

    public void setRound(boolean round) {
        isRound = round;
    }
}
