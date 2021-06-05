package GUI;


import java.util.Vector;


import components.MainOffice;
import components.Package;
/**
 * This class contains logic for sorting the packages info into matrices, as used by the 'All Packages' and 'Branch Info' buttons featured on the system functionality tray.
 * <p>
 *  When called, this class contains info about packages depending on the method called. It is capable of 
 *  returning the info of all the packages in the system, or otherwise a specific branch whose ID is provided.
 *  <p>
 *  Both methods are static, so there's no inherit need for class instantiation. 
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DeliveryGUI
 *
 */
public class AllPackagesData{
	
	public static final int numColumn = 5;
	public static final String column[] = {"Package ID", "Sender", "Destination","Priority","Status"};
	public static final int idIndex = 0,senderIndex=1,destinationIndex=2,priorityIndex=3,statusIndex=4;
	public static final int hubIndex = -1;
	
	/**
	 * This function gathers all the packages that exist within the system, and returns their info as of the moment the function was called as a String matrix.
	 * <p>
	 * 
	 * @return data - String matrix containing the info for all the packages.
	 */
	public static String[][] setDataForAllPackages() {
		
		//receive system information
		Vector<Package> packages = MainOffice.getInstance().getPackages();
		int numPackages = packages.size();
		Package temp;
		String[][] data = new String[numPackages][];
		
		//initialize matrix 'data' using temporary Package variable 'temp'
		for(int i =0; i<numPackages;i++) {
			temp = packages.get(i);
			data[i] = new String[numColumn];
			data[i][idIndex] = String.valueOf(temp.getPackageID());
			data[i][senderIndex] = String.valueOf(temp.getSenderAddress().getZip()+1) + "-" + String.valueOf(temp.getSenderAddress().getStreet());
			data[i][destinationIndex] = String.valueOf(temp.getDestinationAddress().getZip()+1) + "-" + String.valueOf(temp.getDestinationAddress().getStreet());
			data[i][priorityIndex] = temp.getPriority().toString();
			data[i][statusIndex] = temp.getStatus().toString();
		}
		return data;
	}
	
	/**
	 * This function gathers the packages belonging to a specified branch (or hub) within the system, and returns their info as of the moment the function was called as a String matrix.
	 * <p>
	 * 
	 * @param branchId - integer representing the ID of the branch we're looking for. ( -1 for Hub)
	 * @return data - String matrix containing the info for all the packages.
	 */
	public static String[][] setDataForBranchPackages(int branchId) {
		
		//receive system information
		Vector<Package> packages;
		if(branchId == hubIndex)
			packages= MainOffice.getInstance().getHub().getListPackages();
		else
			packages = MainOffice.getInstance().getHub().getBranches().get(branchId).getListPackages();
		int numPackages = packages.size();
		Package temp;
		String[][] data = new String[numPackages][];
		
		//initialize matrix 'data' using temporary Package variable 'temp'
		for(int i =0; i<numPackages;i++) {
			temp = packages.get(i);
			data[i] = new String[numColumn];
			data[i][idIndex] = String.valueOf(temp.getPackageID());
			data[i][senderIndex] = String.valueOf(temp.getSenderAddress().getZip()+1) + "-" + String.valueOf(temp.getSenderAddress().getStreet());
			data[i][destinationIndex] = String.valueOf(temp.getDestinationAddress().getZip()+1) + "-" + String.valueOf(temp.getDestinationAddress().getStreet());
			data[i][priorityIndex] = temp.getPriority().toString();
			data[i][statusIndex] = temp.getStatus().toString();
		}
		return data;
	}
}
