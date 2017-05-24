package com.black.business.customersubsystem;

import java.util.ArrayList;
import java.util.List;

import com.black.business.externalinterfaces.*;
import com.black.business.shoppingcartsubsystem.DbClassShoppingCart;
import com.black.middleware.creditverifcation.CreditVerificationFacade;
import com.black.middleware.exceptions.DatabaseException;
import com.black.business.exceptions.BackendException;
import com.black.business.exceptions.BusinessException;
import com.black.business.exceptions.RuleException;
import com.black.business.ordersubsystem.OrderSubsystemFacade;
import com.black.business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import com.black.middleware.exceptions.MiddlewareException;
import com.black.middleware.externalinterfaces.CreditVerification;
import com.black.middleware.externalinterfaces.CreditVerificationProfile;
import com.black.presentation.data.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerSubsystemFacade implements CustomerSubsystem {
	@Autowired
	ShoppingCartSubsystem shoppingCartSubsystem;

	@Autowired
	OrderSubsystem orderSubsystem;

	@Autowired
	DbClassCustomerProfile dbClassCustomerProfile;

	@Autowired
	DbClassAddress dbClassAddress;

	@Autowired
	DbClassShoppingCart dbClassShoppingCart;

	List<Order> orderHistory;
	AddressImpl defaultShipAddress;
	AddressImpl defaultBillAddress;
	CreditCardImpl defaultPaymentInfo;
	CustomerProfileImpl customerProfile;
	//test


	/** Use for loading order history,
	 * default addresses, default payment info,
	 * saved shopping cart,cust profile
	 * after login*/
	public void initializeCustomer(Integer id, int authorizationLevel)
			throws BackendException {
		boolean isAdmin = (authorizationLevel >= 1);
		loadCustomerProfile(id, isAdmin);
		loadDefaultShipAddress();
		loadDefaultBillAddress();
		loadDefaultPaymentInfo();

		shoppingCartSubsystem.setCustomerProfile(customerProfile);
		shoppingCartSubsystem.retrieveSavedCart();

		orderSubsystem.setCustomerProfile(customerProfile);
		loadOrderData();
	}

	void loadCustomerProfile(int id, boolean isAdmin) throws BackendException {
		try {
			customerProfile = dbClassCustomerProfile.readCustomerProfile(id);
			customerProfile.setIsAdmin(isAdmin);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}
	void loadDefaultShipAddress() throws BackendException {
		//implement
		try {
			defaultShipAddress = (AddressImpl)dbClassAddress.readDefaultShipAddress(customerProfile);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	void loadDefaultBillAddress() throws BackendException {
		try {
			defaultBillAddress = (AddressImpl)dbClassAddress.readDefaultBillAddress(customerProfile);

		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void loadDefaultPaymentInfo() throws BackendException {
		try {
			defaultPaymentInfo = (CreditCardImpl) dbClassAddress.readDefaultCreditCardInfo(customerProfile);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	void loadOrderData() throws BackendException {

		// retrieve the order history for the customer and store here
		//orderSubsystem = new OrderSubsystemFacade(customerProfile);
		System.out.println("order data is loading ...");
		orderHistory = new ArrayList<>();
		orderHistory = orderSubsystem.getOrderHistory();


	}
	/**
	 * Customer Subsystem is responsible for obtaining all the data needed by
	 * Credit Verif system -- it does not (and should not) rely on the
	 * controller for this data.
	 */
	@Override
	public void checkCreditCard() throws BusinessException {
		List<CartItem> items = shoppingCartSubsystem.getCartItems();
		ShoppingCart theCart = shoppingCartSubsystem.getLiveCart();
		Address billAddr = theCart.getBillingAddress();
		CreditCard cc = theCart.getPaymentInfo();
		double amount = DataUtil.computeTotal(items);
		CreditVerification creditVerif = new CreditVerificationFacade();
		try {
			CreditVerificationProfile profile = CreditVerificationFacade.getCreditProfileShell();
			profile.setFirstName(customerProfile.getFirstName());
			profile.setLastName(customerProfile.getLastName());
			profile.setAmount(amount);
			profile.setStreet(billAddr.getStreet());
			profile.setCity(billAddr.getCity());
			profile.setState(billAddr.getState());
			profile.setZip(billAddr.getZip());
			profile.setCardNum(cc.getCardNum());
			profile.setExpirationDate(cc.getExpirationDate());
			creditVerif.checkCreditCard(profile);
		} catch (MiddlewareException e) {
			throw new BusinessException(e);
		}
	}
	/**
	 * Returns true if user has admin access
	 */
	public boolean isAdmin() {
		return customerProfile.isAdmin();
	}



	/**
	 * Use for saving an address created by user
	 */
	public void saveNewAddress(Address addr) throws BackendException {
		dbClassAddress.saveAddress(customerProfile, addr);
	}

	public CustomerProfile getCustomerProfile() {

		return customerProfile;
	}

	public Address getDefaultShippingAddress() {
		return defaultShipAddress;
	}

	public Address getDefaultBillingAddress() {
		return defaultBillAddress;
	}
	public CreditCard getDefaultPaymentInfo() {
		return defaultPaymentInfo;
	}


	/**
	 * Use to supply all stored addresses of a customer when he wishes to select an
	 * address in ship/bill window
	 */
	public List<Address> getAllAddresses() throws BackendException {
		try {
			List<Address> list = dbClassAddress.readAllAddresses(customerProfile);

			return dbClassAddress.readAllAddresses(customerProfile);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;



	}

	public Address runAddressRules(Address addr) throws RuleException,
			BusinessException {

		Rules transferObject = new RulesAddress(addr);
		transferObject.runRules();

		// updates are in the form of a List; 0th object is the necessary
		// Address
		AddressImpl update = (AddressImpl) transferObject.getUpdates().get(0);
		return update;
	}

	public void runPaymentRules(Address addr, CreditCard cc)
			throws RuleException, BusinessException {
		Rules transferObject = new RulesPayment(addr, cc);
		transferObject.runRules();
	}


	public static Address createAddress(String street, String city,
										String state, String zip, boolean isShip, boolean isBill) {
		return new AddressImpl(street, city, state, zip, isShip, isBill);
	}

	public static CustomerProfile createCustProfile(Integer custid,
													String firstName, String lastName, boolean isAdmin) {
		return new CustomerProfileImpl(custid, firstName, lastName, isAdmin);
	}

	public static CreditCard createCreditCard(String nameOnCard,
											  String expirationDate, String cardNum, String cardType) {
		return new CreditCardImpl(nameOnCard, expirationDate, cardNum, cardType);
	}

	@Override
	public List<Order> getOrderHistory() {

		return orderHistory;
	}

	@Override
	public void setShippingAddressInCart(Address addr) {
		defaultShipAddress = (AddressImpl) addr;
	}

	@Override
	public void setBillingAddressInCart(Address addr) {
		defaultBillAddress = (AddressImpl)addr;

	}

	@Override
	public void setPaymentInfoInCart(CreditCard cc) {
		defaultPaymentInfo = (CreditCardImpl) cc;


	}

	@Override
	public void submitOrder() throws BackendException {
		ShoppingCart sc = shoppingCartSubsystem.getLiveCart();
		try {
			orderSubsystem.submitOrder(sc);
		} catch (DatabaseException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void refreshAfterSubmit() throws BackendException {

		loadOrderData();

	}

	@Override
	public ShoppingCartSubsystem getShoppingCart() {

		return shoppingCartSubsystem;
	}

	@Override
	public void saveShoppingCart() throws  BackendException {
		// TODO Auto-generated method stub
		//check with the person responsible for DbClassShoppingCart(). Constructor should be public.
		//	DbClassShoppingCart dbClassShoppingcart = new DbClassShoppingCart();
		try {
			dbClassShoppingCart.saveCart(customerProfile, shoppingCartSubsystem.getLiveCart());
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Override
	public DbClassAddressForTest getGenericDbClassAddress() {
		// TODO Auto-generated method stub
		DbClassAddress dbclass = new DbClassAddress();
		return dbclass;
	}


	@Override
	public CustomerProfile getGenericCustomerProfile() {

		return new CustomerProfileImpl(1, "FirstTest", "LastTest");
		//return customerProfile;
	}

}
