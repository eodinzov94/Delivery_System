package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import components.MainOffice;
/**
 * This class encompasses the logic behind the 'Branch Info' button featured on the system functionality tray.
 * <p>
 * When called, this class instance creates a Dialog that enables the user to choose a branch from the available list created for the system.
 * Once a choice is received, the Dialog will display all the available information for the branch as of the time of request.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 2.0 -- 26.4.2021
 * @see DeliveryGUI
 *
 */
public class BranchInfoDialog implements ActionListener{
	private JDialog infoDialog;
	private JFrame frame;
	private JButton bOK,bCancel;
	private JMenuBar choice;
	private JComboBox<String> branchChoices;
	
	/**
	 * Constructor for the Dialog window showing the different options of branches to choose between.
	 * <p>
	 */
	public BranchInfoDialog() {
		frame = new JFrame();
		infoDialog = new JDialog(frame, "Branch Info", true);
		infoDialog.setLayout(new GridLayout(2,1));
		
		// initialize the combo box and buttons for the dialogue by calling helper functions.
		setJComboBox();
		setButtons();
		
		infoDialog.setSize(265, 115);
		infoDialog.setVisible(true);
		infoDialog.setResizable(false);
		infoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
	}
	
	/**
	 * Helper function to initialize the 'OK' and 'Cancel' buttons in the Dialog created from this class initialization.
	 * <p>
	 */
	private void setButtons() {
		choice = new JMenuBar();
		choice.setBorder(BorderFactory.createCompoundBorder(
    			BorderFactory.createTitledBorder(""), 
    			BorderFactory.createEmptyBorder(5,5,5,5)));
		choice.setLayout(new GridLayout(1,2,5,0));
		bOK = new JButton("OK");
		bCancel = new JButton("Cancel");
		bOK.addActionListener(this);
		bCancel.addActionListener(this);
		choice.add(bOK);
		choice.add(bCancel);
		infoDialog.add(choice,BorderLayout.SOUTH);
	}
	
	/**
	 * Helper function to initialize the Combo Box used to display different choice options for branches in the Dialog created from this class initialization.
	 * <p>
	 */
	private void setJComboBox() {
		branchChoices = new JComboBox<String>();
		branchChoices.addItem("Sorting center");
		int numBranches = MainOffice.getInstance().getHub().getBranches().size();
		for(int i=0;i<numBranches ;i++) 
			branchChoices.addItem("Branch "+ (i+1));
		infoDialog.add(branchChoices);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		String bName = source.getText();
		if(bName.equals("OK")) { // When the 'OK' button is called, we display the appropriate info for the selected branch in an Info Table and dispose of this Dialog.
			DeliveryGUI.getDeliveryGUI().createBranchInfoTable(branchChoices.getSelectedIndex()-1);
			DeliveryGUI.getDeliveryGUI().setClickedBranchInfo(true);
			infoDialog.dispose();
		}
		else { // Cancel button disposes of this Dialog with no additional actions.
			infoDialog.dispose();
		}
		
	}
}
