package GUI;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class represents the Hub object displayed on the GUI for the user.
 * <p>
 * An instance of this class contains several attributes that describe the location & manner of the object. Those dictate the way it appears on the GUI
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 2.0 -- 26.4.2021
 * @see DrawObject 
 *
 */
public class DrawHub extends DrawBranch {

	static private Color hubColor = new Color(2, 101, 2);
	static private int x = 1800;
	static private int y = 300;
	static private int width = 40;
	static private int height = 200;
	static public final Point hubLocation = new Point(x,y);
	
	/**
	 * Get function for the static field 'hubColor'
	 * 
	 * @return hubColor - Color
	 * 
	 */
		public static Color getHubColor() {
		return hubColor;
	}
	
		/**
	 * Constructor function for the class.
	 * 
	 * @param branchId - ID for this object.
	 */
	public DrawHub(int branchId) {
		super(new Point(x, y), null,branchId);
		this.setColor(hubColor);
	}

	/**
	 * Override 'update' from super class, as the DrawHub object is never altered it never needs to update.
	 * @see DrawBranch
	 */
	@Override
	public void update(Observable obj) {
		return;

	}
		/**
	 * Overrides paintComponent as used by Swing components, with custom logic to fit this class.
	 * <p>
	 * This function is later used by the DisplayPanel to paint this class instance on the panel.
	 * 
	 * @param g - Graphics object
	 */
	public void paintComponent(Graphics g){
		int x = this.getCurrentLocation().getX();
		int y = this.getCurrentLocation().getY();
		g.setColor(this.getColor());
		g.fillRect(x, y, width, height);
	}
	
		/**
	 * Get function to receive the Upper Gate of the Hub.
	 * <p>
	 * The Hub has an upper gate from which NonStandardTrucks start their path towards NonStandardPackages
	 * 
	 * @return A new Point representing the upper gate
	 * 
	 */
	public Point getUpperGate() {
		return new Point(x+width/2,y);
	}
	
		/**Overrides getPathToHub() from the super class 'DrawBranch'.
	 * <p>
	 * Throws an exception because there's no path from the Hub to itself.
	 * 
	 */
	public DrawPath getPathToHub() {
		throw new UnsupportedOperationException("No such path exists");
	}
}
