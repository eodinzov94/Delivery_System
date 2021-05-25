package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class encompasses the logic behind the Create System button featured on the system functionality tray.
 * <p>
 * When called, this class creates a Dialog with several sliders to represent the user's choice for amount of trucks, branches and packages to 
 * be created for the system simulation.
 * <p>
 * When accepted via the 'OK' button, this info is passed back into the system, and is fed into the variables that keep track of the respected 
 * aspects of the system for use by the rest of the components.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 2.0 -- 26.4.2021
 * @see DeliveryGUI
 *
 */
public class CreateSystemDialog implements ActionListener {
	private JDialog createSystem;
	private JFrame frame;
	private JButton bOK,bCancel;
	private JMenuBar choice;
	private JSlider numBranches,numTrucks,numPackages;
	
	/**
	 * Constructor for the Dialog that appears when the 'Create System' button is pressed.
	 * <p>
	 * 
	 */
	public CreateSystemDialog() {
		frame = new JFrame();
		createSystem = new JDialog(frame, "Create post system", true);
		createSystem.setLayout(new GridLayout(8,1));

		// initialize the sliders and buttons for the dialogue by calling helper functions.
		setSliders();
		setButtons();
		
		createSystem.setSize(600, 400);
		createSystem.setVisible(true);
		createSystem.setResizable(false);
		createSystem.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
	}
	
	/**
	 * Helper function to initialize the Sliders used to display different choice options for branches in the Dialog created from this class initialization.
	 * <p>
	 * The function calls for a separate helper function per slider called 'setSlider'.
	 * 
	 * 
	 */
	private void setSliders() {
		createSystem.add(new JLabel("Number of branches", SwingConstants.CENTER));
		numBranches = setSlider(1,10,1);
		createSystem.add(new JLabel("Number of trucks per branch", SwingConstants.CENTER));
		numTrucks = setSlider(1,10,1);
		createSystem.add(new JLabel("Number of packages", SwingConstants.CENTER));
		numPackages = setSlider(2,50,2);
		createSystem.add(new JLabel(""));
		
	}
	/**
	 * Helper function to initialize a custom slider using given parameters.
	 * <p>
	 * 
	 * @param min - integer dictating the starting index for the slider.
	 * @param max - integer dictating the end index for the slider.
	 * @param tickSpacing - integer dictating the jump between 2 indexes in the slider.
	 * @return slider - JSlider object
	 */
	private JSlider setSlider(int min,int max,int tickSpacing ) {
		JSlider slider = new JSlider(min,max);
		slider.setMinorTickSpacing(1);  
		slider.setMajorTickSpacing(tickSpacing);  
		slider.setPaintTicks(true);  
		slider.setPaintLabels(true);
		createSystem.add(slider);
		return slider;
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
		createSystem.add(choice,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		String bName = source.getText();
		if(bName.equals("OK")) {
			DeliveryGUI.getDeliveryGUI().getDisplay().setNumBranches(getNumBranches());
			DeliveryGUI.getDeliveryGUI().getDisplay().setNumTrucks(getNumTrucks());
			DeliveryGUI.getDeliveryGUI().getDisplay().setNumPackages(getNumPackages());
			DeliveryGUI.getDeliveryGUI().getDisplay().createDisplay();
			DeliveryGUI.getDeliveryGUI().setCreated(true);
			createSystem.dispose();
		}
		else {
			createSystem.dispose();
		}

	}
	/**
	 * Get function for the field 'numBranches'
	 * 
	 * @return numBranches - integer (from Slider)
	 * 
	 */
	public int getNumBranches() {
		return numBranches.getValue();
	}
	
	/**
	 * Get function for the field 'numTrucks'
	 * 
	 * @return numTrucks - integer (from Slider)
	 * 
	 */
	public int getNumTrucks() {
		return numTrucks.getValue();
	}
	
	/**
	 * Get function for the field 'numPackages'
	 * 
	 * @return numPackages - integer (from Slider)
	 * 
	 */
	public int getNumPackages() {
		return numPackages.getValue();
	}
}
