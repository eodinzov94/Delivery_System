package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * This class represents a Path between 2 objects that are displayed on the GUI
 * for the user.
 * <p>
 * An instance of this class contains several attributes that describe the
 * location & manner of the object. Those dictate the way it appears on the GUI
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DrawObject
 *
 */
public class DrawPath extends DrawObject {
	Point start;
	Point end;
	boolean isHidden;
	/**
	 * Constructor function for the class.
	 * 
	 * @param start - Point object representing the start of the path.
	 * @param start - Point object representing the end of the path.
	 * @param c     - Color object representing the color of which to paint the
	 *              path.
	 */
	public DrawPath(Point start, Point end, Color c) {
		super(start);
		this.start = start;
		this.end = end;
		this.setColor(c);
		isHidden = false;
	}

	/**
	 * Get function for the field 'start'
	 * 
	 * @return start - Point
	 * 
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * Set function for the field 'start'
	 * 
	 * @param start - Point
	 * 
	 */
	public void setStart(Point start) {
		this.start = start;
	}

	/**
	 * Get function for the field 'end'
	 * 
	 * @return end - Point
	 * 
	 */
	public Point getEnd() {
		return end;
	}

	/**
	 * Set function for the field 'end'
	 * 
	 * @param end - Point
	 * 
	 */
	public void setEnd(Point end) {
		this.end = end;
	}

	/**
	 * This function updates the current object to fit the parameters of the
	 * component it displays.
	 * <p>
	 * Every class instance represents a back-end component object that, as part of
	 * the system simulation, changes properties such as location, direction or even
	 * capacity. As such, the GUI object must represent that on screen, and this
	 * function updates the GUI objects parameters to match what is transpiring in
	 * the back-end.
	 * 
	 * @param obj - The component this class object represents on the GUI.
	 */
	@Override
	public void update(Observable obj) {
		// path should never update!
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
		if(isHidden)
			return;
		Graphics2D gr = (Graphics2D)g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setColor(this.getColor());
		gr.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
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
	 * Setter for field  isHidden - boolean
	 * 
	 */
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	
}
