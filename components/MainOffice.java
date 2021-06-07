package components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import GUI.DeliveryGUI;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * This class represents the main office in the delivery system.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 *
 */
public class MainOffice implements Runnable, PropertyChangeListener {
	// Static fields
	private static int state = 0;

	/**Helper function to receive the current active state in the system. 
	 * 
	 * @return state - current active state
	 */
	public static synchronized int getState() {
		return state;
	}

	/**Helper function to set the current active state to a new one.
	 * 
	 * @param s - number of state that's currently taking over.
	 */
	public static synchronized void setState(int s) {
		state = s;
	}

	public static volatile MainOffice instance = null;
	public static boolean isFinished;
	public static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	public static final String filePath = new File("").getAbsolutePath() + "\\src\\components\\tracking.txt";
	public static final String filePathCopy = new File("").getAbsolutePath() + "\\src\\components\\trackingCopy.txt";
	public static Random rand = new Random();
	private static int clock = 0;
	// Instance fields
	private final int numOfPackages;
	private Hub hub;
	private Vector<Package> packages;
	private ArrayList<Customer> customers;
	private Executor executor;
	private FileWriter file;
	private PrintWriter writer;
	private BufferedReader reader;
	int lineNum;

	// Methods or Functions
	/**Helper function to receive the MainOffice instance.
	 *<p> 
	 * As of version 3.0, MainOffice is a Singleton, therefore at most 1 instance of it is active
	 * in a system at any given time.
	 * 
	 * @return instance - the main office instance.
	 */
	public static MainOffice getInstance() {
		if (instance == null) {
			synchronized (MainOffice.class) {
				if (instance == null) {
					instance = new MainOffice(5, 5, 50);
				}
			}
		}
		return instance;
	}

	/**Helper function to help set the MainOffice instance using a given state.
	 * <p>
	 * Whenever a state restore happens, this function is called with a given State instance containing all the
	 * needed references to reset the system to the previous state.
	 * <br>
	 * This function calls all the helper 'set...' functions in the other classes to restore the system to the 
	 * previous state
	 * 
	 * @param s - State instance containing references.
	 */
	public void setMainOffice(State s) {
		restoreTrackingTXT();
		setClock(s.clock);
		setPackages(s.packages);
		setCustomers(s.customers);
		lineNum = s.lineNum;
		hub.setHub(s.hub);
	}
	/**
	 * Constructor for the class MainOffice. Changed to double-checked singleton in
	 * part 3 of the project
	 * <p>
	 * The hub is always created first, afterwards we add 'trucksForBranch' amount
	 * of trucks to it, then a single NonStandardTruck. Afterwards, a 'branches'
	 * amount of branches is created, each with 'trucksForBranch' amount of vans.
	 * 
	 * @param branches        - integer representing the number of branches under
	 *                        the hub.
	 * @param trucksForBranch - integer representing how many trucks each branch
	 *                        (and the hub) will have.
	 * @param numOfPackages   - integer representing how many packages will be
	 *                        generated in the simulation
	 */
	private MainOffice(int branches, int trucksForBranch, int numOfPackages) {
		try {
			Files.delete(new File(filePath).toPath());
		} catch (IOException e1) {
		}
		Branch.numBranch = -1;
		hub = new Hub();
		hub.resetHub();
		lineNum = 1;
		try {
			file = new FileWriter(filePath);
			writer = new PrintWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		isFinished = false;
		this.numOfPackages = numOfPackages;

		hub.addTruck("NonStandardTruck");
		for (int i = 0; i < trucksForBranch; i++) {
			hub.addTruck("StandardTruck");
		}
		System.out.println();
		for (int i = 0; i < branches; i++) {
			Branch b = new Branch();
			hub.addBranch(b);
			for (int j = 0; j < trucksForBranch; j++) {
				b.addTruck("Van");
			}
			System.out.println();
		}
		packages = new Vector<Package>();
		customers = new ArrayList<Customer>();
		for (int i = 0; i < 10; i++)
			customers.add(new Customer(branches));
		executor = Executors.newFixedThreadPool(2);
	}

	/**
	 * This function starts all threads by using startAllThreads() method that
	 * representing Trucks/Branches/Hub.. etc then simulates our whole system after
	 * Button "Start" in GUI was pressed.
	 * <p>
	 * Using the parameter 'playTime', we activate the function tick to simulate
	 * both activity in the system (in the form of the function work() ) and to
	 * create new packages. At the end of the simulation, we use printReport() to
	 * print all the packages and their statuses.
	 * 
	 * @param playTime - integer representing how long the system works.
	 * 
	 */
	public void play() {
		isFinished = false;
		System.out.println("========================== START INIT==========================");
		startAllThreads();
		System.out.println("========================== START SIMULATION==========================");
		while (!checkIfFinished() || packages.size() == 0) {
			DeliveryGUI.pauser.look();
			tick();
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
			}
		}
		isFinished = true;
		((ExecutorService) executor).shutdownNow();
		DeliveryGUI.getDeliveryGUI().getDisplay().run();
		System.out.println("========================== STOP ==========================");
		this.printReport();
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * The function prints all the packages we created and their statuses. It is
	 * used by the function play()
	 * 
	 */
	public void printReport() {
		for (Package p : packages) {
			System.out.println();
			p.printTracking();
			System.out.println();
		}
	}

	/**
	 * Standard function to represent the internal clock as a String.
	 * 
	 * @return String representation of the system clock in a MM:SS format.
	 * 
	 */
	public String clockString() {
		Integer m, s;
		m = clock / 60;
		s = clock % 60;
		String clockStr;
		clockStr = (m > 9) ? m.toString() : "0" + m.toString();

		clockStr += ':';

		clockStr += (s > 9) ? s.toString() : "0" + s.toString();

		System.out.println(clockStr);
		return clockStr;
	}

	/**
	 * Standard function to represent class as a String.
	 * 
	 * @return String representation of class.
	 * 
	 */
	@Override
	public String toString() {
		return "MainOffice";
	}

	/**
	 * Get function for the field 'clock'
	 * 
	 * @return clock - Integer
	 * 
	 */
	public static int getClock() {
		return clock;
	}

	/**
	 * Get function for the field 'rand'
	 * 
	 * @return rand - Random
	 * 
	 */
	public static Random getRand() {
		return rand;
	}

	/**
	 * Get function for the field 'packages'
	 * 
	 * @return packages - ArrayList<Package>
	 * 
	 */
	public Vector<Package> getPackages() {
		return packages;
	}

	/**
	 * Set function for the field 'packages'
	 * 
	 * @param packages - ArrayList<Package> object.
	 * 
	 */
	public void setPackages(Vector<Package> packages) {
		this.packages = packages;
		for (Package p : this.packages)
			p.registerListener();
	}

	/**
	 * This function handles all the creation and activation of elements in the
	 * system.
	 * <p>
	 * This function handles the system's clock, advancing it and printing it to the
	 * UI. Also, every 5 clock ticks the function creates a new random package to be
	 * added to the system, thus 'feeding' the system new packages. Furthermore, the
	 * function calls both the hub's and its branches' work() function, thus
	 * activating all the elements in the system.
	 * 
	 */
	public void tick() {
		clockString();
		clock++;
	}

	/**
	 * This function associates a given package to the correct branch.
	 * <p>
	 * The function, given a package, adds it either to the hub if it's nonstandard,
	 * or to the appropriate branch otherwise.
	 * 
	 * @param p - Package, can be of any type.
	 */
	public void AssociatePackage(Package p) {
		if (p instanceof NonStandardPackage)
			hub.collectPackage(p);
		else
			for (Branch b : hub.getBranches())
				if (b.getBranchId() == p.getSenderAddress().getZip()) {
					b.collectPackage(p);
					break;
				}
	}

	/**
	 * This function checks if system can finish the work (if all packages has
	 * statuses Delivered and if all packages generated)
	 * <p>
	 * 
	 * 
	 * @return true if all packages generated & delivered, false otherwise
	 */
	private boolean checkIfFinished() {
		for (Package p : packages) {
			if (!(p.getStatus().equals(Status.DELIVERED)) || packages.size() < this.numOfPackages) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Starter for all threads that representing different object in delivery system
	 * like branches/trucks/hub ... etc
	 * <p>
	 */
	public void startAllThreads() {
		DeliveryGUI.getDeliveryGUI().startDisplayThread();
		for (Branch b : hub.getBranches()) {
			b.startAllTrucks();
		}
		startCustomers();
		hub.startAllBranches();
		hub.startAllTrucks();
	}

	/**Helper function to restart all the threads belonging to different elements in the system. 
	 * 
	 */
	public void resetThreads() {
		resetCustomers();
		for (Branch b : hub.getBranches()) {
			b.startAllTrucks();
		}
		startCustomers();
		hub.startAllBranches();
		hub.startAllTrucks();
	}

	/**Helper function to stop the Customer threads in the system. 
	 * 
	 */
	public void stopCustomers() {
		((ExecutorService) executor).shutdownNow();
	}

	/**
	 * This method implements Runnable interface. just used to run a play() method
	 * in Thread.
	 */
	public void run() {
		play();
	}

	/**Helper function to start the Customer threads using an ExecutorService.
	 * 
	 */
	public void startCustomers() {
		for (Customer c : customers) {
			executor.execute(c);
		}
	}

	/**Helper function to reset the threads for the customers using a new Executor thread pool.
	 * 
	 */
	public void resetCustomers() {
		executor = Executors.newFixedThreadPool(2);
		startCustomers();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String status = evt.getPropertyName();
		Package p = (Package) evt.getSource();
		rwl.writeLock().lock();
		writer.println(lineNum++ + ":   " + "Sender ID: " + p.customerId + " Package:" + p.getPackageID() + " Status: "
				+ status);
		writer.flush();
		rwl.writeLock().unlock();
	}

	/**This function receives a customer ID, and scans the list of packages to check if the customers' packages have arrived at their destination.
	 * 
	 * @param customerId - ID of the customer to check
	 * @return true if the customer is finished waiting, false otherwise.
	 */
	public boolean checkIfAllPackagesDeliveredByCustomerId(Integer customerId) {
		int deliveredCounter = 0;
		rwl.readLock().lock();
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e1) {
		}
		String line = "";
		try {
			while (deliveredCounter < 5 && (line = reader.readLine()) != null) {
				if (checkIfDeliveredInStringByCustomerId(line, "Sender ID: " + customerId.toString()))
					deliveredCounter++;
			}
		} catch (IOException e) {
		}

		try {
			reader.close();
		} catch (IOException e) {
			return deliveredCounter == 5;
		}
		rwl.readLock().unlock();
		return deliveredCounter == 5;
	}

	/**Helper function that uses a read line from the tracking file and a customer's ID to see if the current line
	 * represents a delivered package belonging to the customer.
	 * 
	 * @param line - line from the tracking file.
	 * @param customer - customers' ID in string format
	 * @return true if the line represents a package belonging to the customer that has been delivered, false otherwise.
	 */
	public boolean checkIfDeliveredInStringByCustomerId(String line, String customer) {
		if (!line.contains(customer) || !line.contains("DELIVERED"))
			return false;
		return true;

	}

	/**
	 * Get function for the field 'customers'
	 * 
	 * @return customers - ArrayList<Customer>
	 * 
	 */
	public ArrayList<Customer> getCustomers() {
		return customers;
	}
	/**
	 * Get function for the field 'hub'
	 * 
	 * @return hub - Hub
	 * 
	 */
	public Hub getHub() {
		return hub;
	}
	
	/**
	 * Set function for the field 'hub'
	 * 
	 * @param hub - Hub object.
	 * 
	 */
	public void setHub(Hub hub) {
		this.hub = hub;
	}

	/**
	 * Set function for the field 'customers'
	 * 
	 * @param customers - ArrayList<Customer> object.
	 * 
	 */
	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}

	/**
	 * Set function for the field 'clock'
	 * 
	 * @param clock - int object.
	 * 
	 */
	public static void setClock(int clock) {
		MainOffice.clock = clock;
	}

	/**Helper function that calls for all the threads in the system to stop.
	 * <p>
	 * Primarily used when the 'Stop' button is called, or when a state change is imminent.
	 * 
	 */
	public void stopThreads() {
		stopCustomers();
		for (Branch b : hub.getBranches()) {
			b.stopAllTrucks();
		}
		hub.stopAllTrucks();
		hub.stopAllBranches();
	}

	/**Restores the Tracking.txt file when a state change occurs.
	 * <p>
	 * We use 2 paths to keep a Tracking file, one for the up-to-date and currently read file,
	 * and one for the copy of the previous one used when we restore a state.
	 * 
	 */
	private void restoreTrackingTXT() {
		File dest = new File(MainOffice.filePath);
		File source = new File(MainOffice.filePathCopy);
		rwl.writeLock().lock();
		try {
			if (writer != null)
				writer.close();
			Files.delete(dest.toPath());
			Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rwl.writeLock().unlock();
		try {
			file = new FileWriter(filePath, true);
			writer = new PrintWriter(file);
		} catch (IOException e) {
		}
	}

	/**Copies the Tracking.txt file to be saved for when a state change will be requested.
	 * <p>
	 * We use 2 paths to keep a Tracking file, one for the up-to-date and currently read file,
	 * and one for the copy of the previous one used when we restore a state.
	 * 
	 */
	public File CopyTrackingTXT(String destination) {
		try {
			Thread.sleep(150);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File source = new File(MainOffice.filePath);
		File dest = null;
		if(destination != null)
			 dest = new File(destination);
		else dest = new File(MainOffice.filePathCopy);
		rwl.writeLock().lock();
		try {
			
			Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rwl.writeLock().unlock();
		return dest;

	}

	/**Get function for the field 'rwl'
	 * 
	 * @return rwl - ReentrantReadWriteLock object.
	 */
	public static ReentrantReadWriteLock getRwl() {
		return rwl;
	}
}
