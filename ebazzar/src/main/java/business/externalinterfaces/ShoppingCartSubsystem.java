
package business.externalinterfaces;

import java.util.List;

import business.exceptions.BackendException;

public interface ShoppingCartSubsystem {
	/**
	 * Used during customer login -- when login succeeds, customer profile is
	 * set in shopping cart subsystem
	 */
	public void setCustomerProfile(CustomerProfile custProfile);

	/**
	 * Used during customer login to cache this customer's saved cart --
	 * performs a database access through data access subsystem
	 */
	public void retrieveSavedCart() throws BackendException;

	public void setCartItems(List<CartItem> list);

	public void makeSavedCartLive();

	public ShoppingCart getLiveCart();

	public List<CartItem> getCartItems();

	/**
	 * Accessor used by customer subsystem to store user's selected ship address
	 * during checkout; stores value in shop cart facade
	 */
	public void setShippingAddress(Address addr);

	/**
	 * Accessor used by customer subsystem to store user's selected ship address
	 * during checkout; stores value in shop cart facade
	 */
	public void setBillingAddress(Address addr);

	/**
	 * Accessor used by customer subsystem to store user's selected ship address
	 * during checkout; stores value in shop cart facade
	 */
	public void setPaymentInfo(CreditCard cc);

	/// static methods

	// tests
	public ShoppingCart getEmptyCartForTest();

	public CartItem getEmptyCartItemForTest();

	void saveLiveCart() throws BackendException;

	public DbClassCartItemForTest getGenericDbClassCartItems();

	public CustomerProfile getGenericCustomerProfile();
}
