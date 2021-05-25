package components;
/**
 * This <strong>enum</strong> represents a package status for any type packages.
 * <p>
 * <strong style="color:blue;">CREATION :</strong> customer just placed request to collect the package from him.
 * <p>
 * <strong style="color:blue;">COLLECTION :</strong> package just collected from the sender, and on the way to the local branch.
 * <p>
 * <strong style="color:blue;">BRANCH_STORAGE :</strong> package arrived to the local branch & waiting for the standard truck to collect it.
 * <p>
 * <strong style="color:blue;">HUB_TRANSPORT :</strong> package collected from the local branch and it is on the way to Hub storage.
 * <p>
 * <strong style="color:blue;">HUB_STORAGE :</strong> package arrived to the Hub storage & waiting for the standard truck to deliver it to destination branch.
 * <p>
 * <strong style="color:blue;">BRANCH_TRANSPORT :</strong>package collected from Hub storage and on it's way to destination branch.
 * <p>
 * <strong style="color:blue;">DELIVERY :</strong> package arrived to destination branch & waiting for Van to collect it.
 * <p>
 * <strong style="color:blue;">DISTRIBUTION :</strong> package collected by Van and on it's way to the customer.
 * <p>
 * <strong style="color:blue;">DELIVERED :</strong> package finally delivered to destination.
 * 
 * @author Ron Vayner 315431346 & Evgeny Odinzov 328667217
 * @version 1.0 -- 29.3.2021
 *
 *
 */
public enum Status {
	CREATION,
	COLLECTION, 
	BRANCH_STORAGE, 
	HUB_TRANSPORT, 
	HUB_STORAGE, 
	BRANCH_TRANSPORT, 
	DELIVERY, 
	DISTRIBUTION,
	DELIVERED;
}
