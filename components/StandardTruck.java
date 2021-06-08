package components;

import java.util.ArrayList;

import GUI.DrawStandardTruck;

/**
 * This class represents a Standard Truck in the delivery system.
 * <p>
 * 
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 * @see Node
 *
 */
public class StandardTruck extends Truck {

	private final int maxWeight;
	private int currentWeight;
	private Branch destination;

	/**
	 * Default Constructor for the class StandardTruck.
	 * 
	 * by default maxWeight is 100
	 *
	 * 
	 */
	public StandardTruck() {
		super();
		maxWeight = 100; // default value
		this.currentWeight = 0;
		destination = null;
		System.out.println("Creating " + this);
	}

	/**Copy constructor for the class
	 * 
	 * @param other - Truck object to copy fields from
	 */
	public StandardTruck(Truck other) {
		super(other);
		StandardTruck st= (StandardTruck)other;
		maxWeight = st.getMaxWeight();
		this.currentWeight = st.getCurrentWeight();
		destination = st.destination; 
		System.out.println("Copying " + this);
	}
	/**
	 * Constructor for the class StandardTruck.
	 * 
	 * @param licensePlate - String representing license plate
	 * @param truckModel   - String representing truck model
	 * @param maxWeight    - Integer representing maximum weight this truck can
	 *                     handle
	 * 
	 */
	public StandardTruck(String licensePlate, String truckModel, int maxWeight) {
		super(licensePlate, truckModel);
		this.maxWeight = (maxWeight > 0) ? maxWeight : 100;
		this.currentWeight = 0;
		destination = null;
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return "StandardTruck [" + super.toString() + " maxWeight=" + maxWeight + "]";
	}

	/**
	 * Get function for the field 'maxWeight'
	 * 
	 * @return maxWeight - int
	 * 
	 */
	public int getMaxWeight() {
		return maxWeight;
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

		if (!(other instanceof StandardTruck))
			return false;

		return super.equals(other);
	}

	/**
	 * Implementation of the 'getName' function in the Node interface
	 * 
	 * @see Node
	 * @return name + id - String.
	 * 
	 */
	public String getNodeName() {
		return "StandardTruck " + this.getTruckID();
	}

	/**
	 * Function that imitates truck unloading.
	 * <p>
	 * Function removes all the packages from local packages list (using a copy to
	 * prevent runtime modification exceptions) and delivers it to destination
	 * branch by calling the deliverPackage method of the truck.
	 * 
	 * 
	 */
	private void unloadPackages() {
		if (this.packages.isEmpty())
			return;
		ArrayList<Package> copyOfPackages = new ArrayList<Package>(this.packages);// prevents
																					// ConcurrentModificationException
		for (Package p : copyOfPackages) {
			this.deliverPackage(p);
		}
	}

	/**
	 * Function that imitates truck loading.
	 * <p>
	 * Function removes all packages with the status 'BRANCH_STORAGE' from the local
	 * branch package list (using a copy to prevent runtime modification
	 * exceptions), and adds them to the trucks' packages list by calling the
	 * trucks' collectPackage method.
	 * 
	 * 
	 */
	private void loadPackagesFromBranch() {
		ArrayList<Package> copyOfDestinationListPackages;
		synchronized(this.destination.getListPackages()) {
			copyOfDestinationListPackages = new ArrayList<Package>(this.destination.getListPackages());// prevents
		}																											// ConcurrentModificationException
		for (Package p : copyOfDestinationListPackages) {
			if (p.getStatus().equals(Status.BRANCH_STORAGE))
				this.collectPackage(p);
		}
	}

	/**
	 * This function imitates a unit of work for a StandardTruck during a clock
	 * pulse.
	 * <p>
	 * The function checks if this truck is available:
	 * <p>
	 * if yes - changes current Thread state into wait.
	 * the truck's thread will in waiting state until gets notified by a Hub that there is
	 * a new  mission
	 * <p>
	 * if truck is busy - it means he is working and this function advances the next
	 * 'tick' of work.
	 * <p>
	 * if TimeLeft = 0 -> truck arrived to the destination, if the destination is
	 * the hub - it unloads all packages, mission is done-> truck is available now.
	 * <p>
	 * if destination is the regular branch - unload all packages that belongs to
	 * this branch , load packages from this branch that are awaiting delivery to
	 * other sorting center, changing destination back to hub.
	 * <p>
	 * at the end
	 * notifies guiListener (DrawTruck object that representing truck in a GUI ) by using alert() method
	 * then, reduces time left by 1 by using reduceTimeLeft() method.
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
			if (this.destination instanceof Hub) {
				System.out.println("StandardTruck " + super.getTruckID() + " arrived to HUB");
				unloadPackages();
				System.out.println("StandardTruck " + super.getTruckID() + " unloaded packages at HUB");
				this.destination = null;
				this.setAvailable(true);
				((DrawStandardTruck)guiListener).reset(); //reset DrawStandardTruck
				return;
			} else {
				System.out.println("StandardTruck " + super.getTruckID() + " arrived to " + destination.getNodeName());
				unloadPackages();
				System.out.println(
						"StandardTruck " + super.getTruckID() + " unloaded packages at " + destination.getNodeName());
				loadPackagesFromBranch();
				System.out.println(
						"StandardTruck " + super.getTruckID() + " loaded packages at " + destination.getNodeName());
				this.destination = MainOffice.getInstance().getHub();
				super.setTimeLeft(MainOffice.getRand().nextInt(6) + 1);
				System.out.println("StandardTruck " + super.getTruckID()
						+ " is on it's way to the HUB, time to arrive: " + super.getTimeLeft());
			}
		}

		this.alert();
		reduceTimeLeft();
	}

	/**
	 * Get function for the field 'currentWeight'
	 * 
	 * @return currentWeight - int
	 * 
	 */
	public int getCurrentWeight() {
		return currentWeight;
	}

	/**
	 * Set function for the field 'currentWeight'
	 * 
	 * @param currentWeight - int
	 * 
	 */
	public void setCurrentWeight(int currentWeight) {
		this.currentWeight = currentWeight;
	}

	/**
	 * Get function for the field 'destination'
	 * 
	 * @return destination - Branch
	 * 
	 */
	public Branch getDestination() {
		return destination;
	}

	/**
	 * Set function for the field 'destination'
	 * 
	 * @param destination - Branch
	 * 
	 */
	public void setDestination(Branch destination) {
		this.destination = destination;
	}

	/**
	 * This function imitates the truck unloading for 1 specific package that is
	 * received as a parameter.
	 * <p>
	 * The function checks the package status, changing the status to the next
	 * appropriate one. it also adds new Tracking, and adds the package to the
	 * destination packages list by calling the collectPackage method implemented by
	 * the destination instance class, then removes the package from local list.
	 * 
	 * @param p - Package
	 */
	@Override
	public void deliverPackage(Package p) {
		if (p.getStatus().equals(Status.HUB_TRANSPORT)) {
			p.addTracking(destination, Status.HUB_STORAGE);
			p.setStatus(Status.HUB_STORAGE);
		} else if (p.getStatus().equals(Status.BRANCH_TRANSPORT)) {
			p.addTracking(destination, Status.DELIVERY);
			p.setStatus(Status.DELIVERY);
		} else
			System.out.println("Package status invalid -> deliverPackage@StandardTruck");
		destination.collectPackage(p);
		if (!(super.packages.remove(p)))
			System.out.println("ERROR! Problem removing package " + p.toString());
	}

	/**
	 * This function imitates the truck loading for 1 specific package that is
	 * received as parameter.
	 * <p>
	 * The function checks the package's weight;
	 * <p>
	 * if truck can fit another package it is added to the truck- it changes the
	 * package status and adds new Tracking, them removing it from destination list
	 * by using deliverPackage implemented by the destination instance class
	 * 
	 * @param p - Package
	 */
	@Override
	public void collectPackage(Package p) {

		// weight check
		if (p instanceof StandardPackage && currentWeight + ((StandardPackage) p).getWeight() > maxWeight)
			return;
		else {
			if (currentWeight + 1 > maxWeight)
				return;
		}

		// weight check passed
		if (p.getStatus().equals(Status.BRANCH_STORAGE)) {
			p.addTracking(this, Status.HUB_TRANSPORT);
			p.setStatus(Status.HUB_TRANSPORT);
		} else if (p.getStatus().equals(Status.HUB_STORAGE)) {
			p.addTracking(this, Status.BRANCH_TRANSPORT);
			p.setStatus(Status.BRANCH_TRANSPORT);
		}
		if (!(super.packages.add(p)))
			System.out.println("Problem adding package " + p.toString());

		destination.deliverPackage(p);

	}

}
