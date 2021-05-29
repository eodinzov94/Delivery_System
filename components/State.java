package components;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import GUI.DisplayPanel;
import GUI.DrawBranch;
import GUI.DrawPackage;
import GUI.DrawPath;
import GUI.DrawTruck;

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
	public State() {
		clock = (int) MainOffice.getClock();
		//hub = (Hub) Hub.getHub().clone();
		packages = (Vector<Package>) MainOffice.getInstance().getPackages().clone();
		customers = (ArrayList<Customer>) MainOffice.getInstance().getCustomers().clone();
		allDrawBranches = (ArrayList<DrawBranch>) DisplayPanel.getAllBranches().clone();
		allDrawPackages = (ArrayList<DrawPackage>) DisplayPanel.getAllPackages().clone();
		allDrawTrucks = (ArrayList<DrawTruck>) DisplayPanel.getAllTrucks().clone();
		drawPaths = (Vector<DrawPath>) DisplayPanel.getPaths().clone();
	}
}
