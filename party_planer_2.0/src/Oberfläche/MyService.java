package Oberfläche;

import Simulation.Simulation;
import Speicher.Speicher;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MyService extends Service<Void> {
	
	private party_planer_gui_controller controller;
	private Speicher speicher;
	private Simulation simulation;
	
		public MyService(party_planer_gui_controller controller, Speicher speicher, Simulation simulation) {
			this.controller = controller;
			this.speicher = speicher;
			this.simulation = simulation;
		}
	
	   @Override
	   protected Task<Void> createTask() {
	      return new Task<Void>() {
	         @Override
	         protected Void call() throws Exception {
	            
	            simulation.startSimulation();
	            return null;
	         }
	      };
	   }
	 
	}
