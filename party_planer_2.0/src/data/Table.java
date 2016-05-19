package data;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Table {

	private int length, height;
	private Point position;
	private HashSet<Point> allPoints = new HashSet<Point>();
	
	public Table(int length, int height, Point position) {

		this.length = length;
		this.height = height;
		this.position = position;
		
		for(int i = 0; i < height; i++)
			for(int j = 0; j < length; j++) {
				allPoints.add(new Point((int)(j + position.getX()), (int)(i + position.getY())));
			}
	}
	
 	public Set<Point> getAllPoints() {
 		return allPoints;
 	}
	
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the width
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	
}
