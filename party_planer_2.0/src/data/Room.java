package data;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;

public class Room {

	private HashSet<Table> tables = new HashSet<Table>();
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
		
		if(!tables.equals(null)){
			Iterator<Table> tablesIterator = tables.iterator();
			Iterator<Point> positionIterator;
			for(int i = 0; i < tables.size(); i++) {
				positionIterator = tablesIterator.next().getAllPoints().iterator();
				while(positionIterator.hasNext()) {
					Point helpPoint = positionIterator.next();
					if (helpPoint.distanceSq(position)==(double)0) res = false; //else System.out.println("help: " + helpPoint.getX() + "  " + helpPoint.getY() + "    position:" + position.getX() + " " + position.getY());
				}
				
			}
		}
		
		return res;
	}
	
	/**
	 * @return the tables
	 */
	public HashSet<Table> getTables() {
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
