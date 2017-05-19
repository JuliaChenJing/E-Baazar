package business.usecasecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import business.BusinessConstants;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.rulesbeans.PaymentBean;
import presentation.data.SessionCache;
import rulesengine.OperatingException;
import rulesengine.ReteWrapper;
import rulesengine.ValidationException;

import static presentation.util.UtilForUIClasses.pathToRules;


public class CheckoutController  {
		
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	
	
	public void runShoppingCartRules(ShoppingCartSubsystem shopCart) throws RuleException, BusinessException {
		//implement
		
	}
	
	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {

		try {
			// set up
			String moduleName = "rules-payment";
			BufferedReader rulesReader =pathToRules(getClass().getClassLoader(), "payment-rules.clp");

			String deftemplateName = "payment-template";
			PaymentBean addrbean = new PaymentBean(addr,cc);
			HashMap<String, PaymentBean> h = new HashMap<>();
			h.put(deftemplateName, addrbean);

			// start up the rules engine
			ReteWrapper engine = new ReteWrapper();
			engine.setRulesAsString(rulesReader);
			engine.setCurrentModule(moduleName);
			engine.setTable(h);
			engine.runRules();
			System.out.println(engine.getUpdates());
			//return engine.getUpdates();
		} catch (ValidationException ex) {
			throw new RuleException(ex.getMessage());
		} catch (IOException ex) {
			throw new RuleException(ex.getMessage());
		} catch (OperatingException ex) {
			throw new RuleException(ex.getMessage());
		} catch (Exception ex) {
			throw new RuleException(ex.getMessage());
		}
	}

	public Address runAddressRules(CustomerSubsystem cust, Address addr) throws RuleException, BusinessException {
		return cust.runAddressRules(addr);
	}
	
	public List<Address> getShippingAddresses(CustomerSubsystem cust) throws BackendException {
		return cust.getAllShipAddresses();
	}
	
	public List<Address> getBillingAddresses(CustomerSubsystem cust) throws BackendException {
		return cust.getAllShipAddresses();
	}
	
	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		//implement
	}
	
	/** Asks Customer Subsystem to check credit card against 
	 *  Credit Verification System 
	 */
	public void verifyCreditCard(CustomerSubsystem cust) throws BusinessException {
		//implement
	}
	
	public void saveNewAddress(CustomerSubsystem cust, Address addr) throws BackendException {		
		cust.saveNewAddress(addr);
	}
	
	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder() throws BackendException {
		//implement
	}


}
