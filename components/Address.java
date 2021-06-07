	package components;

/**
 * This class represents an address for a customer; Both the sender and the
 * receiver are considered customers.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 * 
 *
 */
public class Address {
	private final int zip;
	private final int street;

	/**
	 * Constructor for the class Address.
	 * 
	 * @param zip    - Integer representing zip code.
	 * @param street - Integer representing street number
	 * 
	 */
	public Address(int zip, int street) {
		this.zip = zip;
		this.street = street;
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return zip + "-" + street;
	}
	/**
	 * Get function for the field 'Zip'
	 * 
	 * @return zip - Integer
	 * 
	 */
	public int getZip() {
		return zip;
	}

	/**
	 * Get function for the field 'street'
	 * 
	 * @return street - Integer
	 * 
	 */
	public int getStreet() {
		return street;
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

		if (!(other instanceof Address))
			return false;
		Address temp = ((Address) other);
		return this.zip == temp.zip && this.street == temp.street;
	}
}	