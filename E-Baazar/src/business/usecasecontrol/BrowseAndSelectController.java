package business.usecasecontrol;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import business.RulesQuantity;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.rulesbeans.QuantityBean;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.DataUtil;
import static presentation.util.UtilForUIClasses.pathToRules;

import rulesengine.OperatingException;
import rulesengine.ReteWrapper;
import rulesengine.ValidationException;

public class BrowseAndSelectController {
	
	public void updateShoppingCartItems(ShoppingCartSubsystem shopCartSs, List<CartItem> cartitems) {
		shopCartSs.setCartItems(cartitems);
	}

	
	/** Response to user request to see saved shopping cart items
	 *  This is accomplished by making the saved cart, which was retrieved at login,
	 *  the new live cart. It should not be possible to make this call
	 *  if customer has not logged in. 
	 */
	public void retrieveSavedCart(ShoppingCartSubsystem shopCartSS, boolean custIsLoggedIn) {
		// Saved cart was retrieved during login
		if(custIsLoggedIn)
			shopCartSS.makeSavedCartLive();	
	}
	
	
	 public ShoppingCartSubsystem obtainCurrentShoppingCartSubsystem(CustomerSubsystem cust, 
             ShoppingCartSubsystem cachedCartSS) {
        if (cust == null) {
        	if(cachedCartSS == null) {
        		return new ShoppingCartSubsystemFacade();
        	} else {
        		return cachedCartSS;
        	}
        } else { 
            return cust.getShoppingCart();
        }
    }

	public void runQuantityRules(Product product, String quantityRequested)
			throws RuleException, BusinessException {
	
		
		
		//since product was first loaded into UI
		ProductSubsystem prodSS = new ProductSubsystemFacade();
		//find current quantity available since quantity may have changed
		int currentQuantityAvail = prodSS.readQuantityAvailable(product);
		
		//method one, it works
	    //Rules transferObject = new RulesQuantity(currentQuantityAvail, quantityRequested);
		//transferObject.runRules();
		
		
		//method two, it works as well
        try {
        	
            // set up
            String moduleName = "rules-quantity";
            BufferedReader rulesReader =pathToRules(getClass().getClassLoader(), "quantity-rules.clp");

            String deftemplateName = "quantity-template";
            QuantityBean quanbean = new QuantityBean(quantityRequested,currentQuantityAvail);
            HashMap<String, QuantityBean> h = new HashMap<>();
            h.put(deftemplateName, quanbean);

            // start up the rules engine
            ReteWrapper engine = new ReteWrapper();
            engine.setRulesAsString(rulesReader);
            engine.setCurrentModule(moduleName);
            engine.setTable(h);
            engine.runRules();
            engine.getUpdates();
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
	
	public List<Catalog> getCatalogs() throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getCatalogList();
	}
	
	public List<Product> getProducts(Catalog catalog) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductList(catalog);
	}
	public Product getProductForProductName(String name) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductFromName(name);
	}
	
	/** Assume customer is logged in */
	public CustomerProfile getCustomerProfile(CustomerSubsystem cust) {
		return cust.getCustomerProfile();
	}
}
