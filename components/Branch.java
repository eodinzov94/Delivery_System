package components;

import java.util.ArrayList;
import java.util.Vector;

import GUI.DeliveryGUI;
import GUI.DisplayPanel;
import GUI.DrawBranch;

import GUI.Observable;

/**
 * This class represents a branch in the delivery system.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 * @see Node
 *
 */
public class Branch implements Node, Runnable, Observable {

	static int numBranch = -1; // starts at -1, because HUB is created first.
	private final int branchId;
	private final String branchName;
	private ArrayList<Truck> listTrucks;
	private Vector<Package> listPackages;
	private ArrayList<Thread> truckThreads;
	DrawBranch guiListener = null;

	/**
	 * Constructor for the class Branch.
	 * <p>
	 * Note: the branchId field is created using a static running counter named
	 * 'numBranch'.
	 */
	public Branch() {
		this.branchId = numBranch++;
		this.branchName = "Branch " + branchId;
		this.listTrucks = new ArrayList<Truck>();
		this.listPackages = new Vector<Package>();
		this.truckThreads = new ArrayList<Thread>();
		System.out.println("Creating " + this);
		registerListener();
	}

	/**
	 * Get function for the field 'listTrucks'
	 * 
	 * @return listTrucks - ArrayList
	 * 
	 */
	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}

	/**
	 * Set function for the field 'listTrucks'
	 * 
	 * @param listTrucks - ArrayList<Truck> objects, can be null.
	 * 
	 */
	public void setListTrucks(ArrayList<Truck> listTrucks) {
		this.listTrucks = listTrucks;
	}

	/**
	 * Get function for the field 'listPackages'
	 * 
	 * @return listPackages - ArrayList
	 * 
	 */
	public Vector<Package> getListPackages() {
		synchronized (listPackages) {
			return listPackages;
		}
	}

	/**
	 * Get function for the field 'branchId'
	 * 
	 * @return branchId - integer
	 * 
	 */
	public int getBranchId() {
		return branchId;
	}

	/**
	 * Get function for the field 'branchName'
	 * 
	 * @return branchName - String
	 * 
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * This function finds an available truck.
	 * <p>
	 * This function runs through the list of trucks, until it finds one with the
	 * "available" field equaling 'true'
	 * 
	 * @return Truck object, or null when no trucks are available
	 * 
	 */
	private Truck findAvaliableTruck() {
		for (Truck t : listTrucks)
			if (t.isAvailable())
				return t;
		return null;
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	public String toString() {
		return "Branch " + this.branchId + ", branch name:" + branchName + ", packages: " + listPackages.size()
				+ ", packages: " + listTrucks.size();
	}

	/**
	 * Compares this object to another, and returns true if they're equal.
	 * <p>
	 * 
	 * Compares both pointers directly, then checks if there's a type mismatch, and
	 * checks the local fields.
	 * 
	 * @return boolean
	 * 
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof Branch))
			return false;

		return this.branchId == ((Branch) other).getBranchId();
	}

	/**
	 * Constructor for the class Branch.
	 * <p>
	 * Note: the branchId field is created using a static running counter named
	 * 'numBranch'.
	 * 
	 * @param branchName - pregenerated custom name for the branch.
	 */
	public Branch(String branchName) {
		this.branchId = numBranch++;
		this.branchName = branchName;
		this.listTrucks = new ArrayList<Truck>();
		this.listPackages = new Vector<Package>();
		this.truckThreads = new ArrayList<Thread>();
		registerListener();
	}

	/**
	 * This function collects a package and adds it to the local list of packages.
	 * 
	 * <p>
	 * 
	 * This function prints a message if an error occurred.
	 * 
	 * @param p - A package to add to the current list of packages.
	 */
	public void collectPackage(Package p) {
			addPackage(p);
	}

	/**
	 * This function delivers a package to a truck, and removes it from the local
	 * list of packages.
	 * 
	 * <p>
	 * 
	 * This function prints a message if an error occurred.
	 * 
	 * @param p - A package to remove from the current list of packages.
	 */
	public void deliverPackage(Package p) {
			removePackage(p);
	}

	/**
	 * Implementation of the 'getName' function in the Node interface
	 * 
	 * @see Node
	 * @return branch name- String.
	 * 
	 */
	public String getNodeName() {
		return getBranchName();
	}

	/**
	 * This function imitates a unit of work during a clock pulse.
	 * <p>
	 * The function searches the current list of packages in the branch. If such
	 * exist, it searches for an available Van to complete the task. Once a Van has
	 * been found - the function handles both the 'picking up from sender' and
	 * 'delivering to recipient' tasks using the Van it found 
	 *  after all Van instance's variables manipulations are done, sends notify signal to responsible thread to start Van's work.
	 * <p>
	 *  When unit of work is done this function also using alert function to update the GUI state represented in DrawBranch class
	 *
	 * @see DrawBranch
	 * @see Van
	 */
	public void work() {
		synchronized (listPackages) {
			for (Package p : listPackages) {
				Truck van = this.findAvaliableTruck();
				if (van == null)
					break;
				synchronized (van) {
					if (p.getStatus().equals(Status.CREATION)) {
						van.setTimeLeft(p.getSenderAddress().getStreet() % 10 + 1);
						van.collectPackage(p);
						van.notify();
					} else if (p.getStatus().equals(Status.DELIVERY)) {
						van.setTimeLeft(p.getDestinationAddress().getStreet() % 10 + 1);
						van.collectPackage(p);
						van.notify();
					}
				}
			}
		}
		alert();
	}

	/**
	 * This function is an extension for work().
	 * <p>
	 * it calls each of the branches' Vans' work() function.
	 * @deprecated as a part of HW2
	 */
	public void workTrucks() {
		for (Truck t : listTrucks) {
			t.work();
		}
	}

	/**
	 * This function adds a truck to the existing list of trucks for the branch.
	 * <p>
	 * The function, upon receiving a string representing a type of Truck, uses a
	 * 'Message passing'-esque technique to add a new truck object to the branch.
	 * <p>
	 * This function is inherited by 'Hub' therefore it also handles the
	 * 'NonStandardTruck' case.
	 * 
	 * @param truckType - String representing a type of truck.
	 * @see Hub
	 */
	public void addTruck(String truckType) {
		Truck t = null;
		switch (truckType) {
		case ("Van"):
			t = new Van();
			break;
		case ("StandardTruck"):
			t = new StandardTruck();
			break;

		case ("NonStandardTruck"):
			t = new NonStandardTruck();
			break;
		default:
			t = new StandardTruck();
			break;
		}
		listTrucks.add(t);
		addTruckThread(t);
	}

	/**
	 * This function implements 'Observable' interface inspired by Listener-Observer that we learned 
	 * in classroom. 
	 * Function alerts the 'DrawBranch' instance that something changed
	 * to represent the changes in GUI namely in DisplayPanel when repainting new frame
	 * <p>
	 * @see Listener
	 * @see Observable
	 * @see DrawBranch
	 * @see DisplayPanel
	 */
	public void alert() {
		try {
			this.guiListener.update(this);
		} catch (Exception e) {
		}

	}
	/**
	 * This function implements 'Observable' interface inspired by Listener-Observer that we learned 
	 * in classroom. 
	 * Function using search method from Display panel to find matching DrawBranch instance with same ID
	 * to store the reference of DrawBranch instance which represents the Branch in a GUI.
	 * It is important to note that such instance will always exist since the all GUI and "Pseudo - Backend " objects are initialized exactly in the same order.
	 * where components package represents "pseudo backend" and GUI package represents frontend
	 * <p>
	 * @see Listener
	 * @see Observable
	 * @see DrawBranch
	 * @see DisplayPanel
	 */
	public void registerListener() {
		try {
			this.guiListener = DisplayPanel.getDrawBranchById(this.branchId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * This method checks if there is any package stored in a branch.
	 * <p>
	 * this method used by guiListener ( DrawBranch instance) to set matching color state.
	 * this method also synchronized to prevent ConcurrentModificationException, because default Iterator is not thread safe 
	 * @return true if stores any packages , otherwise false.
	 * @see DrawBranch
	 * 
	 */
	public boolean storesPackages() {
		synchronized(listPackages) {
		for (Package p : listPackages) {
			if (p.getStatus().equals(Status.BRANCH_STORAGE) || p.getStatus().equals(Status.DELIVERY))
				return true;
		}
		}
		return false;
	}

	/**
	 * This method implements Runnable interface. 
	 * Represents Branch work / Hub work (as inherited method), runs while there is at least one package to deliver, 
	 * then checks the system state ( pause, resume @see DeliveryGUI) then calls work method which represents one unit of work.
	 * then changes current thread state into 'sleep' for 500ms
	 * <p>
	 * @see DrawBranch
	 * @see Pauser
	 */
	public void run() {
		System.out.println(this.getNodeName() + " Is Started Work now!");
		while (!MainOffice.isFinished) {
			try {
				DeliveryGUI.pauser.look();
				work();
				Thread.sleep(500L);
			} catch (InterruptedException e) {
			}
		}
		System.out.println(this.getNodeName() + " Is finished work!");
	}
	/**
	 * Getter for field 'guiListener'
	 * 
	 * @return guiListener - DrawBranch 
	 * @see DrawBranch 
	 * 
	 */
	public DrawBranch getGuiListener() {
		return guiListener;
	}
	/**
	 * This method adding new Thread to array list of threads ,that executes Truck's (Van,Standard,NonStandard) run method. 
	 * Used to initialize ArrayList of trucks
	 * 
	 */
	private void addTruckThread(Truck t) {
		Thread thread = new Thread(t);
		this.truckThreads.add(thread);
	}
	/**
	 * This method is starter for all Threads that representing trucks
	 * basically starts all truck threads.
	 * 
	 */
	public void startAllTrucks() {
		for (Thread t : truckThreads) {
			t.start();
		}
	}
	/**
	 * This method is used as API to add packages into
	 *  Vector to provide another layer of synchronized protection. 
	 * 
	 * 
	 */
	public void addPackage(Package p) {
		synchronized (listPackages) {
			listPackages.add(p);
		}
	}
	/**
	 * This method is used as API to remove packages into
	 *  Vector to provide another layer of synchronized protection. 
	 */
	public void removePackage(Package p) {
		synchronized (listPackages) {
			listPackages.remove(p);
		}
	}
}
