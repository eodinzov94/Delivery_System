package components;

import java.util.ArrayList;

/**
 * This abstract class represents a Van (type of Truck) in the delivery system.
 * <p>
 * 
 * @see Node
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 *
 *
 */
public class Van extends Truck {
	/**
	 * Default Constructor for the class Van that uses super Constructor.
	 * 
	 * @see Truck
	 * 
	 */
	public Van() {
		super();
		System.out.println("Creating " + this);
	}

	public Van(Truck other) {
		super(other);
		System.out.println("Copying " + this);
	}

	/**
	 * Constructor for the class Van that uses super Constructor.
	 * 
	 * @see Truck
	 * @param licensePlate - String representing license plate
	 * @param truckModel   - String representing truck model
	 */
	public Van(String licensePlate, String truckModel) {
		super(licensePlate, truckModel);
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return "Van [" + super.toString() + "]";
	}

	/**
	 * Implementation of the 'getName' function in the Node interface
	 * 
	 * @see Node
	 * @return name + id - String.
	 * 
	 */
	public String getNodeName() {
		return "Van " + this.getTruckID();
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

		if (!(other instanceof Van))
			return false;

		return super.equals(other);
	}

	/**
	 * This function imitates a unit of Van work during a clock pulse.
	 * <p>
	 * The function checks if this truck is available, if yes - changes current
	 * Thread state into wait. the truck's thread will in waiting state until gets
	 * notified by a branch that there is a new package collect/delivery mission, if
	 * truck is busy - means he is working and this function representing the next
	 * tick of work. if TimeLeft = 0 -> truck arrived to the branch, delivering
	 * package by using deliverPackage, set available true - because mission is
	 * complete! at the end notifies guiListener (DrawTruck object that representing
	 * truck in a GUI ) by using alert() method then, reduces time left by 1 by
	 * using reduceTimeLeft() method.
	 * 
	 * 
	 */
	public void work() {
		synchronized (this) {
			if (isAvailable())
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("Thread:" + Thread.currentThread().getId() + " Is finished work!");
					return;
				}
		}
		if (getTimeLeft() == 0) {
			if (!(super.packages.isEmpty())) {
				deliverPackage(super.packages.get(0));
				setAvailable(true);
			} else {
				System.out.println("Logical Error - There is no package associated with Van: " + super.getTruckID()
						+ " , so truck cannot be busy in thread : " + Thread.currentThread().getId());
			}
		}
		alert();
		reduceTimeLeft();

	}

	/**
	 * This function imitates package collect by Van truck.
	 * <p>
	 * The function checks the package status,setting available to false - because
	 * Van is busy now then adding the package to the local list, changing the
	 * status to the next appropriate one, adding new Tracking instance to the list.
	 * 
	 * @param p - Package
	 */
	@Override
	public void collectPackage(Package p) {
		if (p.getStatus().equals(Status.CREATION)) {
			if (!this.packages.add(p))
				System.out.println("Problem adding package " + p.toString());
			setAvailable(false);//
			System.out.println("Van " + super.getTruckID() + " is collecting package " + p.getPackageID()
					+ " ,time to arrive: " + this.getTimeLeft());
			p.setStatus(Status.COLLECTION);
			p.addTracking(this, Status.COLLECTION);
		} else if (p.getStatus().equals(Status.DELIVERY)) {
			setAvailable(false);//
			this.packages.add(p);
			System.out.println("Van " + super.getTruckID() + " is delivering package " + p.getPackageID()
					+ " ,time left: " + this.getTimeLeft());
			p.setStatus(Status.DISTRIBUTION);
			p.addTracking(this, Status.DISTRIBUTION);
		} else {
			System.out.println("Error - Van cannot collect package with status: " + p.getStatus());
		}
	}

	/**
	 * This function imitates package deliver by Van truck.
	 * <p>
	 * The function checks the package status, changing the status to the next
	 * appropriate one, adds new Tracking,if the package is the small one and
	 * acknowledge = true, printing confirmation message at the end removing package
	 * from local list
	 * 
	 * @param p - Package
	 */
	@Override
	public void deliverPackage(Package p) {
		if (p.getStatus().equals(Status.COLLECTION)) {
			p.setStatus(Status.BRANCH_STORAGE);
			Branch origin = MainOffice.getInstance().getHub().findBranchById(p.getSenderAddress().getZip());
			p.addTracking(origin, Status.BRANCH_STORAGE);
			System.out.println("Van " + super.getTruckID() + " has collected package " + p.getPackageID()
					+ " ,and arrived back to branch " + p.getSenderAddress().getZip());
		} else if (p.getStatus().equals(Status.DISTRIBUTION)) {
			p.setStatus(Status.DELIVERED);
			p.addTracking(null, Status.DELIVERED);
			System.out
					.println(this.getNodeName() + " has delivered package " + p.getPackageID() + " to the destination");
			if (p instanceof SmallPackage && ((SmallPackage) p).isAcknowledge())
				System.out.println("Package " + p.getPackageID() + " delivery confirmed by customer");
		} else {
			System.out.println("Error - Van cannot deliver package with status: " + p.getStatus());
		}
		if (!(super.packages.remove(p)))
			System.out.println("ERROR! Problem removing package " + p.toString());
	}
	/**
	 * Overrides Clone method
	 * <p>
	 * Different then Copy Constructor, because it returns Van with differend Id's, 
	 * 
	 * @return Van object 
	 */
	protected Object clone() throws CloneNotSupportedException {
		Object obj = null;
		obj = super.clone();
		Van clone = (Van) obj;
		clone.setTruckModel("M" + MainOffice.getRand().nextInt(5));
		clone.setLicensePlate((100 + MainOffice.getRand().nextInt(900)) + "-" + (10 + MainOffice.getRand().nextInt(90))
				+ "-" + (100 + MainOffice.getRand().nextInt(900)));
		clone.setAvailable(true);
		clone.setTruckID(2000 + numTrucks);
		numTrucks++;
		clone.setPackages(new ArrayList<Package>());
		registerListener();
		return clone;
	}

}
