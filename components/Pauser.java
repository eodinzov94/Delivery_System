package components;
/**
 * This class represents a monitor for all threads 
 * to pause or resume all threads when "STOP" or "RESUME" buttons are pressed in GUI
 * <p>
 * Inspired by stackoverflow thread : https://stackoverflow.com/questions/13146993/pausing-resuming-multiple-threads-in-java
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 2.0 -- 26.4.2021
 * 
 *
 */
public class Pauser {
	private boolean isPaused;
	/**
	 * Constructor for the class Pauser.
	 * 
	 */
	public Pauser() {
		isPaused = false;
	}
	/**
	 * Method that sets state of the system as "paused"
	 * 
	 */
	public synchronized void pause() {
		isPaused = true;
	}
	/**
	 * Method that changing state of the system to "resumed"
	 * by notifying all threads
	 */
	public synchronized void resume() {
		isPaused = false;
		notifyAll();
	}

	/**
	 * Getter for variable 'isPaused'
	 * @return isPaused- boolean representing state of the system
	 */
	public boolean isPaused() {
		return isPaused;
	}
	/**
	 * Method that swaps the system into pause state when 'isPaused' is true
	 * It is important to note that every Thread is using Static Pauser in DeliveryGUI system
	 * by using this method to synchronize the system state
	 * @see DeliveryGUI
	 */
	public synchronized void look() {
		while (isPaused) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		
	}

}