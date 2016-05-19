package Simulation;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import Oberfläche.party_planer_gui_controller;
import Speicher.Speicher;
import data.Guest;
import javafx.application.Platform;

public class Simulation {

	private Speicher speicher;
	private party_planer_gui_controller guiController;
	private String state = "pause";
	private Integer noIterations, simulationSpeed, currentIteration = 0;
	private Iterator<Guest> guestListIterator;
	
	public Simulation(Speicher speicher, party_planer_gui_controller guiController){
		try {this.speicher = speicher;} catch(Exception e){ e.getMessage();}
		try {this.guiController = guiController;} catch(Exception e){ e.getMessage();}
		noIterations = speicher.getConfig().getNumberOfIterations();
		simulationSpeed = speicher.getConfig().getSpeedOfSimulation();
		guestListIterator = speicher.getGuestList().iterator();
	}
	
	public void startSimulation(){
		
		state = "start";
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	guiController.refresh();
            }
        });
		
		simulation();
	}
	
	public void pauseSimulation(){
		state = "pause";
	}
	
	public void stopSimulation(){
		state = "stop";
		currentIteration = 0;
		Map<Guest, Point> startPositions = speicher.getConfig().getStartPositions();
		Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
		Guest helpGuest;
		
		for(int i = 0; i < speicher.getGuestList().size(); i++) {
			helpGuest = helpIterator.next();
			helpGuest.setMood((float)-1);
			helpGuest.setPosition(startPositions.get(helpGuest));
		}
		
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	guiController.refresh();
            }
        });
	}
	
	private void simulation(){
		while(state.equals("start")) {
			nextIteration();
		}
	}
	
	public void nextIteration(){
		guestListIterator = speicher.getGuestList().iterator();
			if (currentIteration < noIterations) {
				for(int i = 0; i < speicher.getGuestList().size(); i++) {
					if (state.equals("stop")) break;
					else if((state.equals("start")) || (state.equals("pause"))) {
						nextGuest();
						try {
							Thread.sleep(simulationSpeed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
				}
			} else state = "pause";
		if (state.equals("pause")) {
		Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
		Guest helpGuest;
		for(int i = 0; i < speicher.getGuestList().size(); i++) {
			helpGuest = helpIterator.next();
			helpGuest.setMood(getMoodOfCurrentPosition(helpGuest));
		}
		currentIteration++;
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	guiController.refresh();
            }
        });
		}
	}
	
	public void nextGuest(){
		if ((currentIteration < noIterations)) {
		Guest guest = guestListIterator.next();
		System.out.println(guest.getPosition().getX() + "  " + guest.getPosition().getY());
		guest.setPosition(getBestPosition(guest));	
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	guiController.refresh();
            }
        });
		}
	}
	
	private Point getBestPosition(Guest guest) {
		Point position = new Point(guest.getPosition());
		Point helpPosition = new Point();
		Float bestMood = (float) -1, currentMood;
		Point bestPosition = new Point(-1,-1);
		Boolean validPos = true;
		
		for (int x = -1; x < 2; x++)
			for (int y = -1; y < 2; y++) {
				validPos = true;
				helpPosition.setLocation(position.getX() + x, position.getY() + y);
				Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
				while (helpIterator.hasNext()) {
					if (helpIterator.next().getPosition().equals(helpPosition)) validPos = false;
				}
				if (validPos) {
					if(speicher.getRoom().positionPossible(helpPosition)) {
						if (bestMood == -1) {
							bestMood = getMoodOfPosition(guest, (int)helpPosition.getX(), (int)helpPosition.getY());
							bestPosition.setLocation(helpPosition);
						}
						else {
							currentMood = getMoodOfPosition(guest, (int)helpPosition.getX(), (int)helpPosition.getY());
							if (currentMood < bestMood) {
								bestMood = currentMood;
								bestPosition.setLocation(helpPosition);
							}
						}
					}
				}
			}	
		return bestPosition;
	}
	
	private Float getMoodOfPosition(Guest guest, Integer posX, Integer posY) {
		Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
		Float helpDist, dist, mood = (float)0;
		Guest helpGuest;
		
		for(Integer i = 0; i < speicher.getGuestList().size(); i++) {
			helpGuest = helpIterator.next();
			if (!helpGuest.equals(guest)) {
				helpDist = (float)(Math.pow(posX - helpGuest.getPosition().getX(),2) - Math.pow(posY - helpGuest.getPosition().getY(),2));
				dist = (float)Math.sqrt(helpDist);
				mood = (float)(mood + Math.abs( dist - guest.getDistance(helpGuest)));
			}
		}
		
		return mood;
	}
	
	private Float getMoodOfCurrentPosition(Guest guest) {
		Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
		Float helpDist, dist, mood = (float)0;
		Guest helpGuest;
		
		for(Integer i = 0; i < speicher.getGuestList().size(); i++) {
			helpGuest = helpIterator.next();
			if (!helpGuest.equals(guest)) {
				helpDist = (float)(Math.pow(guest.getPosition().getX() - helpGuest.getPosition().getX(),2) - Math.pow(guest.getPosition().getY() - helpGuest.getPosition().getY(),2));
				dist = (float)Math.sqrt(helpDist);
				mood = (float)(mood + Math.abs( dist - guest.getDistance(helpGuest)));
			}
		}
		
		return mood;
	}
	
}
