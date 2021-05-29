package components;

import java.util.ArrayList;

import GUI.DrawPackage;
import GUI.DrawTruck;

/**
 * This class represents the hub in the delivery system.
 * <p>
 * The class is no longer a singleton since version 3.0 due to cloning reasons.
 * 
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 10.6.2021
 * @see Node
 * @see Branch
 *
 */
public class Hub extends Branch implements Node {


	private ArrayList<Branch> branches;
	private ArrayList<Thread> branchesThreads;
	private static int nextBranch = 0;

	/**
	 * Constructor for the class Hub.
	 * <p>
	 * Access is private because the class was implemented as a Singleton.
	 */
	public Hub() {
		super("HUB");
		branches = new ArrayList<Branch>();
		branchesThreads = new ArrayList<Thread>();
		System.out.println("Creating " + super.toString());
	}

	public void resetHub() {
		Truck.numTrucks = 0;
		Package.numOfPackages = 0;
		DrawPackage.numOfPackages = 0;
		DrawTruck.numTrucks = 0;
	}


	/**
	 * Implementation of the 'getName' function in the Node interface
	 * 
	 * @see Node
	 * @return hub name- String.
	 * 
	 */
	public String getNodeName() {
		return "Hub";
	}

	/**
	 * This function finds an available truck.
	 * <p>
	 * This function runs through the list of trucks, until it finds one with the
	 * "available" field equaling 'true' and a type matching the type represented in
	 * the parameter 'type'. The function uses a 'Message passing'-esque technique,
	 * and works with both 'Standard' and 'NonStandard' trucks.
	 * 
	 * @param type - String representing a type of truck; Standard or NonStandard
	 * @return Truck object, or null when no trucks are available
	 * 
	 */
	public Truck findAvailableTruck(String type) {
		if (type == "StandardTruck") {
			for (Truck t : super.getListTrucks()) {
				if (t instanceof StandardTruck && t.isAvailable())
					return t;
			}
		} else if (type == "NonStandardTruck") {
			for (Truck t : super.getListTrucks()) {
				if (t instanceof NonStandardTruck && t.isAvailable())
					return t;
			}
		}
		return null;
	}

	/**
	 * Get function for the field 'branches'
	 * 
	 * @return branches - ArrayList<Branch>
	 * 
	 */
	public ArrayList<Branch> getBranches() {
		return branches;
	}

	/**
	 * This function attempts to find a branch using a given ID.
	 * 
	 * @param branchId - Integer marking the id of a pre-existing branch
	 * @see Branch
	 * @return Branch b - A branch with the id = 'branchId' or null if none were
	 *         found.
	 * 
	 */
	public Branch findBranchById(int branchId) {
		for (Branch b : this.branches) {
			if (b.getBranchId() == branchId)
				return b;
		}
		return null;
	}

	/**
	 * This function attempts to send an available truck to a branch represented by
	 * the given id.
	 * <p>
	 * This function attempts to find an available truck. Given one, it searches for
	 * a branch with the id 'branchId'. Once both have been found, if any packages
	 * are awaiting delivery to the branch, the truck loads them up and heads
	 * towards the branch by using notify() method.
	 * 
	 * 
	 * @param branchId - Integer marking the id of a pre-existing branch
	 * @see Branch
	 * @return true if a truck was sent, false otherwise.
	 * 
	 */
	public boolean sendToWorkStandardTruckByBranchId(int branchId) {
		Truck st = findAvailableTruck("StandardTruck");
		if (st == null)
			return false;
		Branch b = findBranchById(branchId);
		if (b == null) {
			System.out.println("Invalid branch ID: " + String.valueOf(branchId));
			return false;
		}
		synchronized (st) {
			((StandardTruck) st).setDestination(this);
			ArrayList<Package> copyOfListPackages;
			synchronized (super.getListPackages()) {
				copyOfListPackages = new ArrayList<Package>(super.getListPackages());// prevents
			} // ConcurrentModificationException
			for (Package p : copyOfListPackages) {
				if (p.getDestinationAddress().getZip() == branchId && !(p instanceof NonStandardPackage)) {
					st.collectPackage(p);
				}
			}
			((StandardTruck) st).setDestination(b);
			st.setAvailable(false);
			st.setTimeLeft(MainOffice.getRand().nextInt(11) + 1);
			System.out.println(st.getNodeName() + " is on it's way to " + b.getNodeName() + ", time to arrive: "
					+ st.getTimeLeft());
			st.notify();
		}
		nextBranch = (nextBranch + 1) % branches.size();
		return true;
	}

	/**
	 * This function attempts to find a NonStandard package.
	 * <p>
	 * The function scans the whole list of packages and attempts to find a
	 * NonStandard package. (used in Hubs' work() function)
	 * 
	 * @return NonStandardPackage object if it exists, null if none exists.
	 * 
	 */
	public Package FindNonStandardPackage() {
		synchronized (super.getListPackages()) {
			for (Package p : super.getListPackages())
				if (p instanceof NonStandardPackage && p.getStatus().equals(Status.CREATION))
					return p;
		}
		return null;
	}

	/**
	 * This function checks and returns whether a given package fits into the
	 * NonStandardTruck provided.
	 * 
	 * @param t - NonStandardTruck attempting to fit the package.
	 * @param p - NonStandardPackage attempting to fit in the truck.
	 * @return true if the package fits, false otherwise.
	 * @deprecated as of part 2 of the project
	 */
	public boolean doesFit(NonStandardTruck t, NonStandardPackage p) {
		return t.getHeight() >= p.getHeight() && t.getLength() >= p.getLength() && t.getWidth() >= p.getWidth();
	}

	/**
	 * This function imitates a unit of work during a clock pulse.
	 * <p>
	 * The function searches the current list of packages in the hub. If such exist,
	 * it searches for an available Truck to complete the task. Once a Truck has
	 * been found - the function handles all the appropriate stages of delivery by
	 * using notify() method.
	 * 
	 * The hub handles all types of packages, including NonStandard.
	 * <p>
	 * 
	 * @see StandardTruck
	 * @see NonStandardTruck
	 */
	public void work() {

		while (sendToWorkStandardTruckByBranchId(nextBranch)) {
			sendToWorkStandardTruckByBranchId(nextBranch);
		}

		Package nonStandardPackage = FindNonStandardPackage();
		Truck nonStandardTruck = findAvailableTruck("NonStandardTruck");
		if (nonStandardPackage != null && nonStandardTruck != null) {
			synchronized (nonStandardTruck) {
				nonStandardTruck.setTimeLeft(MainOffice.getRand().nextInt(10) + 1);
				((NonStandardTruck) nonStandardTruck).collectPackage(nonStandardPackage);
				nonStandardTruck.notify();
			}
		}
	}

	/**
	 * Getter for field 'branchesThreads'
	 * 
	 * @return branchesThreads - ArrayList<Thread>
	 */
	public ArrayList<Thread> getBranchesThreads() {
		return branchesThreads;
	}

	/**
	 * This method adding new brunch to array list of branches , and also adding
	 * Thread that executes that branch in future.
	 */
	public void addBranch(Branch b) {
		branches.add(b);
		branchesThreads.add(new Thread(b));
	}

	/**
	 * Starter for all branches Threads basically starts all threads that executes
	 * branches
	 * 
	 */
	public void startAllBranches() {

		for (Thread t : branchesThreads) {
			t.start();
		}
	}

//	@Override
//	protected Object clone() throws CloneNotSupportedException {
//		Object clone = null;
//		clone = super.clone();
//		for(Truck t:this.getListTrucks())
//			((Branch)clone).getListTrucks().add((Truck) t.clone());
//		return clone;
//	}

}
