package presentation.data;

import java.util.List;
import java.util.stream.Collectors;

import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.usecasecontrol.BrowseAndSelectController;
import business.usecasecontrol.CheckoutController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.gui.GuiConstants;

public enum CheckoutData {
	INSTANCE;
	private CheckoutController controller = new CheckoutController();
	public Address createAddress(String street, String city, String state,
			String zip, boolean isShip, boolean isBill) {
		return CustomerSubsystemFacade.createAddress(street, city, state, zip, isShip, isBill);
	}
	
	public CreditCard createCreditCard(String nameOnCard,String expirationDate,
               String cardNum, String cardType) {
		return CustomerSubsystemFacade.createCreditCard(nameOnCard, expirationDate, 
				cardNum, cardType);
	}
	
	//Customer Ship Address Data
	private ObservableList<CustomerPres> shipAddresses; 
	
	//Customer Bill Address Data
	private ObservableList<CustomerPres> billAddresses; 
	

	
	private void loadShipAddresses() throws BackendException {
		CustomerSubsystem custSS
		  = (CustomerSubsystem)SessionCache.getInstance().get(SessionCache.CUSTOMER);
		List<Address> shippingAddresses = controller.getShippingAddresses(custSS);
		List<CustomerPres> displayableCustList =
				shippingAddresses.stream()
				                 .map(addr -> new CustomerPres(custSS.getCustomerProfile(), addr))
				                 .collect(Collectors.toList());
		shipAddresses =  FXCollections.observableList(displayableCustList);				   
										   
	}
	private void loadBillAddresses() {
		List list = DefaultData.CUSTS_ON_FILE
				   .stream()
				   .filter(cust -> cust.getAddress().isBillingAddress())
				   .collect(Collectors.toList());
		billAddresses = FXCollections.observableList(list);
	}
	public ObservableList<CustomerPres> getCustomerShipAddresses() throws BackendException {
		if(shipAddresses == null) loadShipAddresses();
		return shipAddresses;
	}
	public ObservableList<CustomerPres> getCustomerBillAddresses() throws BackendException {
		if(billAddresses == null) loadBillAddresses();
		return billAddresses;
	}

	public List<String> getDisplayAddressFields() {
		return GuiConstants.DISPLAY_ADDRESS_FIELDS;
	}
	public List<String> getDisplayCredCardFields() {
		return GuiConstants.DISPLAY_CREDIT_CARD_FIELDS;
	}
	public List<String> getCredCardTypes() {
		return GuiConstants.CREDIT_CARD_TYPES;
	}
	public Address getDefaultShippingData() {
		//implement
		List<String> add = DefaultData.DEFAULT_SHIP_DATA;
		return CustomerSubsystemFacade.createAddress(add.get(0), add.get(1), 
				add.get(2), add.get(3), true, false);
	}
	
	public Address getDefaultBillingData() {
		List<String> add =  DefaultData.DEFAULT_BILLING_DATA;
		return CustomerSubsystemFacade.createAddress(add.get(0), add.get(1), 
				add.get(2), add.get(3), false, true);
	}
	
	public List<String> getDefaultPaymentInfo() {
		return DefaultData.DEFAULT_PAYMENT_INFO;
	}
	
	
	public CustomerProfile getCustomerProfile(CustomerSubsystem cust) {
		return cust.getCustomerProfile();
	}
	
		
	
	private class ShipAddressSynchronizer implements Synchronizer {
		public void refresh(ObservableList list) {
			shipAddresses = list;
		}
	}
	public ShipAddressSynchronizer getShipAddressSynchronizer() {
		return new ShipAddressSynchronizer();
	}
	
	private class BillAddressSynchronizer implements Synchronizer {
		public void refresh(ObservableList list) {
			billAddresses = list;
		}
	}
	public BillAddressSynchronizer getBillAddressSynchronizer() {
		return new BillAddressSynchronizer();
	}
	
	public static class ShipBill {
		public boolean isShipping;
		public String label;
		public Synchronizer synch;
		public ShipBill(boolean shipOrBill, String label, Synchronizer synch) {
			this.isShipping = shipOrBill;
			this.label = label;
			this.synch = synch;
		}
		
	}
	
}
