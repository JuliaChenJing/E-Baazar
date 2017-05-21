package business.usecasecontrol;


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
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.DataUtil;

public class BrowseAndSelectController {
	
	public void updateShoppingCartItems(ShoppingCartSubsystem shopCartSs, List<CartItem> cartitems) {
		shopCartSs.setCartItems(cartitems);
	}

	
	/** Response to user request to see saved shopping cart items
	 *  This is accomplished by making the saved cart, which was retrived at login,
	 *  the new live cart. It should not be possible to make this call
	 *  if customer has not logged in. 
	 */
	public void retrieveSavedCart(ShoppingCartSubsystem shopCartSS, boolean custIsLoggedIn) {
		// Saved cart was retrieved during login
		if(custIsLoggedIn) shopCartSS.makeSavedCartLive();	
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

		ProductSubsystem prodSS = new ProductSubsystemFacade();
		
		//find current quant avail since quantity may have changed
		//since product was first loaded into UI
		int currentQuantityAvail = prodSS.readQuantityAvailable(product);
		Rules transferObject = new RulesQuantity(currentQuantityAvail, quantityRequested);//
		transferObject.runRules();

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
