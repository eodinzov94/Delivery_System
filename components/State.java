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
	public int numTrucks,numOfPackages,numDrawTrucks;
	public State() {
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
	}
}
