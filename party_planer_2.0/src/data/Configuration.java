package data;

import java.awt.Point;
import java.util.Map;

public class Configuration {

	private Integer speedOfSimulation, numberOfIterations;
	private Map<Guest, Point> startPositions;
	
	public Configuration(Integer speedOfSimulation, Integer numberOfIterations, Map<Guest, Point> startPositions){
		this.speedOfSimulation = speedOfSimulation;
		this.numberOfIterations = numberOfIterations;
		this.startPositions = startPositions;
	}
	
	/**
	 * @return the speedOfSimulation
	 */
	public Integer getSpeedOfSimulation() {
		return speedOfSimulation;
	}

	/**
	 * @return the numberOfIterations
	 */
	public Integer getNumberOfIterations() {
		return numberOfIterations;
	}
	
	public Map<Guest, Point> getStartPositions() {
		return startPositions;
	}
	
}
