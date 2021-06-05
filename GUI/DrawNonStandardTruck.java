package GUI;

import java.awt.Color;

import components.NonStandardTruck;
import components.Package;

/**
 * This class represents a NonStandardTruck object displayed on the GUI for the user.
 * <p>
 * An instance of this class contains several attributes that describe the location & manner of the object. Those dictate the way it appears on the GUI
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DrawObject 
 *
 */
public class DrawNonStandardTruck extends DrawTruck {

	public static final Color collecting = Color.pink;
	public static final Color delivering = new Color(255, 0, 0);
	public static final Color pathColor = Color.red;
	public static final int hubId = -1;

	/**
	 * Constructor function for the class.
	 * 
	 */
	public DrawNonStandardTruck() {
		super();
		this.setColor(Color.PINK);
	}
	/**
	 * Copy constructor for the class.
	 * Shallow copy because copying immutable fields.
	 */
	public DrawNonStandardTruck(DrawNonStandardTruck other) {
		super((DrawTruck)other);
		this.setColor(other.getColor());
	}
	
	/**This function updates the current object to fit the parameters of the component it displays.
	 * <p>
	 * Every class instance represents a back-end component object that, as part of the system simulation, changes properties such as location, direction or even capacity.
	 * As such, the GUI object must represent that on screen, and this function updates the GUI objects parameters to match what is transpiring in the back-end.
	 * @param obj - The component this class object represents on the GUI.
	 */
	@Override
	public void update(Observable obj) throws Exception {
		if (!(obj instanceof NonStandardTruck))
			throw new Exception("Invalid object received!");
		NonStandardTruck truck = ((NonStandardTruck) obj);

		super.setHidden(truck.isAvailable()); // Truck finished work, reset location.
		if (truck.getPackages().isEmpty()) {
			super.setDestination(Point.defaultPoint);
			return;
		}
		Package p = truck.getPackages().get(0);
		if (getDestination().equals(Point.defaultPoint) || getCurrentLocation().equals(getDestination())) {  // Truck has either not left yet, or just picked up the package.
			
			// Set up next part of the delivery.
			setTotalTicksToArrive(truck.getTimeLeft());
			setTicksOver(0);
			initPath(p);
		}

		else { // Truck is on the way
			nextStep();
			setTicksOver(getTicksOver() + 1);
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
			this.setColor(collecting);
			setStartPoint(DisplayPanel.getDrawBranchById(hubId).getUpperGate());
			setDestination(p.getGuiListener().getSenderPoint());
			synchronized (DisplayPanel.getPaths()) { // Synchronized in case more than one object attempts to add a path.
				DisplayPanel.addPath(new DrawPath(getStartPoint(), getDestination(), pathColor));
			}
			this.setCurrentLocation(getStartPoint());
			break;

		case DISTRIBUTION:  // Package has been picked up and needs to get to the destination.

			// Set attributes for the trip
			this.setColor(delivering);
			setStartPoint(p.getGuiListener().getSenderPoint());
			setDestination(p.getGuiListener().getReceiverPoint());
			synchronized (DisplayPanel.getPaths()) { // Synchronized in case more than one object attempts to add a path.
				DisplayPanel.addPath(new DrawPath(getStartPoint(), getDestination(), pathColor));
			}
			this.setCurrentLocation(getStartPoint());
			break;

		default: // Illegal status.
			throw new Exception("Invalid package status received!");
		}
	}

}
