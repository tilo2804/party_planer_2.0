package Speicher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import data.Configuration;
import data.Guest;
import data.Room;

public class Speicher {

	private HashSet<Guest> guestList = new HashSet<Guest>();
	private Room room;
	private Configuration config;
	private ArrayList<Float> partyIndex = new ArrayList<Float>();

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
	
	public void setPartyIndex(Float currentPartyIndex) {
		partyIndex.add(currentPartyIndex);
	}
	
	public Float getCurrentPartyIndex() {
		Iterator<Float> helpIterator = partyIndex.iterator();
		Float res = (float)0;
		while (helpIterator.hasNext()) res = res + helpIterator.next();
		return res/partyIndex.size();
	}
	
}
