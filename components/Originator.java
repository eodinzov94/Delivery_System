package components;


import GUI.DeliveryGUI;
import GUI.DrawTruck;

/**This class represents the Originator in the Memento DP used for saving states of the system.
 * <p>
 * This class handles the creation and setting of states, while the Caretaker handles creation of states, this class handles setting the state for the MainOffice instance.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 10.6.2021
 * @see Caretaker
 * 
 */
public class Originator {
	
	/**Calls upon the Caretaker to create a new state, adding the current 'snapshot' of the system to the top of the states stack.
	 * 
	 */
	public static void createState() {
		Caretaker.addState(new State());
	}
	/**This function calls for the Caretaker to pop the previous state, and uses the parameters saved in the state to restore the system to the previous 'snapshot'
	 * 
	 */
	public static void setState() {
		State s = Caretaker.getState();
		Truck.numTrucks = s.numTrucks;
		Package.numOfPackages= s.numOfPackages;
		DrawTruck.numTrucks= s.numDrawTrucks;
		Branch.numBranch -= 1;
		DeliveryGUI.getDeliveryGUI().getDisplay().setState(s);
		MainOffice.getInstance().setMainOffice(s);
	}

	
}

