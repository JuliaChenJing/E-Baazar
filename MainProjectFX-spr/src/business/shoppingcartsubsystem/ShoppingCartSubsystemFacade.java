package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Rules;
//import business.externalinterfaces.RuleException;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.usecasecontrol.CheckoutController;
import middleware.exceptions.DatabaseException;
import presentation.util.CacheReader;

public class ShoppingCartSubsystemFacade implements ShoppingCartSubsystem {

	ShoppingCartImpl liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
	ShoppingCartImpl savedCart;
	Integer shopCartId;
	CustomerProfile customerProfile;
	Logger LOG = Logger.getLogger(this.getClass().getPackage().getName());
	CartItem cartItem;
	List<CartItem> cartItemList = new ArrayList<CartItem>();

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
			ShoppingCartImpl cartFound = dbClass.retrieveSavedCart(customerProfile);
			if (cartFound == null) {
				savedCart = new ShoppingCartImpl(new ArrayList<CartItem>());
			} else {
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

	// interface methods for testing

	public ShoppingCart getEmptyCartForTest() {
		return new ShoppingCartImpl();
	}

	public CartItem getEmptyCartItemForTest() {
		return new CartItemImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see business.externalinterfaces.ShoppingCartSubsystem#clearLiveCart()
	 */
	@Override
	public void clearLiveCart() {
		//implemented by Efrem
		liveCart.clearCart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see business.externalinterfaces.ShoppingCartSubsystem#getLiveCartItems()
	 */
	@Override
	public List<CartItem> getLiveCartItems() throws BusinessException {
		//Implemented by Efrem
		return liveCart.getCartItems();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * business.externalinterfaces.ShoppingCartSubsystem#runShoppingCartRules()
	 */
	@Override
	public void saveLiveCart() throws BackendException {
		// TODO Auto-generated method stub
		//implement
		//System.out.println("testing..."+customerProfile.getCustId());
		customerProfile = CacheReader.readCustomer().getCustomerProfile();
		CustomerSubsystem cust = CacheReader.readCustomer();
		liveCart.setBillAddress(cust.getDefaultBillingAddress());
		liveCart.setShipAddress(cust.getDefaultShippingAddress());
		liveCart.setPaymentInfo(cust.getDefaultPaymentInfo());
		DbClassShoppingCart dbShoppingCart = new DbClassShoppingCart();
		try {
			dbShoppingCart.saveCart(customerProfile, liveCart);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BackendException(e);
		}
	}

	@Override
	public void runShoppingCartRules() throws business.exceptions.RuleException, BusinessException {
	//Implemented by Efrem
		
		Rules shoppingRuleObject = new RulesShoppingCart(getLiveCart());
		shoppingRuleObject.runRules();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * business.externalinterfaces.ShoppingCartSubsystem#runFinalOrderRules()
	 */
	@Override
	public void runFinalOrderRules() throws business.exceptions.RuleException, BusinessException {
		//Impelemented by Efrem
		Rules shoppingFinalOrderRules= new RulesFinalOrder(getLiveCart());
		shoppingFinalOrderRules.runRules();

	}

}