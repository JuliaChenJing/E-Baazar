package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import middleware.exceptions.DatabaseException;
import presentation.util.CacheReader;

public class ShoppingCartSubsystemFacade implements ShoppingCartSubsystem {
	// the shopping cart that is used by the customer
	// a shopping cart has several CartItems
	ShoppingCartImpl liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
	// the shopping cart that is saved before by a logged in customer
	ShoppingCartImpl savedCart;

	Integer shopCartId;
	// why is here a customer profile? because one shopping cart will definitely have a customerProfile
	CustomerProfile customerProfile;

	Logger log = Logger.getLogger(this.getClass().getPackage().getName());

	// interface methods
	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}

	public void makeSavedCartLive() {
		liveCart = savedCart;
	}

	public ShoppingCart getLiveCart() {
		return liveCart;
	}

	public void retrieveSavedCart() throws BackendException {

		try {
			DbClassShoppingCart dbClass = new DbClassShoppingCart();
			//from Subsystem to DBClass
			ShoppingCartImpl cartFound = dbClass.retrieveSavedCart(customerProfile);
			if (cartFound == null) {

				// set savedCart to an instance of ShoppingCartImpl
				// need to create a cartitems list (with at least one element)
				// and insert into shop cart impl
				savedCart = new ShoppingCartImpl(new ArrayList<CartItem>());
			} else {

				// popluate this list
				savedCart = cartFound;
			}
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}

	}

	@Override
	public void setShippingAddress(Address addr) {
		liveCart.setShipAddress(addr);

	}

	@Override
	public void setBillingAddress(Address addr) {
		liveCart.setBillAddress(addr);

	}

	@Override
	public void setPaymentInfo(CreditCard cc) {
		liveCart.setPaymentInfo(cc);

	}

	public void setCartItems(List<CartItem> list) {
		liveCart.setCartItems(list);
	}

	public List<CartItem> getCartItems() {
		return liveCart.getCartItems();
	}

	// static methods
	public static CartItem createCartItem(String productName, String quantity, String totalprice) {
		try {
			return new CartItemImpl(productName, quantity, totalprice);
		} catch (BackendException e) {
			throw new RuntimeException("Can't create a cartitem because of productid lookup: " + e.getMessage());
		}
	}

	public static CartItem createCartItem(Integer shoppingcartid, Integer productid, Integer cartitemid, String quantity,
			String totalprice, boolean alreadySaved) {
		try {
			return new CartItemImpl(shoppingcartid, productid, cartitemid, quantity, totalprice, alreadySaved);
		} catch (BackendException e) {
			throw new RuntimeException("Can't create a cartitem because of productid lookup: " + e.getMessage());
		}
	}

	// interface methods for testing

	public ShoppingCart getEmptyCartForTest() {
		return new ShoppingCartImpl();
	}

	public CartItem getEmptyCartItemForTest() {
		return new CartItemImpl();
	}

	@Override
	public void saveLiveCart() throws BackendException {
		// TODO Auto-generated method stub
		// implement
		// System.out.println("testing..."+customerProfile.getCustId());
		customerProfile = CacheReader.readCustomer().getCustomerProfile();
		CustomerSubsystem cust = CacheReader.readCustomer();
		liveCart.setBillAddress(cust.getDefaultBillingAddress());
		liveCart.setShipAddress(cust.getDefaultShippingAddress());
		liveCart.setPaymentInfo(cust.getDefaultPaymentInfo());
		DbClassShoppingCart dbShoppingCart = new DbClassShoppingCart();
		try {
			dbShoppingCart.saveCart(customerProfile, liveCart);
		} catch (DatabaseException e) {
			e.printStackTrace();
			throw new BackendException(e);
		}
	}

	@Override
	public DbClassCartItemForTest getGenericDbClassCartItems() {
		// TODO Auto-generated method stub
		return new  DbClassShoppingCart();
	}




}
