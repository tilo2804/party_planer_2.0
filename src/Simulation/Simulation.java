package Simulation;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import Oberfläche.party_planer_gui_controller;
import Speicher.Speicher;
import data.Guest;

public class Simulation {

	private Speicher speicher;
	private party_planer_gui_controller guiController;
	private String state;
	private Integer noIterations, simulationSpeed, currentIteration = 0;
	private Iterator<Guest> guestListIterator;
	
	public Simulation(Speicher speicher, party_planer_gui_controller guiController){
		try {this.speicher = speicher;} catch(Exception e){ e.getMessage();}
		try {this.guiController = guiController;} catch(Exception e){ e.getMessage();}
	}
	
	public void startSimulation(){
		noIterations = speicher.getConfig().getNumberOfIterations();
		simulationSpeed = speicher.getConfig().getSpeedOfSimulation();
		guestListIterator = speicher.getGuestList().iterator();
		state = "start";
		guiController.refresh();
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
		
		guiController.refresh();
	}
	
	private void simulation(){
		while(state.equals("start")) {
			nextIteration();
		}
	}
	
	public void nextIteration(){
			if (currentIteration < noIterations)
				for(int i = 0; i < speicher.getGuestList().size(); i++) {
					if (state.equals("pause")) while(state.equals("pause")){}
					else if (state.equals("stop")) break;
					else if(state.equals("start")) {
						nextGuest();
						try {
							wait(simulationSpeed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} 
				}
			Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
			Guest helpGuest;
			for(int i = 0; i < speicher.getGuestList().size(); i++) {
				helpGuest = helpIterator.next();
				helpGuest.setMood(getMoodOfCurrentPosition(helpGuest));
			}
			currentIteration++;
	}
	
	public void nextGuest(){
		Guest guest = guestListIterator.next();
		guest.setPosition(getBestPosition(guest));	
		guiController.refresh();
	}
	
	private Point getBestPosition(Guest guest) {
		Point position = new Point(guest.getPosition());
		Point helpPosition = new Point();
		Float bestMood = (float) -1, currentMood;
		Point bestPosition = new Point();
		
		for (int x = -1; x < 2; x++)
			for (int y = -1; y < 2; y++)
				if ((y != 0) || (x != 0)) {
					helpPosition.setLocation(position.getX() + x, position.getY() + y);
					if(speicher.getRoom().positionPossible(helpPosition))
						if (bestMood == -1) bestMood = getMoodOfPosition(guest, (int)helpPosition.getX(), (int)helpPosition.getY());
						else {
							currentMood = getMoodOfPosition(guest, (int)helpPosition.getX(), (int)helpPosition.getY());
							if (currentMood < bestMood) {
								bestMood = currentMood;
								bestPosition = helpPosition;
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
