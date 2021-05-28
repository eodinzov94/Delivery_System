package components;


public class Originator {
	
	public static void createState() {
		Caretaker.addState(new State());
	}
	public static void setState() {
		State s = Caretaker.getState();
		//TODO: set all
		
	}
	
}
