package Speicher;

import java.util.HashSet;

import data.Configuration;
import data.Guest;
import data.Room;

public class Speicher {

	private HashSet<Guest> guestList;
	private Room room;
	private Configuration config;

	public Speicher(Room room){
		this.room = room;
	}
	/**
	 * @return the guestList
	 */
	public HashSet<Guest> getGuestList() {
		return guestList;
	}
	
	/**
	 * @param guestList the guestList to set
	 */
	public void setGuestList(HashSet<Guest> guestList) {
		this.guestList = guestList;
	}
	
	/**
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}
	
	/**
	 * @return the config
	 */
	public Configuration getConfig() {
		return config;
	}
	
	/**
	 * @param config the config to set
	 */
	public void setConfig(Configuration config) {
		this.config = config;
	}
}
