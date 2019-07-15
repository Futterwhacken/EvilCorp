package game.logic;

import java.util.ArrayList;

public final class NotificationBus {
    /* Classe statique permettant de faire remonter
        1) les notifications à afficher tour par tour: waitingList
        2) les notifications à afficher immédiatement: immediateList
    */

    private static ArrayList<String> waitingList = new ArrayList<String>();
    private static ArrayList<String> immediateList = new ArrayList<String>();

    public static ArrayList<String> getWaitingList() {
        return waitingList;
    }

    public static ArrayList<String> getImmediateList() {
        return immediateList;
    }

    public static void addWaitingNotif(String notif) {
        waitingList.add(notif);
    }

    public static void addImmediateNotif(String notif) {
        immediateList.add(notif);
    }

    public static void clearWaitingList() {
        waitingList = new ArrayList<String>();
    }

    public static void clearImmediateList() {
        immediateList = new ArrayList<String>();
    }
}
