package components;



/**
 * This class represents a NonStandardTruck in the delivery system.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 * @see Truck
 *
 */
public class NonStandardTruck extends Truck {

	private final int width, length, height;

	/**
	 * Constructor for the class NonStandardTruck.
	 * <p>
	 * We use default values for the measurements of the truck, since none were
	 * provided in this constructor.
	 * 
	 */
	public NonStandardTruck() {
		super();
		// default values
		width = 400;
		length = 1100;
		height = 350;
		System.out.println("Creating " + this);
	}

	/**Copy constructor for the class
	 * 
	 * @param other - Truck object to copy fields from
	 */
	public NonStandardTruck(Truck other) {
		super(other);
		// default values
		width = 400;
		length = 1100;
		height = 350;
		System.out.println("Copying " + this);
	}
	/**
	 * Constructor for the class NonStandardPackage.
	 * <p>
	 * Parameters:
	 * <p>
	 * licensePlate, truckModel - Parameters to create the superclass object within
	 * this class.
	 * <p>
	 * width, length, height - Specifications for the size of the truck.
	 * 
	 */
	public NonStandardTruck(String licensePlate, String truckModel, int length, int width, int height) {
		super(licensePlate, truckModel);
		this.length = (length > 0) ? length : 1100;
		this.width = (width > 0) ? width : 400;
		this.height = (height > 0) ? height : 350;
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return "NonStandardTruck [" + super.toString() + ", length=" + length + ", width=" + width + ", height="
				+ height + "]";
	}

	/**
	 * Get function for the field 'width'
	 * 
	 * @return width - Integer
	 * 
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get function for the field 'length'
	 * 
	 * @return length - Integer
	 * 
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Get function for the field 'height'
	 * 
	 * @return height - Integer
	 * 
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Implementation of the 'getName' function in the Node interface
	 * 
	 * @see Node
	 * @return truck name- String.
	 * 
	 */
	public String getNodeName() {
		return "NonStandardTruck " + this.getTruckID();
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
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof NonStandardTruck))
			return false;

		return super.equals(other);
	}

	/**
	 * This function imitates a unit of work during a clock pulse.
	 * <p>
	 * A truck that is available goes into wait state until branch will notify that there is a package to deliver/collect
	 * When truck collected the package from a sender it changes it's status to distribution by using deliverPackage method.
	 * When truck arrived at its destination (customer) it must unload the package (NonStandardPackage).
	 * When unit of work is done this function also using alert function to update the GUI state represented in DrawNonStandardTruck class
	 * @see NonStandardPackage
	 * @see DrawNonStandardTruck
	 */
	public void work() {
		synchronized(this) {
			if (isAvailable())
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("Thread:" + Thread.currentThread().getId() + " Is finished work!");
				}
			}
		if (getTimeLeft() == 0) {
			if (!(super.packages.isEmpty())) {
				deliverPackage(super.packages.get(0));
				if (super.packages.get(0).getStatus().equals(Status.DELIVERED)) {
					super.packages.remove(0);
					setAvailable(true);
				}
			} else {
				System.out.println("Logical Error - There is no package associated with NonStandardTruck: "
						+ super.getTruckID() + " , so truck cannot be busy in thread : " + Thread.currentThread().getId());
			}
		}
		alert();
		reduceTimeLeft();
	}

	/**
	 * This function collects a package and adds it to the local list of packages.
	 * <p>
	 * A truck that has collected a package must update the tracking, and set itself
	 * as unavailable as it sets off to its destination.
	 * <p>
	 * This function prints a message if an error occurred.
	 * 
	 * @param p - A package to add to the current list of packages.
	 * @see Package
	 */
	@Override
	public void collectPackage(Package p) {
		if (p.getStatus().equals(Status.CREATION)) {
			this.packages.add(p);
			setAvailable(false);
			System.out.println("NonStandardTruck " + super.getTruckID() + " is collecting package " + p.getPackageID()
					+ " ,time to arrive: " + this.getTimeLeft());
			p.setStatus(Status.COLLECTION);
			p.addTracking(this, Status.COLLECTION);
			alert();
		} else {
			System.out.println("Error - NonStandardTruck cannot collect package with status: " + p.getStatus());
		}

	}

	/**
	 * This function delivers a package to a destination (branch/hub), and removes
	 * it from the local list of packages in the truck.
	 * 
	 * <p>
	 * The truck also updates the tracking on the package itself, and in case the
	 * truck must make a journey it calculates a new arrival time.
	 * <p>
	 * This function prints a message if an error occurred.
	 * 
	 * @param p - A package to remove from the current list of packages.
	 * @see Package
	 */
	@Override
	public void deliverPackage(Package p) {
		if (p.getStatus().equals(Status.COLLECTION)) {
			p.setStatus(Status.DISTRIBUTION);
			System.out.println("NonStandardTruck " + super.getTruckID() + " has collected package " + p.getPackageID());
			p.addTracking(this, Status.DISTRIBUTION);
			super.setTimeLeft(
					(Math.abs(p.getSenderAddress().getStreet() - p.getDestinationAddress().getStreet()) / 10) % 10 + 1);
			System.out.println("NonStandardTruck " + super.getTruckID() + " is delivering package " + p.getPackageID()
					+ " ,time to arrive: " + this.getTimeLeft());
		} else if (p.getStatus().equals(Status.DISTRIBUTION)) {
			p.setStatus(Status.DELIVERED);
			System.out.println("NonStandardTruck " + super.getTruckID() + " has delivered package " + p.getPackageID()
					+ " to the destination");
			p.addTracking(null, Status.DELIVERED);
		} else {
			System.out.println("Error - NonStandardTruck cannot deliver package with status: " + p.getStatus());
		}

	}



}
