package data;

import java.awt.Point;
import java.util.Iterator;
import java.util.Set;

public class Room {

	private Set<Table> tables;
	private Integer length, height; //Bsp. wenn Länge = 10 dann ist x im Intervall 0 <= x <= 9
	
	public Room(Integer length, Integer height) {
		this.length = length;
		this.height = height;
	}
	
	public Boolean positionPossible(Point position) {
		Boolean res = true;
		
		if(position.getX() < 0) res = false;
		if(position.getY() < 0) res = false;
		if(position.getX() > (length - 1)) res = false;
		if(position.getY() > (height -1)) res = false;
		
		Iterator<Table> tablesIterator = tables.iterator();
		for(int i = 0; i < tables.size(); i++) {
			if (tablesIterator.next().getAllPoints().contains(position)) res = false;
		}
		
		return res;
	}
	
	/**
	 * @return the tables
	 */
	public Set<Table> getTables() {
		return tables;
	}
	
	/**
	 * @param tables the tables to set
	 */
	public void setTable(Table table) {
		this.tables.add(table);
	}
	
	/**
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}
	
	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return height;
	}
}
