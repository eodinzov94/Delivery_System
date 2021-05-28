package components;

import java.util.Stack;

public class Caretaker {
	private static Stack<State> caretaker = new Stack<State>();
	public static void addState(State s) {
		caretaker.add(s);
	}
	public static State getState() {
		return caretaker.pop();
	}
}
