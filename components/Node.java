package components;

/**
 * This interface is shared amongst all the trucks and branches (Hub included)
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 *
 */
public interface Node{

	/**
	 * Generally, this function handles the receiving of a package by the object by
	 * a different entity.
	 */
	void collectPackage(Package p);

	/**
	 * Generally, this function handles the handing-off of a package by the object
	 * to a different entity.
	 */
	void deliverPackage(Package p);

	/**
	 * This function activates different actions & methods according to the
	 * in-system passage of time by the clock provided in the main office. It is
	 * shared by both trucks and branches alike, but implemented differently.
	 */
	void work();

	/**
	 * General getter for a representation of the entity in a formal fashion.
	 */
	String getNodeName();
}
