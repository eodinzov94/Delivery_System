package GUI;

/**This interface is part of a Listener - Subject Design Pattern set up to synchronize between the back-end and front end
 * <p>
 * We implemented this DP as a means of synchronizing, as much as possible, between the back-end logic that the system objects go through (such as trucks and branches)
 * and their appearance on screen using the GUI elements we've created.
 * <p>
 * A class implementing the Observable listener will have 2 main functionalities:<p>
 * Whenever the GUI element needs to be alerted that a change has transpired that requires a GUI update, alert() must be called by the GUIListener.<p>
 * Whenever an object in the system that requires a GUIListener is created, registerListener() must be invoked in order to synchronize the back-end and front-end around the object.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 2.0 -- 26.4.2021
 *
 */
public interface Observable {
	/**This function invoked a GUI elements update() function once an observable object requires its GUI representation to update.
	 * 
	 */
	public void alert();
	/**This function instantiates the GUIListener of the back-end object, thus 'synchronizing' the back-end object with its front-end representation.
	 * 
	 */
	public void registerListener();
}
