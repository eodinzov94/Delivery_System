package components;

import GUI.DeliveryGUI;

public class Customer implements Node, Runnable {
	private static int idCounter = 0;
	private Address add;
	private int id;
	private boolean isFinished = false;
	private int packCreated = 0;

	public Customer(int branchSize) {
		add = new Address(MainOffice.getRand().nextInt(branchSize),
				MainOffice.getRand().nextInt(999999 - 100000) + 100000);
		id = ++idCounter;
	}

	public Customer(Customer other) {
		add = other.add;
		id = other.id;
		packCreated = other.packCreated;
		isFinished = other.isFinished;
	}

	/**
	 * This function creates a new, pseudo-random package and adds it to the system.
	 * <p>
	 * The function generates random parameters for a new package, including the
	 * type, the priority, the addresses etc.. It then adds it both to the main
	 * office's list of packages (for tracking at the end of the system run) and
	 * also adds it to the appropriate branch.
	 * 
	 */
	public void createPackage() {
		int type, priorityId;
		Priority p;
		Package pack = null;
		type = MainOffice.getRand().nextInt(3);
		priorityId = MainOffice.getRand().nextInt(3);
		Address dAdd;
		dAdd = new Address(MainOffice.getRand().nextInt(MainOffice.getInstance().getHub().getBranches().size()),
				MainOffice.getRand().nextInt(999999 - 100000) + 100000);
		switch (priorityId) {
		case 0:
			p = Priority.LOW;
			break;
		case 1:
			p = Priority.STANDARD;
			break;
		case 2:
			p = Priority.HIGH;
			break;
		default:
			p = Priority.LOW;
			break;
		}
		switch (type) {
		case 0:
			pack = new SmallPackage(p, add, dAdd, MainOffice.getRand().nextBoolean(), this);
			break;
		case 1:
			pack = new StandardPackage(p, add, dAdd,
					(MainOffice.getRand().nextInt(9) + 1) + MainOffice.getRand().nextDouble(), this);
			break;
		case 2:
			pack = new NonStandardPackage(p, add, dAdd, (MainOffice.getRand().nextInt(500 - 10) + 10),
					(MainOffice.getRand().nextInt(1000 - 50) + 50), (MainOffice.getRand().nextInt(400 - 100) + 100),
					this);
			break;
		default:
			pack = new SmallPackage(p, add, dAdd, MainOffice.getRand().nextBoolean(), this);
			break;
		}

		if (!(MainOffice.getInstance().getPackages().add(pack)))
			System.out.println("ERROR ADDING PACKAGE IN MAIN OFFICE!");
		MainOffice.getInstance().AssociatePackage(pack);
		packCreated++;
	}

	public void checkFinished() {
		if (MainOffice.getInstance().checkIfAllPackagesDeliveredByCustomerId(id))
			isFinished = true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		System.out.println(getNodeName() + "  started create packages");
		int currentState = MainOffice.getState();
		if(packCreated==5 && isFinished)
			Thread.currentThread().stop();
		while (packCreated < 5) {
			try {
				DeliveryGUI.pauser.look();
				if (currentState != MainOffice.getState()) {
					System.out.println("Thread:"
							+ Thread.currentThread().getId() + "Terminated");
					Thread.currentThread().stop();
				}
				createPackage();
				System.out.println(getNodeName() + "  created package");
				Thread.sleep((MainOffice.getRand().nextInt(4) + 2) * 1000);
			} catch (InterruptedException ignored) {
				Thread.currentThread().stop();
			}
		}
		while (!isFinished) {
			try {
				DeliveryGUI.pauser.look();
				if (currentState != MainOffice.getState()) {
					System.out.println("Thread:"
							+ Thread.currentThread().getId() + "Terminated");
					Thread.currentThread().stop();
				}
				checkFinished();
				System.out.println(getNodeName() + "  waiting for packages to deliver");
				Thread.sleep(5000);
			} catch (InterruptedException ignored) {
				Thread.currentThread().stop();
			}
		}
	}

	public int getCustomerId() {
		return id;
	}

	@Override
	public void collectPackage(Package p) {

	}

	@Override
	public void deliverPackage(Package p) {

	}

	@Override
	public void work() {

	}

	public String getNodeName() {
		return "Sender " + id;
	}
}
