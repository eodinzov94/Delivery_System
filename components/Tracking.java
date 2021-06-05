package components;
/**
 * This class represents a Tracking in the delivery system.
 * <p>
 * 
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 *
 *
 */
public class Tracking implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int time;
	private final Node node;
	private Status status;
	/**
	 * Constructor for the class Tracking.
	 * 
	 * @param time - int representing time when Tracking added to the package
	 * @param node - Node -reference to the instance that implements Node interface (can be any type of Truck or Branch )
	 * @param status - enum Status instance representing current Package delivery status
	 
	 */
	public Tracking(int time, Node node, Status status) {
		this.time=time;
		this.node =node;
		this.status = status;
	}
	/**
	 * Get function for the field 'time'
	 * 
	 * @return time - int
	 * 
	 */
	public int getTime() {
		return time;
	}
	/**
	 * Set function for the field 'time'
	 * 
	 * @param time - int
	 * 
	 */
	public void setTime(int time) {
		this.time = time;
	}
	/**
	 * Get function for the field 'node'
	 * 
	 * @return node - Node is a reference to the instance that implements Node interface (can be any type of Truck or Branch )
	 * 
	 */
	public Node getNode() {
		return node;
	}
	/**
	 * Get function for the field 'status'
	 * 
	 * @return status - enum Status 
	 * @see Status
	 * 
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * Set function for the field 'status'
	 * 
	 * @param status - enum Status 
	 * @see Status
	 * 
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		String name = (node==null)? "Recipient" : node.getNodeName();
		return time + ": " + name + ", status=" + status;
		
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
		
		if (!(other instanceof Tracking)) return false;
		
		Tracking temp = ((Tracking)other);
		return this.node==temp.node && this.status==temp.status;
	}
}
