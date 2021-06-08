package components;

import java.util.ArrayList;
import java.util.Vector;
import GUI.DisplayPanel;
import GUI.DrawBranch;
import GUI.DrawHub;
import GUI.DrawNonStandardTruck;
import GUI.DrawPackage;
import GUI.DrawPath;
import GUI.DrawStandardTruck;
import GUI.DrawTruck;
import GUI.DrawVan;

/**This class contains all of the needed parameters for a successful saving of a state (or 'snapshot') in the Memento DP.
 * <p>
 * An instance contains all of the counters, copies and otherwise needed fields of all the classes in the system that are required to 
 * define a 'state' of the system at any given point in time.
 * <br>
 * Whenever a state is created, the fields are saved in a State object that is later added to a stack in the Caretaker class.
 * <br>
 * When a state is restored, it is an instance of this class that is used to determine what references and field values are to be used in the system after the restore.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 10.6.2021
 * @see Caretaker
 * @see Oriignator
 * 
 */
public class State {
	public int clock;
	public Hub hub;
	public Vector<Package> packages;
	public ArrayList<Customer> customers;
	public int lineNum;
	public ArrayList<DrawBranch> allDrawBranches;
	public ArrayList<DrawPackage> allDrawPackages;
	public ArrayList<DrawTruck> allDrawTrucks;
	public Vector<DrawPath> drawPaths;
	public Integer state;
	public int numTrucks,numOfPackages,numDrawTrucks;
	/**Constructor for the class.
	 * <p>
	 * Scans all the fields of all the elements of a system, and saves the relevant info in the instance of the class.
	 * <br>
	 * the elements include GUI and system elements that are relevant to a state shift; These might include the packages, the clock, the drawn elements and so on..
	 * 
	 */
	public State() {
		lineNum = MainOffice.getInstance().lineNum;
		numTrucks = Truck.numTrucks ;
		numOfPackages = Package.numOfPackages;
		numDrawTrucks = DrawTruck.numTrucks;
		clock = (int) MainOffice.getClock();
		hub = new Hub(MainOffice.getInstance().getHub());
		
		packages = new Vector<Package>();
		for (Package p : MainOffice.getInstance().getPackages()) {
			if (p instanceof SmallPackage)
				packages.add(new SmallPackage(p));
			else if (p instanceof StandardPackage)
				packages.add(new StandardPackage(p));
			else if (p instanceof NonStandardPackage)
				packages.add(new NonStandardPackage(p));
		}
		
		customers = new ArrayList<Customer>();
		for (Customer c : MainOffice.getInstance().getCustomers()) {
			customers.add(new Customer(c));
		}
		
		allDrawBranches = new ArrayList<DrawBranch>();
		for (DrawBranch b : DisplayPanel.getAllBranches()) {
			if( b instanceof DrawHub)
				allDrawBranches.add(b);
			else
				allDrawBranches.add(new DrawBranch(b));
		}
		
		allDrawPackages = new ArrayList<DrawPackage>();
		for (DrawPackage p : DisplayPanel.getAllPackages()) {
			allDrawPackages.add(new DrawPackage(p));
		}
		
		allDrawTrucks = new ArrayList<DrawTruck>();
		for (DrawTruck t : DisplayPanel.getAllTrucks()) {
			if (t instanceof DrawVan)
				allDrawTrucks.add(new DrawVan((DrawVan) t));
			else if (t instanceof DrawStandardTruck)
				allDrawTrucks.add(new DrawStandardTruck((DrawStandardTruck) t));
			else if (t instanceof DrawNonStandardTruck)
				allDrawTrucks.add(new DrawNonStandardTruck((DrawNonStandardTruck) t));
		}
		
		drawPaths = new Vector<DrawPath>(DisplayPanel.getPaths());
		MainOffice.getInstance().CopyTrackingTXT(null);
	}
	
	
}
