package components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import GUI.DeliveryGUI;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;





/**
 * This class represents the main office in the delivery system.
 * <p>
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 *
 */
public class MainOffice implements Runnable,PropertyChangeListener{
	//Static fields
	private static int state =0;
	public static synchronized int getState() {return state;}
	public static synchronized void setState(int s) {state = s;}
	public static volatile MainOffice instance=null;
	public static boolean isFinished;
	public static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	public static final String filePath = new File("").getAbsolutePath()+"\\src\\components\\tracking.txt";
	public static Random rand = new Random();
	private static int clock = 0;
	//Instance fields
	private final int numOfPackages;
	private Hub hub;
	private Vector<Package> packages;
	private ArrayList<Customer> customers;
	private Executor executor;
	private FileWriter file;
	private PrintWriter writer;
	private BufferedReader reader;
	private int lineNum;
	//Methods or Functions
	public static MainOffice getInstance() {
		if (instance == null) {
            synchronized (MainOffice.class) {
                if (instance == null) {
                    instance = new MainOffice(5,5,50);
                }
            }
        }
        return instance;
	}
	public void setMainOffice(State s) {
		setClock(s.clock);
		setPackages(s.packages);
		setCustomers(s.customers);
		setLineNum(s.lineNum);
		hub.setHub(s.hub);
	}
	/**
	 * Constructor for the class MainOffice. Changed to double-checked singleton in part 3 of the project
	 * <p>
	 * The hub is always created first, afterwards we add 'trucksForBranch' amount
	 * of trucks to it, then a single NonStandardTruck. Afterwards, a 'branches'
	 * amount of branches is created, each with 'trucksForBranch' amount of vans.
	 * 
	 * @param branches        - integer representing the number of branches under
	 *                        the hub.
	 * @param trucksForBranch - integer representing how many trucks each branch
	 *                        (and the hub) will have.
	 * @param numOfPackages - integer representing how many packages will be generated in the simulation
	 */
	private MainOffice(int branches, int trucksForBranch, int numOfPackages) {
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
		for(int i=0; i<10;i++)
			customers.add(new Customer(branches));
		executor = Executors.newFixedThreadPool(2);
	}

	/**
	 * This function starts all threads by using startAllThreads() method
	 *  that representing Trucks/Branches/Hub.. etc
	 * then simulates our whole system after Button "Start" in GUI was pressed.
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
		((ExecutorService) executor).shutdown();
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
		for(Package p:packages)
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
	 * This function checks if system can finish the work 
	 * (if all packages has statuses Delivered and if all packages generated)
	 * <p>
	 * 
	 * 
	 * @return true if all packages generated & delivered, false otherwise
	 */
	private boolean checkIfFinished() {
		for (Package p : packages) {
			if (!(p.getStatus().equals(Status.DELIVERED)) || packages.size()< this.numOfPackages) {
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
		for (Branch b:hub.getBranches()) {
			b.startAllTrucks();
		}
		startCustomers();
		hub.startAllBranches();
		hub.startAllTrucks();
	}
	public void resetThreads() {
		resetCustomers();
		for (Branch b:hub.getBranches()) {
			b.startAllTrucks();
		}
		startCustomers();
		hub.startAllBranches();
		hub.startAllTrucks();
	}
	public void stopCustomers() {
		((ExecutorService) executor).shutdownNow();
		while(!((ExecutorService) executor).isTerminated()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * This method implements Runnable interface. 
	 * just used to run a play() method in Thread.
	 */
	public void run() {
		play();
	}
	public void startCustomers() {
		for (Customer c: customers){
			executor.execute(c);
		}
	}
	public void resetCustomers() {
		executor = Executors.newFixedThreadPool(2);
		startCustomers();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Package p = (Package)evt.getSource();
		rwl.writeLock().lock();
		writer.println(lineNum++ +":   "+"Sender ID: " +p.customerId+ " Package:"+ p.getPackageID() + " Status: " + p.getStatus());
		writer.flush();
		rwl.writeLock().unlock();
	}
	public boolean checkIfAllPackagesDeliveredByCustomerId(Integer customerId) {
		int deliveredCounter =0;
		try {
			reader = new BufferedReader (new FileReader(filePath));
		} catch (FileNotFoundException e1) {
		}
		rwl.readLock().lock();
		String line = "";
		try {
			while(deliveredCounter<5 && (line=reader.readLine()) !=null) {
				if(checkIfDeliveredInStringByCustomerId(line,"Sender ID: "+customerId.toString()))
					deliveredCounter++;
			}
		} catch (IOException e) {
		}
		rwl.readLock().unlock();
		try {
			reader.close();
		} catch (IOException e) {
		}
		return deliveredCounter == 5;
	}
	public boolean checkIfDeliveredInStringByCustomerId(String line, String customer) {
		if(!line.contains(customer) || !line.contains("DELIVERED"))
			return false;
		return true;
		
	}
	public ArrayList<Customer> getCustomers() {
		return customers;
	}
	public int getLineNum() {
		return lineNum;
	}
	public Hub getHub() {
		return hub;
	}
	public void setHub(Hub hub) {
		this.hub = hub;
	}
	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	public static void setClock(int clock) {
		MainOffice.clock = clock;
	}
	public void stopThreads() {
		stopCustomers();
		for (Branch b:hub.getBranches()) {
			b.stopAllTrucks();
		}
		hub.stopAllTrucks();
		hub.stopAllBranches();
	}
	
}
