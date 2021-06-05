package GUI;

/**
 * This class encompasses several methods and attributes belonging to a Point on the GUI interface.
 * <p>
 * An instance of this class is in actually an ordered set of 2 co-ordinates: x co-ordinate along the horizontal axis, and y co-ordinate along the vertical.<br>
 * A Point object is the general way of which we keep track of the various Drawable objects within the system, they are usually the point of origin for us to begin doing
 * actions such as painting a component or drawing a path between components.
 * <p>
 * It is important to once again note that most of the logic associated to the initialization or altering of points and the GUI objects that use them
 * are both tailored to the specifics given for this assignment and also drawn out of well known formulae that are available online.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 *
 */
public class Point {
	public static final Point defaultPoint = new Point(0,0);
	private int x,y;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Copy Constructor function for the class.
	 * 
	 * @param other - Point object to copy.
	 */
	public Point ( Point other ) {
		this(other.getX(),other.getY());
	}
	
	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
	/**
	 * Compares this object to another, and returns true if they're equal.
	 * <p>
	 * 
	 * Compares both pointers directly, then checks if there's a type mismatch, and
	 * checks the local fields.
	 * 
	 * @return boolean
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	/**
	 * Get function for the field 'x'
	 * 
	 * @return x - int
	 * 
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Set function for the field 'x'
	 * 
	 * @param x - int
	 * 
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Get function for the field 'y'
	 * 
	 * @return y - int
	 * 
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Set function for the field 'y'
	 * 
	 * @param y - int
	 * 
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**Helper function to receive the distance between 2 points, one of them being the local point. (this)
	 * 
	 * @param other - second point to create a line with.
	 * @return double value of the distance between points.
	 */
	public double pointsDistance(Point other) {
		return Math.sqrt( Math.pow(x - other.x, 2) + Math.pow(y-other.y,2));
	}
	
	/**This function receives 4 attributes of an object in transit and uses them to calculate the next position the object should appear on.
	 * <p>
	 * The function uses a known formula to divide the line created by the 2 point parameters proportionally according to the number of steps
	 * the object has to make. <br> It then calculates the next point on that line that the object will appear on according to the number of steps the object
	 * has already made.
	 * 
	 * 
	 * @param start - Point object representing the start of the path.
	 * @param end - Point object representing the end of the path.
	 * @param numOfSteps - integer representing the amount of steps needed to complete the path.
	 * @param stepNum - integer representing the amount of steps the object already made.
	 * 
	 * @return new Point object representing the next location of the object.
	 */
	public static Point proportionalStep (Point start , Point end, int numOfSteps, int stepNum) {
		//start = starting point, end = destination point, numOfSteps = total ticks to arrive, stepNum =  ticks passed since started current race 
		if(numOfSteps == stepNum)
			return end;
		int x1 = start.getX() , y1 = start.getY();
		int x2 = end.getX() , y2 = end.getY();
		int l = numOfSteps - stepNum;
		int k = stepNum;
		int xp = (x1*l + x2*k)/(l+k); // Division of a segment in a given ratio formula for X
		int yp = (y1*l + y2*k)/(l+k); // Division of a segment in a given ratio formula for Y
		return new Point(xp,yp);
		
	}
}
