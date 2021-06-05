package GUI;

import java.awt.Color;
import java.awt.Graphics;

import components.Branch;


/**
 * This class represents a Branch object displayed on the GUI for the user.
 * <p>
 * An instance of this class contains several attributes that describe the location & manner of the object. Those dictate the way it appears on the GUI
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DrawObject 
 *
 */
public class DrawBranch extends DrawObject {

	private final DrawPath pathToHub;
	private final int branchId;
	boolean isHidden;
	static final Color hasPackage  = Color.blue;
	static final Color noPackage  = Color.cyan;
	static private final int width = 40;
	static private final int height = 30;
	
	/**
	 * Constructor function for the class.
	 * 
	 * @param loc - Point of origin to start drawing the object from.
	 * @param pathToHub - DrawPath object to keep a path to the central hub.
	 * @param branchId - ID for this object.
	 */
	public DrawBranch(Point loc,DrawPath pathToHub,int branchId) {
		super(loc);
		this.pathToHub = pathToHub;
		this.setColor(noPackage);
		this.branchId = branchId;
		isHidden = false;
	}
	
	/**
	 * Copy constructor for the class.
	 * Shallow copy because copying immutable fields.
	 */
	public DrawBranch(DrawBranch other) {
		super((DrawObject)other);
		
		this.pathToHub = other.pathToHub;
		this.setColor(other.getColor());
		this.branchId = other.branchId;
		this.isHidden = other.isHidden;
	}
	

	/**This function updates the current object to fit the parameters of the component it displays.
	 * <p>
	 * Every class instance represents a back-end component object that, as part of the system simulation, changes properties such as location, direction or even capacity.
	 * As such, the GUI object must represent that on screen, and this function updates the GUI objects parameters to match what is transpiring in the back-end.
	 * @param obj - The component this class object represents on the GUI.
	 */
	@Override
	public void update(Observable obj) throws Exception {
		if(!(obj instanceof Branch)) throw new Exception("Invalid object received!");
		Branch temp = (Branch)obj;
		if (temp.storesPackages())
			this.setColor(hasPackage);
		else this.setColor(noPackage);
	}
	
		/**
	 * Overrides paintComponent as used by Swing components, with custom logic to fit this class.
	 * <p>
	 * This function is later used by the DisplayPanel to paint this class instance on the panel.
	 * 
	 * @param g - Graphics object
	 */
	public void paintComponent(Graphics g){
		if(isHidden)
			return;
		int x = this.getCurrentLocation().getX();
		int y = this.getCurrentLocation().getY();
		g.setColor(this.getColor());
		g.fillRect(x, y, width, height);
		if(pathToHub != null)
			pathToHub.paintComponent(g);
	}
	
		/**
	 * Get function to receive the Upper Gate of the current branch.
	 * <p>
	 * The upper branch faces the top-positioned packages, that represent the 'senders' of packages.
	 * 
	 * @return A new Point representing the upper gate
	 * 
	 */
	public Point getUpperGate() {
		int x = this.getCurrentLocation().getX();
		int y = this.getCurrentLocation().getY();
		return new Point(x+20, y);
	}
	
		/**
	 * Get function to receive the Lower Gate of the current branch.
	 * <p>
	 * The upper branch faces the bottom-positioned packages, that represent the 'receivers' of packages.
	 * 
	 * @return A new Point representing the lower gate
	 * 
	 */
	public Point getLowerGate() {
		int x = this.getCurrentLocation().getX();
		int y = this.getCurrentLocation().getY();
		return new Point(x+20, y+30);
	}
	
		
	/**
	 * Get function to receive the path to the hub from the current branch.
	 * <p>
	 * Every class instance keeps a DrawPath object that contains the connection between the class instance and the central hub.
	 * 
	 * @return pathToHub - DrawPath
	 * 
	 */
	public DrawPath getPathToHub() {
		return pathToHub;
	}
	
		/**
	 * Get function for the field 'branchId'
	 * 
	 * @return branchId - int
	 * 
	 */
	public int getBranchId() {
		return branchId;
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
