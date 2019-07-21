import game.logic.GameMaster;
import game.logic.area.region.Region;
import game.logic.event.*;
import game.logic.exploitation.*;
import game.logic.NotificationBus;

import game.graphic.Display;
import game.graphic.*;
import game.graphic.gfx.Font;
import game.graphic.gfx.Image;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.io.*;

public class RuntimeGraphic extends AbstractGame {

	private static GameMaster gm = null;
	
	private static boolean clickedLastMenu1 = false;
	
	private static boolean clickedExit = false;
	
	private static boolean clickedLastMenuAddExploitation = false;
	private static boolean clickedLastMenuRemoveExploitation = false;
	private static boolean clickedLastMenuBuyAction = false;
	
	private static boolean clickedAddExploitation = false;
	
	private static boolean clickedAddPrimary = false;
	private static boolean clickedAddSecondary = false;
	private static boolean clickedAddTertiary = false;

	private static boolean clickedRemovePrimary = false;
	private static boolean clickedRemoveSecondary = false;
	private static boolean clickedRemoveTertiary = false;

	private static boolean clickedBuyAction1 = false;
	private static boolean clickedBuyAction2 = false;
	private static boolean clickedBuyAction3 = false;
	private static boolean clickedBuyAction4 = false;
	private static boolean clickedBuyAction5 = false;

	private static boolean clickedLastTurn = false;
	private static boolean clickedCurrentTurn = false;
	
	private static boolean engagedLast = false;
	private static boolean engagedCurrent = false;
	private static int engaging = -1;
	private static int engagingLast = -1;
	
	private static int[] engagePath;
	
	private static boolean clickedLastNotif = false;
	private static boolean clickedCurrentNotif = false;

	private static Region currentMenuRegion = null;

	private static int countWaitingNotification = 0;
	private static int countImmeiateNotification = 0;

	private static EventBuyable eb = null;

    public RuntimeGraphic() {

		/* REGION SELECTION AREAS */
		clicMouseableRegion = new Mouseable[gm.getWorld().getRegionCount()];

		clicMouseableRegion[0] = new Mouseable(190, 40, 250, 75);		// Europe
		clicMouseableRegion[1] = new Mouseable(25, 10, 150, 100);		// North America
		clicMouseableRegion[2] = new Mouseable(110, 110, 160, 190);		// South America
		clicMouseableRegion[3] = new Mouseable(180, 80, 260, 150);		// Africa
		clicMouseableRegion[4] = new Mouseable(260, 30, 380, 160);		// Asia


		/* CHOICE SELECTION AREAS */
		clicMouseableChoice = new Mouseable[6];
		
		clicMouseableChoice[0] = new Mouseable(10, 180, 30, 190);		// EXIT
		clicMouseableChoice[1] = new Mouseable(30, 220, 120, 226);		// CHOICE 1
		clicMouseableChoice[2] = new Mouseable(30, 235, 120, 241);		// CHOICE 2
		clicMouseableChoice[3] = new Mouseable(30, 250, 120, 256);		// CHOICE 3
		clicMouseableChoice[4] = new Mouseable(30, 265, 120, 271);		// CHOICE 4
		clicMouseableChoice[5] = new Mouseable(30, 280, 120, 286);		// CHOICE 5

		/* NEXT TURN BUTTON */
		tour = "";
		clicMouseableNextTurn = new Mouseable(290, 180, 330, 190);

		/* MENU PATH HANDLING */
		engagePath = new int[3];
		
		engagePath[0] = 0;
		engagePath[1] = 0;
		engagePath[2] = 0;
		/*
		 * 0th -> display menu ? 1 : 0;
		 * 1st -> selection : (1) add exploitation ; (2) remove exploitation ; (3) buy action.
		 * 2nd ->				1) primary				1) primary					1) 1st act°
		 *						2) secondary			2) secondary				2) 2nd act°
		 *						3) tertiary				3) tertiary					3) 3rd act°
		 *																			4) 4th act°
		 *																			5) 5th act°
		 */
		
		immediateNotifications = new ArrayList<String>();
		waitingNotifications = new ArrayList<String>();

        rendererImage = new ArrayList<Image>();
        rendererXImage = new ArrayList<Integer>();
        rendererYImage = new ArrayList<Integer>();

      	rendererText = new ArrayList<String>();
        rendererTextColor = new ArrayList<Integer>();
        rendererXText = new ArrayList<Integer>();
        rendererYText = new ArrayList<Integer>();

		rendererTextMenu = new ArrayList<String>();
        rendererTextColorMenu = new ArrayList<Integer>();
        rendererXTextMenu = new ArrayList<Integer>();
        rendererYTextMenu = new ArrayList<Integer>();

		rendererTextAddExploitation = new ArrayList<String>();
        rendererTextColorAddExploitation = new ArrayList<Integer>();
        rendererXTextAddExploitation = new ArrayList<Integer>();
        rendererYTextAddExploitation = new ArrayList<Integer>();

		rendererTextRemoveExploitation = new ArrayList<String>();
        rendererTextColorRemoveExploitation = new ArrayList<Integer>();
        rendererXTextRemoveExploitation = new ArrayList<Integer>();
        rendererYTextRemoveExploitation = new ArrayList<Integer>();

		rendererTextBuyAction = new ArrayList<String>();
        rendererTextColorBuyAction = new ArrayList<Integer>();
        rendererXTextBuyAction = new ArrayList<Integer>();
        rendererYTextBuyAction = new ArrayList<Integer>();

		immediateNotifications = new ArrayList<String>();
		waitingNotifications= new ArrayList<String>();

        rendererGauges = new ArrayList<Gauges>();

		/* chargement des jauges */
        for (int i = 0; i < gm.getWorld().getRegionCount(); i++)
				rendererGauges.add(new Gauges((int) gm.getWorld().getRegion(i).getProductivity(), (int) gm.getWorld().getRegion(i).getVisibility(), (int) gm.getWorld().getRegion(i).getSocial(), true));
		int k = gm.getWorld().getRegionCount();
			rendererGauges.add(new Gauges((int) gm.getWorld().getProductivity(), (int) gm.getWorld().getVisibility(), (int) gm.getWorld().getSocial(), false));


		/* chargement de la map */
        rendererImage.add(new Image("/data/resources/map.png"));
        rendererXImage.add(27);
        rendererYImage.add(-10);

		/* chargement des affichages de base */
		/* panneau droit */
        rendererText.add("  - 100%");
        rendererTextColor.add(0xffffffff);
        rendererXText.add(460);
        rendererYText.add(194);

        rendererText.add("  - 00%");
        rendererTextColor.add(0xffffffff);
        rendererXText.add(460);
        rendererYText.add(268);

        rendererText.add("social unrest");
        rendererTextColor.add(0xff00ff00);
        rendererXText.add(415);
        rendererYText.add(50);

        rendererText.add("pollution level");
        rendererTextColor.add(0xff00ffff);
        rendererXText.add(410);
        rendererYText.add(75);

        rendererText.add("production level");
        rendererTextColor.add(0xffff0000);
        rendererXText.add(407);
        rendererYText.add(100);

		rendererText.add("Jauges mondiales");
        rendererTextColor.add(0xffff01ff);
        rendererXText.add(370);
        rendererYText.add(280);

		rendererText.add("Turn number " + tour);
        rendererTextColor.add(0xffa101ff);
        rendererXText.add(420);
        rendererYText.add(10);

		/* panneau gauche */
        rendererText.add("< Menu >");
        rendererTextColor.add(0xff0000ff);
        rendererXText.add(40);
        rendererYText.add(180);

		rendererText.add("next turn #");
		rendererTextColor.add(0xffa101ff);
		rendererXText.add(290);
		rendererYText.add(180);

		/* panneau central */

        rendererText.add("< notifications >");
        rendererTextColor.add(0xffffff00);
        rendererXText.add(200);
        rendererYText.add(180);

		/* MENU1 */
		rendererTextMenu.add("add exploitation");
		rendererTextColorMenu.add(0xff00a1ff);
		rendererXTextMenu.add(30);
		rendererYTextMenu.add(220);
		rendererTextMenu.add("remove exploitation");
		rendererTextColorMenu.add(0xff00a1ff);
		rendererXTextMenu.add(30);
		rendererYTextMenu.add(235);
		rendererTextMenu.add("buy action");
		rendererTextColorMenu.add(0xff00a1ff);
		rendererXTextMenu.add(30);
		rendererYTextMenu.add(250);
		rendererTextMenu.add("exit");
		rendererTextColorMenu.add(0xff00a1ff);
		rendererXTextMenu.add(clicMouseableChoice[0].getX1());
		rendererYTextMenu.add(clicMouseableChoice[0].getY1());

		/* MENU ADD EXPLOITATION */
		rendererTextAddExploitation.add("Primary sector");
		rendererTextColorAddExploitation.add(0xff00a1ff);
		rendererXTextAddExploitation.add(30);
		rendererYTextAddExploitation.add(220);
		rendererTextAddExploitation.add("secondary sector");
		rendererTextColorAddExploitation.add(0xff00a1ff);
		rendererXTextAddExploitation.add(30);
		rendererYTextAddExploitation.add(235);
		rendererTextAddExploitation.add("tertiariary sector");
		rendererTextColorAddExploitation.add(0xff00a1ff);
		rendererXTextAddExploitation.add(30);
		rendererYTextAddExploitation.add(250);
		rendererTextAddExploitation.add("return");
		rendererTextColorAddExploitation.add(0xff00a1ff);
		rendererXTextAddExploitation.add(clicMouseableChoice[0].getX1());
		rendererYTextAddExploitation.add(clicMouseableChoice[0].getY1());

		/* MENU REMOVE EXPLOITATION */
		rendererTextRemoveExploitation.add("Primary sector");
		rendererTextColorRemoveExploitation.add(0xff00a1ff);
		rendererXTextRemoveExploitation.add(30);
		rendererYTextRemoveExploitation.add(220);
		rendererTextRemoveExploitation.add("secondary sector");
		rendererTextColorRemoveExploitation.add(0xff00a1ff);
		rendererXTextRemoveExploitation.add(30);
		rendererYTextRemoveExploitation.add(235);
		rendererTextRemoveExploitation.add("tertiariary sector");
		rendererTextColorRemoveExploitation.add(0xff00a1ff);
		rendererXTextRemoveExploitation.add(30);
		rendererYTextRemoveExploitation.add(250);
		rendererTextRemoveExploitation.add("return");
		rendererTextColorRemoveExploitation.add(0xff00a1ff);
		rendererXTextRemoveExploitation.add(clicMouseableChoice[0].getX1());
		rendererYTextRemoveExploitation.add(clicMouseableChoice[0].getY1());

		/* MENU BUY ACTION */
		rendererTextBuyAction.add("activity1");
		rendererTextColorBuyAction.add(0xff00a1ff);
		rendererXTextBuyAction.add(30);
		rendererYTextBuyAction.add(220);
		rendererTextBuyAction.add("activity2");
		rendererTextColorBuyAction.add(0xff00a1ff);
		rendererXTextBuyAction.add(30);
		rendererYTextBuyAction.add(235);
		rendererTextBuyAction.add("activity3");
		rendererTextColorBuyAction.add(0xff00a1ff);
		rendererXTextBuyAction.add(30);
		rendererYTextBuyAction.add(250);
		rendererTextBuyAction.add("activity4");
		rendererTextColorBuyAction.add(0xff00a1ff);
		rendererXTextBuyAction.add(30);
		rendererYTextBuyAction.add(265);
		rendererTextBuyAction.add("activity5");
		rendererTextColorBuyAction.add(0xff00a1ff);
		rendererXTextBuyAction.add(30);
		rendererYTextBuyAction.add(280);
		rendererTextBuyAction.add("return");
		rendererTextColorBuyAction.add(0xff00a1ff);
		rendererXTextBuyAction.add(clicMouseableChoice[0].getX1());
		rendererYTextBuyAction.add(clicMouseableChoice[0].getY1());
    }

    @Override
    public void update(GameLoop gameLoop, double updateHertz) {
		cursorZone = "";
		cursorProd = -1;
		cursorVisi = -1;
		cursorSoc = -1;

		clickedLastTurn = clickedCurrentTurn;
		engagedLast = engagedCurrent;
		engagingLast = engaging;

		this.rendererText.add(cursorZone);
		rendererXText.add(220);
        rendererYText.add(240);
		rendererTextColor.add(0xffffff00);
		int x = gameLoop.getInput().getMouseX();
		int y = gameLoop.getInput().getMouseY();


		/* CHOICES CLICK MANAGEMENT */
		for (int i = 0; i < clicMouseableChoice.length; i++) {
			if ((((clicMouseableChoice[i].getX1() <= x) && (clicMouseableChoice[i].getX2() >= x) && (clicMouseableChoice[i].getY1() <= y) && (clicMouseableChoice[i].getY2() >= y)))) {
				if (gameLoop.getInput().isButton(MouseEvent.BUTTON1)) {
					engagedCurrent = true;
					engaging = i;
				} else {
					engagedCurrent = false;
					engaging = -1;
				}
			}
		}
		
		if (engagePath[2] != 0) engagePath[2] = 0;
		
		if (engagedLast == true && engagedCurrent == false) {
			switch(engagingLast) {
				case -1:
					break;
				case 0:
					if (engagePath[2] != 0) {
						engagePath[2] = engagingLast;
					} else if (engagePath[1] != 0) {
						engagePath[1] = engagingLast;
					} else if (engagePath[0] == 1) {
						currentMenuRegion = null;
						engagePath[0] = engagingLast;
					}
					break;
				case 1:
					if (engagePath[0] == 1 && engagePath[1] == 0) {
						engagePath[1] = engagingLast;
					} else if (engagePath[1] == 0) {
						engagePath[1] = engagingLast;
					} else if (engagePath[2] == 0) {
						engagePath[2] = engagingLast;
					}
					break;
				case 2:
					if (engagePath[0] == 1 && engagePath[1] == 0) {
						engagePath[1] = engagingLast;
					} else if (engagePath[1] == 0) {
						engagePath[1] = engagingLast;
					} else if (engagePath[2] == 0) {
						engagePath[2] = engagingLast;
					}
					break;
				case 3:
					if (engagePath[0] == 1 && engagePath[1] == 0) {
						engagePath[1] = engagingLast;
					} else if (engagePath[1] == 0) {
						engagePath[1] = engagingLast;
					} else if (engagePath[2] == 0) {
						engagePath[2] = engagingLast;
					}
					break;
				case 4:
					if (engagePath[0] == 1 && engagePath[1] == 3) {
						engagePath[2] = engagingLast;
					}
					break;
				case 5:
					if (engagePath[0] == 1 && engagePath[1] == 3) {
						engagePath[2] = engagingLast;
					}
					break;
			}
			
			System.out.println("engaging: " + engagingLast + " {" + engagePath[0] + ", " + engagePath[1] + ", " + engagePath[2] + "}");
		}
		
        
		/* NEXT TURN MANAGEMENT */
		if ((((clicMouseableNextTurn.getX1() <= x) && (clicMouseableNextTurn.getX2() >= x) && (clicMouseableNextTurn.getY1() <= y) && (clicMouseableNextTurn.getY2() >= y)))) {
			if (gameLoop.getInput().isButton(MouseEvent.BUTTON1)) {
				clickedCurrentTurn = true;
			} else {
				clickedCurrentTurn = false;
			}
		}

		tour = String.valueOf(gm.getTurn());
		if (clickedLastTurn == true && clickedCurrentTurn == false) {
			while (waitingNotifications.size() > 0) {
				waitingNotifications.remove(0);
			}
			while (immediateNotifications.size() > 0){
				immediateNotifications.remove(0);
			}
			gm.nextTurn();
		}

		for (String notif: NotificationBus.getWaitingList()) {
			waitingNotifications.add(notif);
		}

		for (String notif: NotificationBus.getImmediateList()) {
			immediateNotifications.add(notif);
		}
        

		/* REGION */
		for (int i = 0; i < clicMouseableRegion.length; i++) {
			Mouseable m = clicMouseableRegion[i];

			/* CURSEUR */
			if (((m.getX1() <= x) && (m.getX2() >= x) && (m.getY1() <= y) && (m.getY2() >= y))) {
				cursorZone = "zone: ";
				cursorZone += gm.getWorld().getRegion(i).getName();
				cursorProd = gm.getWorld().getRegion(i).getProductivity();
				cursorVisi = gm.getWorld().getRegion(i).getVisibility();
				cursorSoc = gm.getWorld().getRegion(i).getSocial();

				/* CLIC */
				if (gameLoop.getInput().isButton(MouseEvent.BUTTON1)) {
					if (engagePath[0] == 1) {
						currentMenuRegion = null;
						engagePath[0] = 0;
					}
					engagePath[0] = 1;
					currentMenuRegion = gm.getWorld().getRegion(i);

					if (engagePath[1] != 0) {
						engagePath[0] = 1;
						engagePath[1] = 0;
					}
				}
			}
		}

		for (int i = 0; i < rendererGauges.size() - 1; i++)
			rendererGauges.set(i, new Gauges((int) gm.getWorld().getRegion(i).getProductivity(), (int) gm.getWorld().getRegion(i).getVisibility(), (int) gm.getWorld().getRegion(i).getSocial(), true));
		int k = rendererGauges.size() - 1;
			rendererGauges.set(k, new Gauges((int) gm.getWorld().getProductivity(), (int) gm.getWorld().getVisibility(), (int) gm.getWorld().getSocial(), false));
		
        display(gameLoop, gameLoop.getDisplay());
    }


    @Override
    public void display(GameLoop gameLoop, Display disp) {
		/* affichage des images */
        for (int i = 0; i < rendererImage.size(); i++) {
            disp.drawImage(rendererImage.get(i), rendererXImage.get(i), rendererYImage.get(i));
        }

		/* affichage des jauges */
        for (int i = 0; i < rendererGauges.size(); i++) {
            if (rendererGauges.get(i).isRound() == true) {
                disp.drawGaugeMini(rendererGauges.get(i), posXGauges[i], posYGauges[i]);
            } else {
                disp.drawGlobalRates(rendererGauges.get(i));
            }
        }

		if (gm.getRatePoints() >= 0)
			disp.drawText("FUNDS @" + gm.getPoints() + "  (+" + gm.getRatePoints() + "%)", 10, 290, 0xffffff00);
		else
			disp.drawText("FUNDS @" + gm.getPoints() + "  (-" + gm.getRatePoints() + "%)", 10, 290, 0xffffff00);


		if (immediateNotifications.size() > 0) {
			disp.drawNotification(immediateNotifications.get(0).split("\n"), 150, 250);
		}
		if (waitingNotifications.size() > 0) {
			disp.drawNotification(waitingNotifications.get(0).split("\n"), 150, 200);
		}

		/* affichage du texte */
        for (int i = 0; i < rendererText.size(); i++) {
            disp.drawText(rendererText.get(i), rendererXText.get(i), rendererYText.get(i), rendererTextColor.get(i));
        }

		/* affichage dynamique en fonction de position du curseur */
		disp.drawText(cursorZone, 410, 30, 0xffff01ff);
		if (cursorSoc != -1) {
			disp.drawText(String.valueOf((int) cursorProd), 440, 110, 0xffff01ff);
			disp.drawText(String.valueOf((int) cursorVisi), 440, 85, 0xffff01ff);
			disp.drawText(String.valueOf((int) cursorSoc), 440, 60, 0xffff01ff);
		}

		/* affichage du nombre de tours */
		disp.drawText(String.valueOf(gm.getTurn()), 475, 10, 0xffffc1ff);

		/* affichage du menu */
		displayMenu(gameLoop, gameLoop.getDisplay(), engagePath[0] == 1 && engagePath[1] == 0);
		displayAddExploitation(gameLoop, gameLoop.getDisplay(), engagePath[0] == 1 && engagePath[1] == 1);
		displayRemoveExploitation(gameLoop, gameLoop.getDisplay(), engagePath[0] == 1 && engagePath[1] == 2);
		displayBuyAction(gameLoop, gameLoop.getDisplay(), engagePath[0] == 1 && engagePath[1] == 3);
	}

	public void displayMenu(GameLoop gameLoop, Display disp, boolean isDisp) {
		if (isDisp) {
			/* affichage du texte */
			for (int i = 0; i < rendererTextMenu.size(); i++) {
				disp.drawText(rendererTextMenu.get(i), rendererXTextMenu.get(i), rendererYTextMenu.get(i), rendererTextColorMenu.get(i));
			}
		}
	}

	public void displayAddExploitation(GameLoop gameLoop, Display disp, boolean isDisp) {
		if (isDisp) {
			/* affichage du texte */
			for (int i = 0; i < rendererTextAddExploitation.size(); i++) {
				disp.drawText(rendererTextAddExploitation.get(i), rendererXTextAddExploitation.get(i), rendererYTextAddExploitation.get(i), rendererTextColorAddExploitation.get(i));
			}
		}
	}

	public void displayRemoveExploitation(GameLoop gameLoop, Display disp, boolean isDisp) {
		if (isDisp) {
			/* affichage du texte */
			for (int i = 0; i < rendererTextRemoveExploitation.size(); i++) {
				disp.drawText(rendererTextRemoveExploitation.get(i), rendererXTextRemoveExploitation.get(i), rendererYTextRemoveExploitation.get(i), rendererTextColorRemoveExploitation.get(i));
			}
		}
	}

	public void displayBuyAction(GameLoop gameLoop, Display disp, boolean isDisp) {
		if (isDisp) {
			/* affichage du texte */
			for (int i = 0; i < rendererTextBuyAction.size(); i++) {
				disp.drawText(rendererTextBuyAction.get(i), rendererXTextBuyAction.get(i), rendererYTextBuyAction.get(i), rendererTextColorBuyAction.get(i));
			}
		}
	}
	
	public void displayMenuPath(GameLoop gameLoop, Display disp, boolean isDisp) {
		if (isDisp) {
			return;
		}
	}
	


    public static void main(String[] args) {
		RuntimeGraphic.gm = GameMaster.initGameMaster("data/config/");
        GameLoop gameLoop = new GameLoop(new RuntimeGraphic());
        gameLoop.start();
    }
}
