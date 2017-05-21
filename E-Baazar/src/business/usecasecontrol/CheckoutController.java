package business.usecasecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import business.BusinessConstants;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.*;
import business.rulesbeans.FinalOrderBean;
import business.rulesbeans.PaymentBean;
import business.rulesbeans.ShopCartBean;
import rulesengine.OperatingException;
import rulesengine.ReteWrapper;
import rulesengine.ValidationException;
import presentation.util.CacheReader.*;
import business.externalinterfaces.OrderItem ;

import static presentation.util.CacheReader.readCustomer;
import static presentation.util.UtilForUIClasses.pathToRules;


public class CheckoutController  {
		
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	
	
	public void runShoppingCartRules(ShoppingCartSubsystem shopCart) throws RuleException, BusinessException {
		//implement
		try {

			ShoppingCart scc= shopCart.getLiveCart();
			// set up
			String moduleName = "rules-shopcart";
			BufferedReader rulesReader =pathToRules(getClass().getClassLoader(), "shopcart-rules.clp");

			String deftemplateName = "shopcart-template";
			ShopCartBean shopbean = new ShopCartBean(scc);
			HashMap<String, ShopCartBean> h = new HashMap<>();
			h.put(deftemplateName, shopbean);

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

	//return an Address
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
		try {
			// set up
			ShoppingCart ssc = scss.getLiveCart();
			String moduleName = "rules-finalOrder";
			BufferedReader rulesReader = pathToRules(getClass().getClassLoader(), "finalorder-rules.clp");

			String deftemplateName = "finalOrder-template";
			FinalOrderBean finalorderbean = new FinalOrderBean(ssc);
			HashMap<String, FinalOrderBean> h = new HashMap<>();
			h.put(deftemplateName, finalorderbean);

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
