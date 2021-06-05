package GUI;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class is an abstract representation of the Drawable truck elements in the GUI.
 * <p>
 * It contains several functions and attributes that are shared among the various Drawable trucks we have.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DrawObject 
 *
 */
public abstract class DrawTruck extends DrawObject {

	public static int numTrucks = 0;
	static int wheelDiameter = 10;
	static int bodySize = 16;
	static Color wheelColor = Color.black;
	private Point startPoint, destination;
	int totalTicksToArrive;
	int ticksOver;
	private final int truckId;
	boolean isHidden;

	/**
	 * Constructor function for the class.
	 */
	public DrawTruck() {
		super(Point.defaultPoint);
		destination = Point.defaultPoint;
		startPoint = new Point(this.getCurrentLocation());
		totalTicksToArrive = 0;
		ticksOver = 0;
		truckId = 2000 + numTrucks;
		numTrucks++;
		isHidden = true;
	}
	/**
	 * Copy constructor for the class.
	 * Shallow copy because copying immutable fields.
	 */
	public DrawTruck(DrawTruck other) {
		super((DrawObject)other);
		destination = other.getDestination();
		startPoint = other.startPoint;
		totalTicksToArrive = other.totalTicksToArrive;
		ticksOver = other.ticksOver;
		truckId = other.truckId;
		isHidden = other.isHidden;
	}

	/**
	 * Overrides paintComponent as used by Swing components, with custom logic to
	 * fit this class.
	 * <p>
	 * This function is later used by the DisplayPanel to paint this class instance
	 * on the panel.
	 * 
	 * @param g - Graphics object
	 */
	public void paintComponent(Graphics g) {
		if (isHidden)
			return;
		g.setColor(this.getColor());
		int x = this.getCurrentLocation().getX() -8;
		int y = this.getCurrentLocation().getY() -8;
		g.fillRect(x, y, bodySize, bodySize);
		g.setColor(wheelColor);
		g.fillOval(x - 4, y - 4, wheelDiameter, wheelDiameter);
		g.fillOval(x + 8, y - 4, wheelDiameter, wheelDiameter);
		g.fillOval(x + 8, y + 8, wheelDiameter, wheelDiameter);
		g.fillOval(x - 4, y + 8, wheelDiameter, wheelDiameter);
	}

	/**
	 * Get function for the field 'destination'
	 * 
	 * @return destination - Point
	 * 
	 */
	public Point getDestination() {
		return destination;
	}

	/**
	 * Set function for the field 'destination'
	 * 
	 * @param destination - Point
	 * 
	 */
	public void setDestination(Point destination) {
		this.destination = destination;
	}

	/**
	 * Get function for the field 'totalTicksToArrive'
	 * 
	 * @return totalTicksToArrive - int
	 * 
	 */
	public int getTotalTicksToArrive() {
		return totalTicksToArrive;
	}

	/**
	 * Set function for the field 'totalTicksToArrive'
	 * 
	 * @param totalTicksToArrive - int
	 * 
	 */
	public void setTotalTicksToArrive(int totalTicksToArrive) {
		this.totalTicksToArrive = totalTicksToArrive;
	}

	/**
	 * Get function for the field 'ticksOver'
	 * 
	 * @return ticksOver - int
	 * 
	 */
	public int getTicksOver() {
		return ticksOver;
	}

	/**
	 * Set function for the field 'ticksOver'
	 * 
	 * @param ticksOver - int
	 * 
	 */
	public void setTicksOver(int ticksOver) {
		this.ticksOver = ticksOver;
	}

	/**
	 * Get function for the field 'startPoint'
	 * 
	 * @return startPoint - Point
	 * 
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * Set function for the field 'startPoint'
	 * 
	 * @param startPoint - Point
	 * 
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Helper function, uses Point class'es proportionalStep() function to calculate the next point the GUI object should appear on
	 * based on the previous location of the Truck element, and the increment it has completed since.
	 * <p>
	 * Once the calculation is done, updates the current location of the GUI element on the screen to match, as much as possible, 
	 * the actual location of the actual Truck element running in the background.
	 * 
	 * @see Point
	 */
	public void nextStep() {
		Point nextStep = Point.proportionalStep(this.getStartPoint(), this.getDestination(),
				this.getTotalTicksToArrive(), this.getTicksOver());
		this.setCurrentLocation(nextStep);
	}


	/**
	 * Get function for the field 'isHidden'
	 * 
	 * @return isHidden - boolean
	 * 
	 */
	public boolean isHidden() {
		return isHidden;
	}

	/**
	 * Set function for the field 'isHidden'
	 * 
	 * @param isHidden - boolean
	 * 
	 */
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 * Get function for the field 'truckId'
	 * 
	 * @return truckId - int
	 * 
	 */
	public int getTruckId() {
		return truckId;
	}


}
