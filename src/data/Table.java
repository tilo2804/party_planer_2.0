package data;

import java.awt.Point;
import java.util.Set;

public class Table {

	private int length, height;
	private Point position;
	private Set<Point> allPoints;
	
	public Table(int length, int height, Point position) {
		Point helpPoint = new Point();
		
		this.length = length;
		this.height = height;
		this.position = position;
		
		for(int i = 0; i < height; i++)
			for(int j = 0; j < length; j++) {
				helpPoint.setLocation(j + position.getX(), i + position.getY());
				allPoints.add(helpPoint);
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
	public int getWidth() {
		return height;
	}
	
	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	
}
