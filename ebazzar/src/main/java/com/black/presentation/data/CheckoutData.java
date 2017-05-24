package com.black.presentation.data;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.black.presentation.gui.GuiConstants;
import com.black.business.BusinessConstants;
import com.black.business.SessionCache;
import com.black.business.customersubsystem.CustomerSubsystemFacade;
import com.black.business.exceptions.BackendException;
import com.black.business.externalinterfaces.Address;
import com.black.business.externalinterfaces.CreditCard;
import com.black.business.externalinterfaces.CustomerProfile;
import com.black.business.externalinterfaces.CustomerSubsystem;
import com.black.business.usecasecontrol.BrowseAndSelectController;
import com.black.business.usecasecontrol.CheckoutController;
import org.springframework.stereotype.Repository;

@Repository
public enum CheckoutData {
	INSTANCE;
	
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
	
	
	/**
	 * Precondition: Customer has logged in
	 */
	private void loadShipAddresses() throws BackendException {
		CustomerProfile custProf 
		  = ((CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER)).getCustomerProfile();
		List<Address> shippingAddresses = CheckoutController.INSTANCE.getShippingAddresses(custProf);
		List<CustomerPres> displayableCustList =
				shippingAddresses.stream()
				                 .map(addr -> new CustomerPres(custProf, addr))
				                 .collect(Collectors.toList());
		shipAddresses =  FXCollections.observableList(displayableCustList);				   
										   
	}
	
	/**
	 * Precondition: Customer has logged in
	 */
	private void loadBillAddresses() throws BackendException  {
		CustomerProfile custProf
                = ((CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER)).getCustomerProfile();
		List<Address> billingAddresses = CheckoutController.INSTANCE.getShippingAddresses(custProf);
		List<CustomerPres> displayableCustList =
				billingAddresses.stream()
				                .map(addr -> new CustomerPres(custProf, addr))
				                .collect(Collectors.toList());
		billAddresses = FXCollections.observableList(displayableCustList);		
	}
	public ObservableList<CustomerPres> getCustomerShipAddresses() throws BackendException {
		if(shipAddresses == null) {
			loadShipAddresses();
		}
		return shipAddresses;
	}
	public ObservableList<CustomerPres> getCustomerBillAddresses() throws BackendException {
		if(billAddresses == null) {
			loadBillAddresses();
		}
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
	
	
	
	public List<String> getDefaultPaymentInfo() {
		return DefaultData.DEFAULT_PAYMENT_INFO;
	}
	
	
	public CustomerProfile getCustomerProfile() {
		return BrowseAndSelectController.INSTANCE.getCustomerProfile();
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
