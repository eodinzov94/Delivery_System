package GUI;

import java.awt.Color;
import components.Package;
import java.awt.Graphics;
import components.NonStandardPackage;


/**
 * This class represents a Package object displayed on the GUI for the user.
 * <p>
 * An instance of this class contains several attributes that describe the location & manner of the object. Those dictate the way it appears on the GUI
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DrawObject 
 *
 */
public class DrawPackage extends DrawObject {
	public static int numOfPackages = 0;
	private final int packageID;
	static private int deltaY = 850;
	static private int diameter = 25;
	static Color hasPackage = new Color(204,0,0);
	static Color noPackage = new Color(255,153,153);
	private Color upperColor;
	private Color lowerColor;
	boolean isHidden;


	/**
	 * Constructor function for the class.
	 * 
	 * @param loc - Point of origin to start drawing the object from.
	 */
	public DrawPackage(Point loc) {
		super(loc);
		upperColor = hasPackage;
		lowerColor = noPackage;
		packageID = 1000 + numOfPackages++;
		isHidden = true;
	}
	/**
	 * Constructor function for the class.
	 * 
	 * @param loc - Point of origin to start drawing the object from.
	 * @param pathToHub - DrawPath object to keep a path to the central hub.
	 * @param branchId - ID for this object.
	 */
	public DrawPackage(DrawPackage other) {
		super((DrawObject)other);
		upperColor = other.upperColor;
		lowerColor = other.lowerColor;
		packageID = other.packageID;
		isHidden = other.isHidden;
	}

	/**This function updates the current object to fit the parameters of the component it displays.
	 * <p>
	 * Every class instance represents a back-end component object that, as part of the system simulation, changes properties such as location, direction or even capacity.
	 * As such, the GUI object must represent that on screen, and this function updates the GUI objects parameters to match what is transpiring in the back-end.
	 * @param obj - The component this class object represents on the GUI.
	 */
	@Override
	public void update(Observable obj) throws Exception {
		// TODO Auto-generated method stub
		if(!(obj instanceof Package)) throw new Exception("Invalid object received!");
		Package temp = (Package)obj;
		switch (temp.getStatus()) {
			
		case COLLECTION: // The package is being collected, update colors
			if(temp instanceof NonStandardPackage)
				upperColor = hasPackage;
			else
				upperColor= noPackage;
			lowerColor= noPackage;
			break;

		case DELIVERED: // The package is being delivered, update colors
			lowerColor = hasPackage;
			upperColor= noPackage;
			break;
			
		default: // The package is idle.
			lowerColor = noPackage;
			upperColor= noPackage;
		}
			
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
	public void paintComponent(Graphics g){
		if(isHidden)
			return;
		int x = this.getCurrentLocation().getX();
		int y = this.getCurrentLocation().getY();
		g.setColor(upperColor);
		g.fillOval(x,y,diameter,diameter);
		g.setColor(lowerColor);
		g.fillOval(x,y + deltaY,diameter,diameter);

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
	 * Get function for the 'SenderPoint' (Package sender)
	 * 
	 * @return SenderPoint - new Point object based on formula.
	 * 
	 */
	public Point getSenderPoint() {
		int x = this.getCurrentLocation().getX();
		int y = this.getCurrentLocation().getY();
		return new Point (x+diameter/2,y+diameter);
	}
	/**
	 * Get function for the 'ReceiverPoint' (Package recipient)
	 * 
	 * @return ReceiverPoint - new Point object based on formula.
	 * 
	 */
	public Point getReceiverPoint() {
		int x = this.getCurrentLocation().getX();
		int y = this.getCurrentLocation().getY();
		return new Point (x+diameter/2,y +deltaY);
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
	 * Get function for the field 'packageID'
	 * 
	 * @return packageID - int
	 * 
	 */
	public int getPackageID() {
		return packageID;
	}

	/**
	 * Get function for the field 'upperColor'
	 * 
	 * @return upperColor - Color
	 * 
	 */
	public Color getUpperColor() {
		return upperColor;
	}

	/**
	 * Set function for the field 'upperColor'
	 * 
	 * @param upperColor - Color
	 * 
	 */
	public void setUpperColor(Color upperColor) {
		this.upperColor = upperColor;
	}

	/**
	 * Get function for the field 'lowerColor'
	 * 
	 * @return lowerColor - Color
	 * 
	 */
	public Color getLowerColor() {
		return lowerColor;
	}

	/**
	 * Set function for the field 'lowerColor'
	 * 
	 * @param lowerColor - Color
	 * 
	 */
	public void setLowerColor(Color lowerColor) {
		this.lowerColor = lowerColor;
	}

}