package components;

import java.util.Stack;

/**Caretaker class for the state changing procedure of the memento DP.
 * <p>
 * The states are implemented in a Stack so that a LIFO order is established between them, meaning
 * you can only return back to the directly previous state saved (much like a ctrl-z operation).
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 10.6.2021
 * 
 *
 */
public class Caretaker {
	private static Stack<State> caretaker = new Stack<State>();
	/**Add function for a new state, adds it to the top of the stack.
	 * 
	 * @param s - new state to add
	 */
	public static void addState(State s) {
		caretaker.add(s);
	}
	/**Helper function akin to 'pop' that returns the previous state saved at the top of the stack.
	 * 
	 * @return s - latest state at the top of the stack
	 */
	public static State getState() {
		return caretaker.pop();
	}
}
