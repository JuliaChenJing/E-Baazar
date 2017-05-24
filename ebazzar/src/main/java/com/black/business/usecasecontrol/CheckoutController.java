package com.black.business.usecasecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.black.business.BusinessConstants;
import com.black.business.SessionCache;
import com.black.business.externalinterfaces.Address;
import com.black.business.externalinterfaces.CreditCard;
import com.black.business.externalinterfaces.CustomerProfile;
import com.black.business.externalinterfaces.CustomerSubsystem;
import com.black.business.externalinterfaces.ShoppingCartSubsystem;
import com.black.business.exceptions.BackendException;
import com.black.business.exceptions.BusinessException;
import com.black.business.exceptions.RuleException;

import org.springframework.stereotype.Service;

@Service
public enum CheckoutController  {
	INSTANCE;
	
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	
	
	public void runShoppingCartRules() throws RuleException, BusinessException {
		//implement
		
	}
	public List<Address> getShippingAddresses(CustomerProfile custProf) throws BackendException {
		//implement
		LOG.warning("Method CheckoutController.getShippingAddresses has not been implemented");
		return new ArrayList<Address>();
	}
	
	//implement
//	public Address getDefaultShippingAddress(CustomerProfile custProf) throws BackendException {
//		
//	}
//	
//	public Address getDefaultBillingAddress(CustomerProfile custProf) throws BackendException {
//		
//	}
	
	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		//implement
	}
	
	public Address runAddressRules(Address addr) throws RuleException, BusinessException {
		CustomerSubsystem cust = 
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		return cust.runAddressRules(addr);
	}
	
	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		//implement
	}
	
	/** Asks Customer Subsystem to check credit card against 
	 *  Credit Verification System 
	 */
	public void verifyCreditCard() throws BusinessException {
		//implement
	}
	
	public void saveNewAddress(Address addr) throws BackendException {
		CustomerSubsystem cust = 
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);			
		cust.saveNewAddress(addr);
	}
	
	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder() throws BackendException {
		//implement
	}


}
