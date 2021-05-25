package program;
import GUI.DeliveryGUI;
/**
 * This class a driver class for the delivery system
 * <p>
 * 
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 * @see DeliveryGUI
 */
public class Game {
	/**
	 * Main calls for the GUI display to pop up, allowing the configuration and running of the delivery system.
	 * @param args - String[]
	 */
	public static void main(String[] args) {
		DeliveryGUI game = DeliveryGUI.getDeliveryGUI();
	}

}
	