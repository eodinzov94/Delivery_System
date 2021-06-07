package components;
import java.util.ArrayList;

import GUI.DisplayPanel;
import GUI.DrawPackage;
import GUI.Observable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 * This class represents a package in the delivery system.
 * <p>
 * The package in this form is an abstract object, and is meant to be
 * implemented by derived subclasses.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 * @see Node
 * @see Branch
 *
 */
public abstract class Package implements Observable{
	public static int numOfPackages = 0; // running static integer that helps number our packages.
	private final int packageID;
	private Priority priority;
	private Status status;
	private final Address senderAddress;
	private final Address destinationAddress;
	private ArrayList<Tracking> tracking;
	private DrawPackage guiListener = null ;
	private PropertyChangeSupport support; 
	int customerId;
	private synchronized static int setPackageID() {
		return numOfPackages++;
	}

	/**
	 * Constructor for the class StandardPackage.
	 * 
	 * @param priority          - enum representing priority.
	 * @param senderAddress     - Address instance representing the sender address
	 * @param destinationAdress - Address instance representing the destination
	 *                          address
	 * @param c 				- customer instance
	 */
	public Package(Priority priority, Address senderAddress, Address destinationAdress,Customer c) {
		this.priority = priority;
		this.senderAddress = senderAddress;
		this.destinationAddress = destinationAdress;
		this.tracking = new ArrayList<Tracking>();
		this.packageID = 1000 + setPackageID();
		this.status = Status.CREATION;
		tracking.add(new Tracking(MainOffice.getClock(), c, Status.CREATION));
		registerListener();
		customerId = c.getCustomerId();
		support = new PropertyChangeSupport(this);
		addPropertyChangeListener(MainOffice.getInstance());
		support.firePropertyChange("CREATION", null, this);
	}

	/**Copy constructor for the class
	 * 
	 * @param other - Package object to copy fields from
	 */
	public Package(Package other) {
		this.priority = other.priority;
		this.senderAddress = other.senderAddress;
		this.destinationAddress = other.destinationAddress;
		this.tracking = new ArrayList<Tracking>(other.getTracking());
		this.packageID = other.getPackageID();
		this.status = other.getStatus();
		customerId = other.customerId;
		support = other.support;
	}
	
	/**
	 * Function to add a new tracking record to the tracking list.
	 * <p>
	 * The function uses a system-universal clock implemened in the main office to
	 * update the 'time' segment of a tracking record.
	 * 
	 * @param node   - A node object representing the current locatin of the package
	 * @param status - A status object describing the package's transit status.
	 */
	void addTracking(Node node, Status status) {
		Tracking newNode = new Tracking(MainOffice.getClock(), node, status);
		tracking.add(newNode);
		support.firePropertyChange(status.toString(), null, this);
	}

	/**
	 * Small function to print out a tracking record.
	 * <p>
	 * The function scans the whole list of tracking records, and calls them to be
	 * printed one by one.
	 */
	void printTracking() {
		System.out.println("TRACKING " + this);
		for (Tracking t : tracking) {
			System.out.println(t);
		}
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return "Package [packageID=" + packageID + "Sender ID=" + customerId + ", priority="  + priority + ", status=" + status + ", senderAddress="
				+ senderAddress + ", destinationAddress=" + destinationAddress + "]";
	}


	/**
	 * Get function for the field 'numOfPackages'
	 * 
	 * @return numOfPackages - Int
	 * 
	 */
	public int getPackageID() {
		return packageID;
	}

	/**
	 * Get function for the field 'priority'
	 * 
	 * @return priority - Priority
	 * 
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * Set function for the field 'priority'
	 * 
	 * @param priority - Priority
	 * 
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * Get function for the field 'status'
	 * 
	 * @return status - Status
	 * 
	 */
	public Status getStatus() {
		synchronized(status) {
		return status;
		}
	}

	/**
	 * Set function for the field 'status'
	 * after status changed ,alert function is called to represent the change in a GUI
	 * @param status - Status
	 * 
	 */
	public void setStatus(Status status) {
		synchronized(status) {
			this.status = status;
			this.alert();
		}
	}
	/**
	 * Get function for the field 'senderAddress'
	 * 
	 * @return senderAddress - Address
	 * 
	 */
	public Address getSenderAddress() {
		return senderAddress;
	}

	/**
	 * Get function for the field 'destinationAddress'
	 * 
	 * @return destinationAddress - Address
	 * 
	 */
	public Address getDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * Get function for the field 'tracking'
	 * 
	 * @return tracking - ArrayList<Tracking>
	 * 
	 */
	public ArrayList<Tracking> getTracking() {
		return tracking;
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
		if (other instanceof Package)
			return this.packageID == ((Package) other).packageID;
		return false;
	}
	/**
	 * This function implements 'Observable' interface inspired by Listener-Observer that we learned 
	 * in classroom. 
	 * Function using search method from DisplayPanel to find matching DrawPackage instance with same ID
	 * to store the reference of DrawPackage instance which represents the Package in a GUI.
	 * <p>
	 * @see Listener
	 * @see Observable
	 * @see DrawPackage
	 * @see DisplayPanel
	 */
	public void registerListener() {
		try {
			this.guiListener= DisplayPanel.getDrawPackageById(this.packageID);
			guiListener.setHidden(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * This function implements 'Observable' interface inspired by Listener-Observer that we learned 
	 * in classroom. 
	 * Function alerts the 'DrawPackage' instance that something changed
	 * to representing the changes in GUI namely in DisplayPanel when repainting new frame
	 * <p>
	 * @see Listener
	 * @see Observable
	 * @see DrawPackage
	 * @see DisplayPanel
	 */
	public void  alert() {
		try {
			this.guiListener.update(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Getter for field 'guiListener'
	 * 
	 * @return guiListener - DrawPackage
	 * @see DrawPackage 
	 * 
	 */
	public DrawPackage getGuiListener() {
		return guiListener;
	}
	
	/**Helper function to add a PropertyChangeListener to this object, as used in the PropertyChangeSupport class.
	 * 
	 * @param pcl - PropertyChangeListener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl){ 	
    	support.addPropertyChangeListener(pcl); 
    } 
    
	/**Helper function to remove a PropertyChangeListener to this object, as used in the PropertyChangeSupport class.
	 * 
	 * @param pcl - PropertyChangeListener to remove.
	 */
    public void removePropertyChangeListener(PropertyChangeListener pcl){ 	
    	support.removePropertyChangeListener(pcl); 
    } 
}
	