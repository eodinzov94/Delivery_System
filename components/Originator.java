package components;

import GUI.DeliveryGUI;
import GUI.DrawTruck;
public class Originator {
	
	public static void createState() {
		Caretaker.addState(new State());
	}
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

