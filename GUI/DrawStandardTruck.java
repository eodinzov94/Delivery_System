package GUI;

import java.awt.Color;
import java.awt.Graphics;
import components.StandardTruck;

/**
 * This class represents a StandardPackage object displayed on the GUI for the user.
 * <p>
 * An instance of this class contains several attributes that describe the location & manner of the object. Those dictate the way it appears on the GUI
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DrawObject 
 *
 */
public class DrawStandardTruck extends DrawTruck {
	private Integer numPackages;
	public static final Color hasPackages = new Color(0, 102, 0);
	public static final Color noPackages = new Color(102, 255, 102);
	public static final int noDestination = -2;
	public static final int destinationHub = -1;
	private int destinationId;

	/**
	 * Constructor function for the class.
	 * 
	 */
	public DrawStandardTruck() {
		super();
		numPackages = 0;
		this.setColor(Color.orange);
		this.setCurrentLocation(DrawHub.hubLocation);
		destinationId = noDestination;
	}
	/**
	 * Copy constructor for the class.
	 * Shallow copy because copying immutable fields.
	 */
	public DrawStandardTruck(DrawStandardTruck other) {
		super((DrawTruck)other);
		numPackages = other.getNumPackages();
		this.setColor(other.getColor());
		this.setCurrentLocation(other.getCurrentLocation());
		destinationId = other.getDestinationId();
	}
	/**
	 * Get function for the field 'numPackages'
	 * 
	 * @return numPackages - int
	 * 
	 */
	public int getNumPackages() {
		return numPackages;
	}

	/**
	 * Set function for the field 'numPackages'
	 * 
	 * @param numPackages - int
	 * 
	 */
	public void setNumPackages(int numPackages) {
		this.numPackages = numPackages;
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
		int x = this.getCurrentLocation().getX() - 8;
		int y = this.getCurrentLocation().getY() - 8;
		g.fillRect(x, y, bodySize, bodySize);
		g.setColor(wheelColor);
		g.fillOval(x - 4, y - 4, wheelDiameter, wheelDiameter);
		g.fillOval(x + 8, y - 4, wheelDiameter, wheelDiameter);
		g.fillOval(x + 8, y + 8, wheelDiameter, wheelDiameter);
		g.fillOval(x - 4, y + 8, wheelDiameter, wheelDiameter);
		if (numPackages > 0)
			g.drawString(numPackages.toString(), x + 4, y - 5);
	}

	/**
	 * Helper function to set the current color of the GUI object based on the properties of the StandardTruck instance it represents.
	 * <p>
	 * The color depends on whether or not the StandardTruck has any packages loaded into it.
	 * 
	 */
	private void setCurrentColor() {
		if (numPackages > 0)
			this.setColor(hasPackages);
		else
			this.setColor(noPackages);
	}
	
	/**This function resets the trucks' parameters and position to default values.
	 * <p>
	 * The function is separate from the update() logic because it cannot rely on the truck 'running' a journey, 
	 * and so it is called explicitly and separately than update() at the end of a StandardTrucks' journey.
	 * 
	 */
	public void reset() {
		this.setTotalTicksToArrive(0);
		setDestinationId(noDestination);
		setCurrentLocation(DrawHub.hubLocation);
		setDestination(Point.defaultPoint);
		setStartPoint(getCurrentLocation());
		this.setTicksOver(0);
		setHidden(true);
	}
	
	/**This function updates the current object to fit the parameters of the component it displays.
	 * <p>
	 * Every class instance represents a back-end component object that, as part of the system simulation, changes properties such as location, direction or even capacity.
	 * As such, the GUI object must represent that on screen, and this function updates the GUI objects parameters to match what is transpiring in the back-end.
	 * @param obj - The component this class object represents on the GUI.
	 */
	@Override
	public void update(Observable obj) throws Exception {
		if (!(obj instanceof StandardTruck))
			throw new Exception("Invalid object received!");
		StandardTruck truck = ((StandardTruck) obj);

		super.setHidden(false);
		numPackages = truck.getPackages().size();
		setCurrentColor();
		
		// The following code divides into 3 out of 4 stages in a standard journey: leaving the hub, driving between the destinations, and arriving at a branch.
		// The last stage- arriving at the Hub, is handled in the reset() function.
		
		if (getCurrentLocation().equals(getDestination())) { // Truck has arrived at the branch, update parameters and begin journey back to hub
				this.setStartPoint(this.getCurrentLocation());
				this.setDestination(DisplayPanel.getDrawBranchById(destinationId).getPathToHub().end);
				this.setDestinationId(destinationHub);
				this.setTotalTicksToArrive(truck.getTimeLeft());
				this.setTicksOver(0);
			}
		else { 
			if(getDestination().equals(Point.defaultPoint)) { // Truck has started journey to a branch from the Hub,
				DrawPath p = truck.getDestination().getGuiListener().getPathToHub();
				this.setTotalTicksToArrive(truck.getTimeLeft());
				this.setTicksOver(0);
				this.setDestination(p.start);
				this.setStartPoint(p.end);
				this.setCurrentLocation(p.end);
				this.setDestinationId(truck.getDestination().getBranchId());
			}
			else { // Truck is still en-route and hasn't arrived anywhere yet.
			this.nextStep();
			this.setTicksOver(this.getTicksOver() + 1);
		}
		}
	}
	
	/**
	 * Get function for the field 'destinationId'
	 * 
	 * @return destinationId - int
	 * 
	 */
	public int getDestinationId() {
		return destinationId;
	}

	/**
	 * Set function for the field 'destinationId'
	 * 
	 * @param destinationId - int
	 * 
	 */
	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}

}
