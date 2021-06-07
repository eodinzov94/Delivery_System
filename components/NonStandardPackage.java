package components;



/**
 * This class represents a NonStandardPackage in the delivery system.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 * @see Package
 *
 */
public class NonStandardPackage extends Package {
	private final int width;
	private final int length;
	private final int height;

	/**
	 * Constructor for the class NonStandardPackage.
	 * <p>
	 * Parameters:
	 * <p>
	 * priority, senderAddress, destinationAdress - Parameters to create the superclass object within this class.
	 * <p>
	 * width, length, height - Specifications for the size of the package.
	 * 
	 */
	public NonStandardPackage(Priority priority, Address senderAddress, Address destinationAdress, int width,
			int length, int height , Customer c) {
		super(priority, senderAddress, destinationAdress, c);
		this.height = height;
		this.length = length;
		this.width = width;
		System.out.println("Creating " + this);
	}

	/**Copy constructor for the class
	 * 
	 * @param other - Package object to copy fields from
	 */
	public NonStandardPackage(Package other) {
		super(other);
		NonStandardPackage nsp = (NonStandardPackage) other;
		this.height = nsp.height;
		this.length = nsp.length;
		this.width = nsp.width;
		System.out.println("Copying " + this);
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
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	public String toString() {
		return "NonStandardPackage [packageID=" + getPackageID() + ", Sender ID=" + customerId + ", priority=" + getPriority() + ", status=" + getStatus()
				+ ", senderAddress=" + getSenderAddress() + ", destinationAddress=" + getDestinationAddress() + ", width=" + width
				+ ", length=" + length + ", height=" + height + "]";
	}

	/**
	 * Compares this object to another, and returns true if they're equal.
	 * <p>
	 * 
	 * Compares both pointers directly, then checks if there's a type mismatch, and checks the local fields.
	 * 
	 * @return boolean
	 * 
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof NonStandardPackage))
			return false;

		NonStandardPackage temp = ((NonStandardPackage) other);
		return (width == temp.width && length == temp.length && height == temp.height);
	}

	

}
