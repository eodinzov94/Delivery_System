package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;


import javax.swing.*;

import components.MainOffice;
import components.Originator;
import components.Pauser;

/**
 * This class instantiates the frame for the GUI, as well as encompasses all the other components of the User Interface. 
 * <p>
 * The class is accessed as a Singleton, as only one DeliveryGUI exists per system.
 * <p>
 * When called for the first time, this class creates a frame in which lie all the other components of the UI, such as the 'Create System' button or the panel that displays
 * the running of the system.
 * <p>
 * It also contains needed objects and logics to connect the 'back-end' with the 'front-end' such as an instance of MainOffice and the thread for it.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 3.0 -- 05.06.2021
 * @see DisplayPanel 
 * @see MainOffice
 *
 */
public class DeliveryGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static Pauser pauser = new Pauser();
	static private DeliveryGUI frame = null;
	private Thread gameThread = null;
	private boolean isGameStarted = false;
	private boolean isClickedBranchInfo = false;
	private boolean isCreated = false;
	private JMenuBar mainMenu;
	private DisplayPanel display;
	private Thread displayThread;
	private JScrollPane tableContainerAllPackages;
	private JScrollPane tableContainerBranchPackages;
	private boolean isClickedPackageInfo = false;
	private int numOfBranches;

	/**
	 * This function returns a DeliveryGUI object- a new one if none exist, or the current object if one was initialized already.
	 * <p>
	 * @return frame - the DeliveryGUI singleton 
	 */
	static public DeliveryGUI getDeliveryGUI() {
		if (frame == null)
			frame = new DeliveryGUI();
		return frame;
	}

	/**
	 * Constructor for the class MainOffice, is only called by getDeliveryGUI().
	 * <p>
	 * The function creates the JFrame and MenuBar for it.
	 */
	private DeliveryGUI() {
		super("Post tracking system");
		this.setSize(1900, 1000);
		this.setVisible(true);
		this.setResizable(false);
		display = new DisplayPanel();
		this.add(display);
		
		// create the MenuBar by calling a helper function.
		setMenuBar();
		
		displayThread = new Thread(display);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		revalidate();
		numOfBranches = 5;
	}

	/**
	 * Helper function, creates the MenuBar for the system frame, utilizes the 'setNewMenuItem' helper function to create each menu item separately.
	 * <p>
	 */
	private void setMenuBar() {
		mainMenu = new JMenuBar();
		mainMenu.setLayout(new GridLayout(1, 6));
		mainMenu.setBounds(0, 635, 1200, 30);
		this.add(mainMenu, BorderLayout.SOUTH);
		setNewMenuItem("Create system");
		setNewMenuItem("Start");
		setNewMenuItem("Stop");
		setNewMenuItem("Resume");
		setNewMenuItem("All packages info");
		setNewMenuItem("Branch info");
		setNewMenuItem("Clone branch");
		setNewMenuItem("Restore");
		setNewMenuItem("Report");
	}

	/**
	 * Helper function, initializes a JButton using a given String, adds a listener and attaches it to the systems' menu.
	 * <p>
	 * @param name - String to represent the text on the button.
	 */
	private JButton setNewMenuItem(String name) {
		JButton item = new JButton(name);
		item.addActionListener(this);
		mainMenu.add(item);
		return item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		String bName = source.getText();
		if (bName.equals("Create system") && !isCreated) { // 'Create System' was selected, initialize a new CreateSystemDialog object.
			getDisplay().createDisplay();
			setCreated(true);
		} else if (bName.equals("Start") && isCreated && !isGameStarted) {  // 'Start' was selected, if a system was created- start the system simulation.
			this.startApp();
			try {
				Thread.sleep(80);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			printThreadsInfo();
		} else if (isGameStarted != true) {
			return;
		} else if (bName.equals("Stop")) { // 'Stop' was selected, suspend all threads.
			stopAllThreads();
		} else if (bName.equals("Resume")) { // 'Resume' was selected, resume all threads.
			resumeAllThreads();
		} else if (bName.equals("All packages info")) { // 'All packages info' was selected, display/hide all the packages info in this current time.
			if (!isClickedPackageInfo) {
				createPackagesInfoTable();
				isClickedPackageInfo = true;
			} else {
				isClickedPackageInfo = false;
				tableContainerAllPackages.setVisible(false);
				deleteTableContainerAllPackages();
			}
		} else if (bName.equals("Branch info")) { // 'Branch info' was selected, display/hide the info for the selected branch in this current time.
			if (!isClickedBranchInfo) {
				new BranchInfoDialog();
			} else {
				isClickedBranchInfo = false;
				tableContainerBranchPackages.setVisible(false);
				deleteTableContainerBranchPackages();
			}

		}
		else if(bName.equals("Report")) {
			try {
				Desktop.getDesktop().edit(MainOffice.getInstance().CopyTrackingTXT(new File("").getAbsolutePath() + "\\src\\components\\report.txt"));
			} catch (IOException e1) {
			}
		}
		else if(bName.equals("Clone branch") && numOfBranches < 6) {//"Clone branch" was selected, pauses the system, creating system copy, adding new branch, starting the system again
			stopAllThreads();
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e1) {
			}
			Originator.createState();
			numOfBranches++;
			new CloneDialog();
			resumeAllThreads();
			System.out.println("-------------------------Clone---------------------");
			try {
				Thread.sleep(100);
				printThreadsInfo();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		else if(bName.equals("Restore")&& numOfBranches >5) {
			System.out.println("-------------------------RESTORE---------------------");//"Restore" was selected , pauses the system , setting Memento from previous state , starting the system again 
			stopAllThreads();
			MainOffice.getInstance().stopThreads();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			MainOffice.setState(MainOffice.getState() + 1);
			Originator.setState();
			numOfBranches--;
			MainOffice.getInstance().resetThreads();
			resumeAllThreads();
			try {
				Thread.sleep(100);
				printThreadsInfo();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	

	/**
	 * Helper function, creates an info table to display all the packages info on the screen.
	 * <p>
	 * Utilizes the 'setDataForAllPackages' function in the AllPackagesData class to retrieve a matrix containing the packages info.
	 * @see AllPackagesData
	 */ 
	private void createPackagesInfoTable() {

		JTable packagesTable = new JTable(AllPackagesData.setDataForAllPackages(), AllPackagesData.column);
		tableContainerAllPackages = new JScrollPane(packagesTable);
		tableContainerAllPackages.setBounds(0, 0, 500, 23 + 16 * MainOffice.getInstance().getPackages().size());
		display.add(tableContainerAllPackages, JLayeredPane.POPUP_LAYER);
	}
	
	/**
	 * Helper function, deletes the current info table from the display.
	 * @see DisplayPanel
	 */ 
	private void deleteTableContainerAllPackages() {
		display.remove(tableContainerAllPackages);
	}
	
	/**
	 * Helper function, deletes the current info table from the display.
	 * @see DisplayPanel
	 */ 
	private void deleteTableContainerBranchPackages() {
		display.remove(tableContainerBranchPackages);
	}
	
	/**
	 * Helper function, creates an info table to display the packages of a specific branch on the screen.
	 * <p>
	 * Utilizes the 'setDataForBranchPackages' function in the AllPackagesData class to retrieve a matrix containing the packages info.
	 * @see AllPackagesData
	 */ 
	public void createBranchInfoTable(int branchId) {
		JTable packagesTable = new JTable(AllPackagesData.setDataForBranchPackages(branchId), AllPackagesData.column);
		tableContainerBranchPackages = new JScrollPane(packagesTable);
		int step;
		if(branchId == -1) // hub was selected
			step =23+ 16* MainOffice.getInstance().getHub().getListPackages().size();
		else { // a branch was selected
			step =23+ 16* MainOffice.getInstance().getHub().getBranches().get(branchId).getListPackages().size();
		}
		tableContainerBranchPackages.setBounds(500, 0, 500, step);
		display.add(tableContainerBranchPackages, JLayeredPane.POPUP_LAYER);
	}

	/**
	 * Initializer function, creates a new MainOffice instance using the given parameters after the 'Create System' dialogue.
	 * <p>
	 * Initializes a thread to run the MainOffice instance, and calls it to run.
	 * @see MainOffice
	 */ 
	public void startApp() {
		gameThread = new Thread(MainOffice.getInstance());
		gameThread.start();
		isGameStarted = true;
	}

	/**
	 * Get function for the field 'display'
	 * 
	 * @return display - DisplayPanel
	 * 
	 */
	public DisplayPanel getDisplay() {
		return display;
	}

	/**
	 * Helper function to start the displayThread thread.
	 * 
	 */
	public void startDisplayThread() {
		displayThread.start();
	}

	/**
	 * Helper function to pause all the running threads in the system.
	 * <p>
	 * The system utilizes a Pauser from the Pauser class that acts as a monitor for all the threads in the system.
	 * @see Pauser
	 */
	public void stopAllThreads() {
		pauser.pause();

	}
	/**
	 * Helper function to resume all the running threads in the system.
	 * <p>
	 * The system utilizes a Pauser from the Pauser class that acts as a monitor for all the threads in the system.
	 * @see Pauser
	 */
	public void resumeAllThreads() {
		pauser.resume();
	}
	/**
	 * Get function for the field 'isCreated'
	 * 
	 * @return isCreated - boolean
	 * 
	 */
	public boolean isCreated() {
		return isCreated;
	}

	/**
	 * Set function for the field 'isCreated'
	 * 
	 * @param isCreated - boolean
	 * 
	 */
	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	/**
	 * Get function for the field 'isClickedBranchInfo'
	 * 
	 * @return isClickedBranchInfo - boolean
	 * 
	 */
	public boolean isClickedBranchInfo() {
		return isClickedBranchInfo;
	}

	/**
	 * Set function for the field 'isClickedBranchInfo'
	 * 
	 * @param isClickedBranchInfo - boolean
	 * 
	 */
	public void setClickedBranchInfo(boolean isClickedBranchInfo) {
		this.isClickedBranchInfo = isClickedBranchInfo;
	}
	/**
	 * Helper function print amount of Threads running in system.
	 */
	public void printThreadsInfo() {
		System.out.println("--------Thread Info------Active Threads:"+Thread.activeCount());
	}
}
