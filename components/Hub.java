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
	private ArrayList<Thread> branchThreads;
	private static int nextBranch = 0;

	/**
	 * Constructor for the class Hub.
	 * <p>
	 */
	public Hub() {
		super("HUB");
		branches = new ArrayList<Branch>();
		branchThreads = new ArrayList<Thread>();
		System.out.println("Creating " + super.toString());
	}

	/**
	 * Copy Constructor for the class Hub.
	 * <p>
	 * 
	 * @param other - other hub to copy from
	 */
	public Hub(Hub other) {
		super(((Branch) other));
		branchThreads = new ArrayList<Thread>();
		branches = new ArrayList<Branch>();
		for (Branch b : other.getBranches()) {
			addBranch(new Branch(b));
		}

		System.out.println("Copying " + super.toString());
	}

	/**Helper function to reset the static parameters both in the GUI and the program.
	 * <p>
	 * occasionally during run-time we need to soft-reset these parameters to refresh the system,
	 * this function does that.
	 * 
	 */
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
		nextBranch = (nextBranch) % branches.size();
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
	 * This method adding new brunch to array list of branches , and also adding
	 * Thread that executes that branch in future.
	 */
	public void addBranch(Branch b) {
		branches.add(b);
		
	}
	/**Clones an existing branch by a given ID and immediately starts it so it joins the rest of the system. 
	 * <p>
	 * Uses the branchs' clone() implementation to create a new branch, add it to the list of branches, create
	 * a thread for it and start it.
	 * 
	 * @param id - ID of the branch to clone.
	 * 
	 */
	public void cloneBranch(int id) throws Exception {
		if (!(id >= 0 && id < branches.size()))
			throw new Exception("Invalid clone id received!\n");
		Branch clone = (Branch) (branches.get(id).clone());
		clone.registerListener();
		clone.startAllTrucks();
		addBranch(clone);
		Thread t = new Thread(clone);
		t.start();
		branchThreads.add(t);
	}

	/**
	 * Starter for all branches Threads basically starts all threads that executes
	 * branches
	 * 
	 */
	public void startAllBranches() {
		for (Branch b : this.branches) {
			Thread t = new Thread(b);
			t.start();
			branchThreads.add(t);
		}
		Thread t = new Thread(this);
		t.start();
		branchThreads.add(t);
	}

	/**Overrides the clone functon for this class, however the Hub cannot be copied so it throws and error.
	 * 
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Cannot clone hub!");
	}

	/**Simple helper function to set the current list of branches to a new one received as parameter.
	 * 
	 * @param branches - ArrayList of branches.
	 */
	public void setBranches(ArrayList<Branch> branches) {
		this.branches = branches;
	}

	/**helper function to help set the hub when a state change occurs.
	 * <p>
	 * State change means the last branch was removed (implemented as a stack) <br>
	 * therefore, we remove the last branch by copying all of the other ones from the given
	 * Hub instance by the state.
	 * <p>
	 * As with the nature of state changes, this is a shallow copy transfer to what had already existed
	 * in the previous state.
	 * 
	 * @param h - previous states' hub instance.
	 */
	public void setHub(Hub h) {
		branches.remove(branches.size()-1);
		for(int i=0; i<branches.size();i++) {
			Branch newB = branches.get(i);
			Branch oldB = h.branches.get(i);
			newB.setBranch(oldB);
		}
		this.setBranch(h);
	}
	/**Helper function to stop all the branches threads.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void stopAllBranches() {
		for( Thread t: branchThreads)
			t.stop();
	}
}
