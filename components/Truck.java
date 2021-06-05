package components;

import java.util.ArrayList;
import GUI.DeliveryGUI;
import GUI.DisplayPanel;
import GUI.DrawTruck;
import GUI.Observable;

/**
 * This abstract class represents a Track in the delivery system.
 * <p>
 * 
 * @see Van
 * @see StandardTruck
 * @see NonStandardTruck
 * @see Node
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 *
 *
 */
public abstract class Truck implements Node, Runnable, Observable, Cloneable {
	public static int numTrucks = 0; // running static integer that helps number our trucks.
	private int truckID;
	private String licensePlate;
	private String truckModel;
	private boolean available;
	private int timeLeft;

	DrawTruck guiListener = null;
	ArrayList<Package> packages;

	/**
	 * Default Constructor for the class Truck.
	 * 
	 * licensePlate - String representing license plate (sets it randomly)
	 * truckModel - String representing truck model (sets it randomly) available -
	 * boolean representing truck availability (sets True by default) truckID - int
	 * representing the truck id in delivery system (sets as running index 2000 +
	 * number of truck instances)
	 */
	public Truck() {
		truckModel = "M" + MainOffice.getRand().nextInt(5);
		licensePlate = (100 + MainOffice.getRand().nextInt(900)) + "-" + (10 + MainOffice.getRand().nextInt(90)) + "-"
				+ (100 + MainOffice.getRand().nextInt(900));
		available = true;
		truckID = 2000 + numTrucks;
		numTrucks++;
		packages = new ArrayList<Package>();
		registerListener();
	}
	/**
	 * Copy Constructor for the class Truck.
	 * 
	 * ( Deep Copy )
	 */
	public Truck(Truck other) {
		truckModel = other.getTruckModel();
		licensePlate = other.getLicensePlate();
		available = other.available;
		truckID = other.getTruckID();
		packages = new ArrayList<Package>();
		timeLeft = other.timeLeft;
		if (other.getPackages().size() > 0)
			for (Package p : other.getPackages()) {
				if (p instanceof SmallPackage)
					packages.add(new SmallPackage(p));
				else if (p instanceof StandardPackage)
					packages.add(new StandardPackage(p));
				else if (p instanceof NonStandardPackage)
					packages.add(new NonStandardPackage(p));
			}
		registerListener();
	}

	/**
	 * Constructor for the class Truck. available - boolean representing truck
	 * availability (sets True by default) truckID - int representing the truck id
	 * in delivery system (sets as running index 2000 + number of truck instances)
	 * 
	 * @param licensePlate - String representing license plate
	 * @param truckModel   - String representing truck model
	 */
	public Truck(String licensePlate, String truckModel) {
		this.truckModel = truckModel;
		this.licensePlate = licensePlate;
		this.available = true;
		this.truckID = 2000 + numTrucks++;
		packages = new ArrayList<Package>();
		registerListener();

	}

	/**
	 * Get function for the field 'available'
	 * 
	 * @return available - boolean
	 * 
	 */
	public boolean isAvailable() {
		return available;
	}
	/**
	 * Setter for field - int truckID
	 * 
	 */
	public void setTruckID(int truckID) {
		this.truckID = truckID;
	}
	/**
	 * Setter for field - String licensePlate
	 * 
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	/**
	 * Setter for field- String truckModel
	 * 
	 */
	public void setTruckModel(String truckModel) {
		this.truckModel = truckModel;
	}
	/**
	 * Setter for field - ArrayList<Package> packages
	 * 
	 */
	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}

	/**
	 * Set function for the field 'available'
	 * 
	 * @param available - boolean
	 * 
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * Get function for the field 'timeLeft'
	 * 
	 * @return timeLeft - int
	 * 
	 */
	public int getTimeLeft() {
		return timeLeft;
	}

	/**
	 * Set function for the field 'timeLeft'
	 * 
	 * @param timeLeft - int
	 * 
	 */
	public void setTimeLeft(int timeLeft) {
		if (timeLeft > 0)
			this.timeLeft = timeLeft*3;
	}

	/**
	 * This function reduces timeLeft by 1 if timeLeft is positive (because time
	 * cannon be negative)
	 * 
	 */
	public void reduceTimeLeft() {
		if (timeLeft > 0)
			timeLeft--;
	}

	/**
	 * Get function for the field 'truckID'
	 * 
	 * @return truckID - int
	 * 
	 */
	public int getTruckID() {
		return truckID;
	}

	/**
	 * Get function for the field 'licensePlate'
	 * 
	 * @return licensePlate - String
	 * 
	 */
	public String getLicensePlate() {
		return licensePlate;
	}

	/**
	 * Get function for the field 'truckModel'
	 * 
	 * @return truckModel - String
	 * 
	 */
	public String getTruckModel() {
		return truckModel;
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return "truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel + ", available="
				+ available;
	}

	/**
	 * abstract function for the Node interface, that should return short string
	 * name that represents the instance
	 * 
	 */
	public abstract String getNodeName();

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

		if (!(other instanceof Truck))
			return false;

		return this.truckID == ((Truck) other).truckID;
	}

	/**
	 * Get function for the field 'packages'
	 * 
	 * @return packages - ArrayList<Package>
	 * 
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}

	/**
	 * This function implements 'Observable' interface inspired by Listener-Observer
	 * that we learned in classroom. Function using search method from Display panel
	 * to find matching DrawDrawTruck instance with same ID to store the reference
	 * of DrawDrawTruck instance which represents the DrawTruck in a GUI. It is
	 * important to note that such instance will always exist since the all GUI and
	 * "Pseudo - Backend " objects are initialized exactly in the same order. where
	 * components package represents "pseudo backend" and GUI package represents
	 * frontend
	 * <p>
	 * 
	 * @see Listener
	 * @see Observable
	 * @see DrawTruck
	 * @see DisplayPanel
	 */
	public void registerListener() {
		try {
			this.guiListener = DisplayPanel.getDrawTruckById(truckID);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This function implements 'Observable' interface inspired by Listener-Observer
	 * that we learned in classroom. Function alerts the 'DrawTruck' instance that
	 * something changed to represent the changes in GUI namely in DisplayPanel when
	 * repainting new frame
	 * <p>
	 * 
	 * @see Listener
	 * @see Observable
	 * @see DrawTruck
	 * @see DisplayPanel
	 */
	public void alert() {
		try {
			guiListener.update(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Getter for field 'guiListener'
	 * 
	 * @return guiListener - DrawTruck
	 * @see DrawTruck
	 * 
	 */
	public DrawTruck getGuiListener() {
		return guiListener;
	}

	/**
	 * This method implements Runnable interface. Represents truck work for all
	 * types of trucks as inherited method , runs while there is at least one
	 * package to deliver, then checks the system state ( pause, resume @see
	 * DeliveryGUI) then calls work method which represents one unit of work. then
	 * changes current thread state into 'sleep' for 500ms
	 * <p>
	 * 
	 * @see Pauser
	 */
	public void run() {
		System.out.println(this.getNodeName() + " Is Started Work now! Thread: " + Thread.currentThread().getId());
		while (!MainOffice.isFinished) {
			try {
				DeliveryGUI.pauser.look();
				work();
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				System.out.println("Thread:" + Thread.currentThread().getId() + " Is finished work!");
				return;
			}
		}
		System.out.println(this.getNodeName() + " Is finished work!");
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	/**
	 * This function linking all packages with the same ID in MainOffice instance 
	 * to synchronized the packages after Button "Restore" has been pressed in GUI
	 * 
	 */
	public void linkPackages() {
		for (int i=0;i<this.packages.size() ;i++) {
			for (Package origin : MainOffice.getInstance().getPackages()) {
				if (packages.get(i).equals(origin)) {
					packages.set(i,origin); 
					break;
				}
			}

		}
	}
	
}
