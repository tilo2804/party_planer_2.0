package data;

import java.awt.Point;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Guest {

	private String name, job;
	private Point position;
	private Set<Point> posHistory;
	private Map<Guest, Float> distances;
	private Float mood;
	private Set<Float> moodHistory;
	
	public Guest(String name, String job) {
				this.name = name;
				this.job = job;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}

	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Point position) {
		if (this.position != null) this.posHistory.add(this.position);
		this.position = position;
	}

	/**
	 * @return the posHistory
	 */
	public Set<Point> getPosHistory() {
		return posHistory;
	}

	/**
	 * @return the distances
	 */
	public Float getDistance(Guest guest) {
		return this.distances.get(guest);
	}

	/**
	 * @param distances the distances to set
	 */
	public void setDistances(Guest guest, Float distance) {
		this.distances.put(guest, distance);
	}

	/**
	 * @return the mood
	 */
	public Float getMood() {
		return mood;
	}

	/**
	 * @param mood the mood to set
	 */
	public void setMood(Float mood) {
		if (mood == -1) {
			this.moodHistory = new HashSet<Float>();
			this.mood = null;
		} else {
			if (mood != null) this.moodHistory.add(mood);
			this.mood = mood;
		}
		
	}

	/**
	 * @return the moodHistory
	 */
	public Set<Float> getMoodHistory() {
		return moodHistory;
	}
}
