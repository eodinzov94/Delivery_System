package components;


/**
 * This class represents the standard package in the delivery system.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 *
 */
public class StandardPackage extends Package {
	private final  double weight;
	/**
	 * Constructor for the class StandardPackage.
	 * 
	 * @param priority    - enum representing priority.
	 * @param senderAddress - Address instance representing the sender address
	 * @param destinationAdress - Address instance representing the destination address
	 * @param weight - double representing the package's weight
	 */
	public StandardPackage(Priority priority, Address senderAddress, Address destinationAdress, double weight, Customer c) {
		super(priority,senderAddress,destinationAdress,c);
		this.weight=weight;
		System.out.println("Creating "+ this);
	}
	
	/**Copy constructor for the class
	 * 
	 * @param other - Package object to copy fields from
	 */
	public StandardPackage(Package other) {
		super(other);
		this.weight=((StandardPackage)other).weight;
		System.out.println("Copying "+ this);
	}
	
	
	/**
	 * Get function for the field 'weight'
	 * 
	 * @return weight - double
	 * 
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	public String toString() {
		return "StandardPackage [packageID=" + getPackageID() + ", Sender ID=" + customerId + ", priority=" + getPriority() + ", status=" + getStatus() + ", senderAddress="
				+ getSenderAddress() + ", destinationAddress=" + getDestinationAddress() + ", weight=" + weight + "]";
	}
	
	/**
	 * Compares this object to another, and returns  true if they're equal.
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
		
		if (!(other instanceof StandardPackage)) return false;
		
		return super.equals(other);
	}
}
