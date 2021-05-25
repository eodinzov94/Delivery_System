package GUI;

/**This interface is part of a Listener - Subject Design Pattern set up to synchronize between the back-end and front end
 * <p>
 * We implemented this DP as a means of synchronizing, as much as possible, between the back-end logic that the system objects go through (such as trucks and branches)
 * and their appearance on screen using the GUI elements we've created.
 * <p>
 * A class implementing the Listener interface is a back-end class that will, upon having done an action that needs to update its presence on the GUI, invoke update() to its GUI listener
 * that it contains, which will in turn reflect the change onto the screen.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 2.0 -- 26.4.2021
 *
 */
public interface Listener {
	/** This function is called whenever a Listener object underwent a change in status that must be reflected onto the GUI.
	 * <p>
	 * When invoked, a set of details that describe the current status of the object are sent to the GUI element in order to update
	 * its representation on the screen.
	 * 
	 * @param obj - GUI listener object implementing the Observable interface
	 * @throws Exception
	 */
	public void update(Observable obj) throws Exception;
	
}
