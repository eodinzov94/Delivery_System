package GUI;

import java.awt.Color;
import components.Package;

import components.Van;

/**
 * This class represents a Van object displayed on the GUI for the user.
 * <p>
 * An instance of this class contains several attributes that describe the location & manner of the object. Those dictate the way it appears on the GUI
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.20211
 * @see DrawObject 
 *
 */
public class DrawVan extends DrawTruck {
	
	static private Color defaultColor = Color.blue;
	
	/**
	 * Constructor function for the class.
	 * 
	 */
	public DrawVan() {
		super();
		this.setColor(defaultColor);
	}
	
	/**
	 * Copy constructor for the class.
	 * Shallow copy because copying immutable fields.
	 */
	public DrawVan(DrawVan other) {
		super((DrawTruck)other);
		this.setColor(defaultColor);
	}

	/**This function updates the current object to fit the parameters of the component it displays.
	 * <p>
	 * Every class instance represents a back-end component object that, as part of the system simulation, changes properties such as location, direction or even capacity.
	 * As such, the GUI object must represent that on screen, and this function updates the GUI objects parameters to match what is transpiring in the back-end.
	 * @param obj - The component this class object represents on the GUI.
	 */
	@Override
	public void update(Observable obj) throws Exception {
		if(!(obj instanceof Van))
			throw new Exception("Invalid object received!");
		Van truck = ((Van)obj);
		super.setHidden(truck.isAvailable());
		if (truck.getPackages().isEmpty()) { // Van has finished work, reset location.
			super.setDestination(Point.defaultPoint);
			return;
		}
		Package p = truck.getPackages().get(0);
		if (getDestination().equals(Point.defaultPoint)) { // Truck has not left yet, use initPath() to initiate a path to the package it needs to pick up.
			
			// Set up the delivery.
			setTotalTicksToArrive(truck.getTimeLeft());
			setTicksOver(0);
			initPath(p);
		}
		
		else { // Van is on the way
			nextStep();
			setTicksOver(getTicksOver()+1);
		}
	}
		
	/**This function receives a package and creates a path for the current truck based on the current status of the package.
	 * <p>
	 * This function is called several times for different trucks for the same package, once for every time it has to be moved to a new destination.
	 * 
	 * @param p - Package the truck is handling
	 * @throws Exception
	 */
	public void initPath(Package p) throws Exception {
		switch (p.getStatus()) {
		case COLLECTION: // Package has just been created and needs to be picked up
			
			// Set attributes for the trip
			setDestination(DisplayPanel.getDrawBranchById(p.getSenderAddress().getZip()).getUpperGate());
			setStartPoint(p.getGuiListener().getSenderPoint());
			this.setCurrentLocation(getStartPoint());
			synchronized (DisplayPanel.getPaths()) { // Synchronized in case more than one object attempts to add a path.
				DisplayPanel.addPath(new DrawPath(this.getStartPoint(), this.getDestination(), defaultColor));
			}
			break;

		case DISTRIBUTION: // Package needs to be delivered to the recipient.

			// Set attributes for the trip
			setStartPoint(DisplayPanel.getDrawBranchById(p.getDestinationAddress().getZip()).getLowerGate());
			setDestination(p.getGuiListener().getReceiverPoint());
			synchronized (DisplayPanel.getPaths()) { // Synchronized in case more than one object attempts to add a path.
				DisplayPanel.addPath(new DrawPath(this.getStartPoint(), this.getDestination(), defaultColor));
			}
			this.setCurrentLocation(this.getStartPoint());
			break;

		default:  // Illegal status.
			throw new Exception("Invalid package status received! Status received:" + p.getStatus());
		}
	}


}
