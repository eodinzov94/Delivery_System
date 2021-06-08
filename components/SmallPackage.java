package components;


/**
 * This class represents the small package in the delivery system.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 *
 */
public class SmallPackage extends Package {
	private final boolean acknowledge;
	
	/**
	 * Constructor for the class SmallPackage.
	 * 
	 * @param priority    - enum representing priority.
	 * @param senderAddress - Address instance representing the sender address
	 * @param destinationAdress - Address instance representing the destination address
	 * @param acknowledge - boolean representing Customer confirmation when the package is delivered,
	 * true - customer has to confirm the delivery, false - no confirmation needed
	 */
	public SmallPackage(Priority priority, Address senderAddress, Address destinationAdress, boolean acknowledge, Customer c) {
		super(priority,senderAddress,destinationAdress , c);
		this.acknowledge=acknowledge;
		System.out.println("Creating "+ this);
	}
	
	/**Copy constructor for the class
	 * 
	 * @param other - Package object to copy fields from
	 */
	public SmallPackage(Package other) {
		super(other);
		this.acknowledge=((SmallPackage)other).acknowledge;
		System.out.println("Copying "+ this);
	}
	
	
	
	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	public String toString() {
		return "SmallPackage [packageID=" + getPackageID() + ", Sender ID=" + customerId + ", priority=" + getPriority() + ", status=" + getStatus() + ", senderAddress="
				+ getSenderAddress() + ", destinationAddress=" + getDestinationAddress() + ", acknowledge=" + acknowledge + "]";
	}
	/**
	 * Get function for the field 'acknowledge'
	 * 
	 * @return acknowledge - boolean
	 * 
	 */
	public boolean isAcknowledge() {
		return acknowledge;
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
		if (this == other) return true;
		
		if (!(other instanceof SmallPackage)) return false;
		
		return super.equals(other);
	}
	
}
