import evilcorp.logic.GameMaster;
import evilcorp.logic.area.region.Region;
import evilcorp.logic.event.*;
import evilcorp.logic.exploitation.*;
import evilcorp.logic.NotificationBus;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;

public class MainConsole {
    private static final Scanner scan = new Scanner(System.in);
    private static GameMaster gm = null;

    private static void display(String str) { System.out.println(str); }
    private static void clear() { for (int i = 0; i < 50; i++) System.out.println(); }
    private static int input() {
        System.out.print(">>> ");
        int input;
        try {
            input = scan.nextInt();
        }
        catch (InputMismatchException ex) {
            input = -1;
        }
        scan.nextLine();
        return input;
    }

    private static void displayGameStatus() {
        display("");
        display("========== Game Status ==========");
        display("Turn "+gm.getTurn());
        display("Points: "+gm.getPoints()+" ("+gm.getRatePoints()+")");
        display("\nProductivity: "+gm.getWorld().getProductivity());
        display("Pollution: "+gm.getWorld().getVisibility());
        display("Social: "+gm.getWorld().getSocial());

        display("");
    }

    private static void displayWaitingNotifications() {
        for (String notif: NotificationBus.getWaitingList()) {
            display("");
            display(notif);
        }
        // clear juste avant prochain tour
        //NotificationBus.clearWaitingList();
    }

    private static void displayImmediateNotifications() {
        for (String notif: NotificationBus.getImmediateList()) {
            display("");
            display(notif);
            display("");
        }
        NotificationBus.clearImmediateList();
    }

    private static void turnMenu() {
        int select, regionSelect;
        do {
            // display game status
            displayGameStatus();
            display("========= Notifications =========");
            displayWaitingNotifications();

            display("");
            display("============ Regions ============");
            for (int i = 0; i < gm.getWorld().getRegionCount(); i++) {
                display(""+gm.getWorld().getRegion(i).toStringLight());
            }

            // menu
            display("");
            display("=========== Main menu ===========");
            display("1) Select region");
            display("2) Next turn");
            display("0) Quit\n");

            select = input();
            clear();

            switch (select) {
                case 1:
                    regionSelectMenu();
                break;

                case 2:
                    NotificationBus.clearWaitingList();
                    gm.nextTurn();
                    if (gm.checkGameStatus() != 1) return;
                break;
            }

        } while (select != 0);
    }

    private static void regionSelectMenu() {
        int regionCount = gm.getWorld().getRegionCount();
        int select;
        do {
            display("");
            display("========= Select region =========");

            for (int i = 0; i < regionCount; i++) {
                display((i+1)+") "+gm.getWorld().getRegion(i).getName());
            }
            display("0) Return\n");

            select = input();
            clear();

            if (select >= 1 && select <= regionCount) {
                regionMenu(gm.getWorld().getRegion(select-1));
            }

        } while (select != 0);
    }

    private static void regionMenu(Region r) {
        int select;
        do {
            display(r+"");
            display("");
            display("========== Region menu ==========");
            display("1) Add exploitation");
            display("2) Remove exploitation");
            display("3) Buy action");
            display("0) Return\n");

            select = input();
            clear();

            switch (select) {
                case 1:
                    addExploitationMenu(r);
                break;

                case 2:
                    removeExploitationMenu(r);
                break;

                case 3:
                    actionMenu(r);
                break;
            }

        } while (select != 0);
    }

    private static void addExploitationMenu(Region r) {
        int select;
        do {
            display(r+"");
            display("");
            display("======== Add exploitation =======");
            display("1) Primary sector");
            display("\tCost: "+ExploitationPrimary.COST);
            display("2) Secondary sector");
            display("\tCost: "+ExploitationSecondary.COST);
            display("3) Tertiary sector");
            display("\tCost: "+ExploitationTertiary.COST);
            display("0) Return\n");

            select = input();
            clear();

            if (select >= 1 && select <= 3) {
                r.buyExploitation(select-1);
                displayImmediateNotifications();
            }

        } while (select != 0);
    }


    private static void removeExploitationMenu(Region r) {
        ArrayList<Exploitation> ex = r.getExploitations();
        int select;
        do {
            display(r+"");
            display("");
            display("====== Remove exploitation ======");
            for (int i = 0; i < ex.size(); i++) {
                display((i+1)+") "+ex.get(i));
                display("\tCost: "+ex.get(i).getRemoveCost());
            }
            display("0) Return\n");

            select = input();
            clear();

            if (select >= 1 && select <= ex.size()) {
                r.removeExploitation(select-1);
                displayImmediateNotifications();
            }

        } while (select != 0);
    }

    private static void actionMenu(Region r) {

        ArrayList<EventBuyable> eb = r.getBuyableEvents();
        int select;
        do {
            display(r+"");
            display("");
            display("========== Action menu ==========");

            for (int i = 0; i < eb.size(); i++) {
                EventBuyable e = eb.get(i);
                if (e.isBought()) {
                    display((i+1)+") [Bought] "+e.getName());
                }
                else {
                    if (e.getUses() == 0) {
                        display((i+1)+") [Depleted] "+e.getName()+", Lasts: "+e.getRemainingTurns()+"/"+e.getDuration()+", Usable: "+e.getUses()+" times");
                    }
                    else if (e.isTriggered()) {
                        display((i+1)+") [Active] "+e.getName()+", Lasts: "+e.getRemainingTurns()+"/"+e.getDuration()+", Usable: "+e.getUses()+" times");
                    }
                    else {
                        display((i+1)+") "+e.getName()+", Lasts: "+e.getRemainingTurns()+"/"+e.getDuration()+", Usable: "+e.getUses()+" times");
                    }
                }
                display("\t"+e.getDescription());
                display("\tCost: "+e.getCost()+", Risk: "+e.getRisk());
            }
            display("0) Return\n");

            select = input();
            clear();

            if (select >= 1 && select <= eb.size()) {
                EventBuyable e = eb.get(select-1);
                r.buyEvent(e);
                displayImmediateNotifications();
            }

        } while (select != 0);
    }

    public static void main(String[] args) {
        try {
            MainConsole.gm = GameMaster.initGameMaster("data/config/logic/");
        }
        catch (NullPointerException e) {
            display("Could not load GameMaster, exiting");
            return;
        }

        turnMenu();

        int status = gm.checkGameStatus();
        if (status != 1) {
            displayGameStatus();

            if (status == 0) {
                display("You lost ! The people won !");
            }
            else if (status == 2) {
                display("You won ! Now to Mars !");
            }
        }
    }
}
