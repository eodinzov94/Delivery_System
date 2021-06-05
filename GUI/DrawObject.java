package GUI;
import java.awt.Color;
import java.awt.Graphics;


/**
 * This class is an abstract representation of every Drawable element in the system.
 * <p>
 * It contains several functions and attributes that are shared among the various Drawable objects we have.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 *
 */
public abstract class DrawObject implements Listener {

	public static Color defaultObjectColor = Color.BLACK;
	private Point currentLocation;
	private Color c;
	
	/**
	 * Constructor function for the class.
	 * 
	 * @param loc - Point of origin to start drawing the object from.
	 */
	public DrawObject(Point loc) {
		currentLocation = loc;
		this.c = defaultObjectColor;
	}
	/**
	 * Copy constructor for the class.
	 * Shallow copy because copying immutable fields.
	 */
	public DrawObject(DrawObject other) {
		currentLocation = other.getCurrentLocation();
		this.c = other.c;
	}
	
	/**
	 * Get function for the field 'currentLocation'
	 * 
	 * @return currentLocation - Point
	 * 
	 */
	public Point getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * Set function for the field 'currentLocation'
	 * 
	 * @param location - Point
	 * 
	 */
	public void setCurrentLocation(Point location) {
		this.currentLocation = location;
	}
	/**
 * Get function for the field 'c' (color)
 * 
 * @return c - Color
 * 
 */
	public Color getColor() {
		return c;
	}

	/**
	 * Set function for the field 'c' (color)
	 * 
	 * @param c - Color
	 * 
	 */
	public void setColor(Color c) {
		this.c = c;
	}
	/**
	 * Overrides paintComponent as used by Swing components, with custom logic to fit this class.
	 * <p>
	 * This function is inherited and implemented by subclasses to fit their custom GUI appearance.
	 * 
	 * @param g - Graphics object
	 */
	public abstract void paintComponent(Graphics g);
}
