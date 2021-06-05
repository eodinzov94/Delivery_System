	package GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import components.MainOffice;
import components.State;

/**
 * This class represents the panel on the GUI where the Delivery System is displayed in real time. 
 * <p>
 * A panel instance contains ArrayLists and Vectors for the GUI branches, trucks, paths and packages that are drawn on the screen.
 * <p>
 * The panel handles UI updates by repainting all the components it contains every interval set by the system.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DeliveryGUI 
 *
 */
public class DisplayPanel extends JLayeredPane implements Runnable {

	private static final long serialVersionUID = 1L;
	private boolean isHidden;
	int numBranches = 6, numPackages = 50, numTrucks = 5;
	static private ArrayList<DrawBranch> allBranches;
	static private ArrayList<DrawPackage> allPackages;
	static private ArrayList<DrawTruck> allTrucks;
	static private Vector<DrawPath> paths;

	/** Helper function to add a path to the Vector of paths.
	 * 
	 * @param path - DrawPath object to add to the Vector.
	 */
	public static void addPath(DrawPath path) {
		synchronized (paths) {
			paths.add(path);
		}
	}

	/**
	 * Constructor function for the panel.
	 * 
	 */
	public DisplayPanel() {
		super();
		isHidden = true;
		setBackground(Color.white);
		setBorder(BorderFactory.createLoweredBevelBorder());
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1), getBorder()));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.darkGray, Color.lightGray));
	}
	
	/** 
	 * Helper function to initialize all the Drawable objects contained in the display
	 * <p>
	 * Initializes the objects using the given parameters extracted from the user input in the Create System dialog.
	 * After initialization, displays the new objects onto the screen.
	 */
	public void createDisplay() {
		paths = new Vector<DrawPath>();
		initTrucks();
		initBranches();
		initPackages();
		this.isHidden = false;
		repaint();
	}
	
	/**
	 * Helper function to initialize the ArrayList of packages.
	 * <p>
	 * Creates new Drawable components dynamically using a formula devised for proportional alignment of drawings.
	 */
	private void initPackages() {
		allPackages = new ArrayList<DrawPackage>();

		for (int i = 0; i < numPackages; i++) {
			Point location = new Point(100 + i * 33, 15);
			allPackages.add(new DrawPackage(location));
		}
	}

	/**
	 * Helper function to initialize the ArrayList of trucks.
	 * <p>
	 * Uses the input for 'num trucks' to create Drawable truck components of varying types.
	 */
	private void initTrucks() {
		allTrucks = new ArrayList<DrawTruck>();
		allTrucks.add(new DrawNonStandardTruck());
		for (int j = 0; j < numTrucks; j++) {
			allTrucks.add(new DrawStandardTruck());
		}
		for (int i = 1; i <= numBranches; i++)
			for (int j = 0; j < numTrucks; j++) {
				allTrucks.add(new DrawVan());
			}
	}

	/**
	 * Helper function to initialize the ArrayList of branches.
	 * <p>
	 * Creates new Drawable components dynamically using a formula devised for proportional alignment of drawings.
	 */
	private void initBranches() {
		allBranches = new ArrayList<DrawBranch>();
		allBranches.add(new DrawHub(-1));
		for (int i = 0; i < numBranches; i++) {
			Point start = new Point(30, 150 + i * branchesStep());
			Point pEnd = new Point(1800, 310 + i * hubLinesStep());
			Point pStart = new Point(70, 165 + i * branchesStep());
			DrawPath dp = new DrawPath(pStart, pEnd, DrawHub.getHubColor());
			DrawBranch db = new DrawBranch(start, dp, i);
			allBranches.add(db);
			if(i>4) {
				dp.isHidden = true;
				db.isHidden = true;
			}
		}
	}

	/**
	 * Small function to invoke paintComponent for every object in the Branches ArrayList
	 * @param g - Graphics object.
	 * @see Graphics
	 */
	private void drawBranches(Graphics g) {
		for (DrawBranch b : allBranches) {
			b.paintComponent(g);
		}

	}

/**
	 * Small function to invoke paintComponent for every object in the Paths Vector
	 * @param g - Graphics object.
	 * @see Graphics
	 */
	private void drawPaths(Graphics g) {
		synchronized (paths) {
			for (DrawPath p : paths) {
				p.paintComponent(g);
			}
		}
	}

	/**
	 * Small function to invoke paintComponent for every object in the Trucks ArrayList
	 * @param g - Graphics object.
	 * @see Graphics
	 */
	private void drawTrucks(Graphics g) {
		for (DrawTruck t : allTrucks) {
			t.paintComponent(g);
		}
	}

	/**
	 * Small function to invoke paintComponent for every object in the Packages ArrayList
	 * @param g - Graphics object.
	 * @see Graphics
	 */
	private void drawPackages(Graphics g) {
		for (DrawPackage p : allPackages) {
			p.paintComponent(g);
		}

	}

	/**
	 * This function contains the dynamic horizontal space interval between Branches formula.
	 * <p>
	 * This function works best with the size specified in the PDF (1200x700) and might result in unintended behavior on other
	 * aspects.
	 */
	public int branchesStep() {
		if (numBranches > 1)
			return 450 /numBranches-1;
		return 450;
	}

	/**
	 * This function contains the dynamic horizontal space interval between Packages formula.
	 * <p>
	 * This function works best with the size specified in the PDF (1200x700) and might result in unintended behavior on other
	 * aspects.
	 */
	public int packagesStep() {
		return 950 / (40);
	}

	/**
	 * This function contains the dynamic horizontal space interval between Hub lines formula.
	 * <p>
	 * This function works best with the size specified in the PDF (1200x700) and might result in unintended behavior on other
	 * aspects.
	 */
	public int hubLinesStep() {
		if (numBranches > 1)
			return 160 / (numBranches-1);
		return 160;
	}
	/**
	 * Main function draw every Object in display panel, uses help function to draw.
	 * @param g - Graphics object.
	 * @see Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isHidden)
			return;
		drawBranches(g);
		drawPaths(g);
		drawPackages(g);
		if (!MainOffice.isFinished)
			drawTrucks(g);
	}

	/**
	 * Set function for the field 'numBranches'
	 * 
	 * @param numBranches - int
	 * 
	 */
	public void setNumBranches(int numBranches) {
		this.numBranches = numBranches;
	}

	/**
	 * Get function for the field 'numBranches'
	 * 
	 * @return numBranches - int
	 * 
	 */
	public int getNumBranches() {
		return numBranches;
	}
	
		/**
	 * Get function for the field 'numPackages'
	 * 
	 * @return numPackages - int
	 * 
	 */
	public int getNumPackages() {
		return numPackages;
	}
	
	/**
	 * Set function for the field 'numPackages'
	 * 
	 * @param numPackages - int
	 * 
	 */
	public void setNumPackages(int numPackages) {
		this.numPackages = numPackages;
	}

	/**
	 * Get function for the field 'numTrucks'
	 * 
	 * @return numTrucks - int
	 * 
	 */
	public int getNumTrucks() {
		return numTrucks;
	}

	/**
	 * Set function for the field 'numTrucks'
	 * 
	 * @param numTrucks - int
	 * 
	 */
	public void setNumTrucks(int numTrucks) {
		this.numTrucks = numTrucks;
	}

	/**
	 * Get function for the field 'paths'
	 * 
	 * @return paths - Vector<DrawPath>
	 * 
	 */
	public static Vector<DrawPath> getPaths() {
		synchronized (paths) {
			return paths;
		}
	}
	
	/**This function receives an id and searches (and returns) a DrawTruck object whose ID matches the specification.
	 * <p>
	 * In case the ID doesn't match any ID, an exception is thrown.
	 * 
	 * @param id - integer matching the desired DrawTrucks' object ID
	 * @return t - DrawTruck object whose id matches the parameter
	 * @throws Exception
	 */
	public static DrawTruck getDrawTruckById(int id) throws Exception {
		for (DrawTruck t : allTrucks)
			if (t.getTruckId() == id)
				return t;
		throw new Exception("Matching truck not found");
	}

	/**This function receives an id and searches (and returns) a DrawPackage object whose ID matches the specification.
	 * <p>
	 * In case the ID doesn't match any ID, an exception is thrown.
	 * 
	 * @param id - integer matching the desired DrawPackage' object ID
	 * @return t - DrawPackage object whose id matches the parameter
	 * @throws Exception
	 */
	public static DrawPackage getDrawPackageById(int id) throws Exception {
		for (DrawPackage p : allPackages)
			if (p.getPackageID() == id)
				return p;
		throw new Exception("Matching package not found");
	}

	/**This function receives an id and searches (and returns) a DrawBranch object whose ID matches the specification.
	 * <p>
	 * In case the ID doesn't match any ID, an exception is thrown.
	 * 
	 * @param id - integer matching the desired DrawBranch' object ID
	 * @return t - DrawBranch object whose id matches the parameter
	 * @throws Exception
	 */
	public static DrawBranch getDrawBranchById(int id) throws Exception {
		for (DrawBranch b : allBranches)
			if (b.getBranchId() == id)
				return b;
		throw new Exception("Matching branch not found");
	}

	/**
	 * This method implements Runnable interface. Represents display panel 
	 * Redrawing all object every 500MS
	 *
	 */
	public void run() {
		while (!MainOffice.isFinished) {
			DeliveryGUI.pauser.look(); // Check the pauser to see if the program hasn't been suspended
			repaint();// repaint the panel.
			try {
				Thread.sleep(500L); // timeout for half a second.
			} catch (InterruptedException e) {
			}
		}
		repaint(); // repaint at the end of the program run, for the final update.

	}
	/**
	 * Getter for field allBranches
	 * @return allBranches - ArrayList<DrawBranch>
	 *
	 */
	public static ArrayList<DrawBranch> getAllBranches() {
		return allBranches;
	}
	/**
	 * Getter for field allPackages
	 * @return allPackages - ArrayList<DrawPackage>
	 *
	 */
	public static ArrayList<DrawPackage> getAllPackages() {
		return allPackages;
	}
	/**
	 * Getter for field allTrucks
	 * @return allTrucks - ArrayList<DrawTruck>
	 *
	 */
	public static ArrayList<DrawTruck> getAllTrucks() {
		return allTrucks;
	}
	/**
	 * Setter - for the previous memento state, restores all the draw objects states from memento (State s)
	 *
	 */
	public  void setState(State s) {
		allBranches = s.allDrawBranches;
		allPackages = s.allDrawPackages;
		allTrucks = s.allDrawTrucks;
		paths = s.drawPaths;
		repaint();
	}

	
	
}
