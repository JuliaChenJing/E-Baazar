package com.black.business.usecasecontrol;


import java.util.List;
import com.black.business.exceptions.BackendException;
import com.black.business.exceptions.BusinessException;
import com.black.business.exceptions.RuleException;
import com.black.business.RulesQuantity;
import com.black.business.externalinterfaces.CartItem;
import com.black.business.externalinterfaces.Catalog;
import com.black.business.externalinterfaces.CustomerProfile;
import com.black.business.externalinterfaces.CustomerSubsystem;
import com.black.business.externalinterfaces.Product;
import com.black.business.externalinterfaces.ProductSubsystem;
import com.black.business.externalinterfaces.Rules;
import com.black.business.externalinterfaces.ShoppingCartSubsystem;
import com.black.business.productsubsystem.ProductSubsystemFacade;
import com.black.business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import com.black.business.util.DataUtil;
import org.springframework.stereotype.Service;

@Service
public enum BrowseAndSelectController {
	INSTANCE;
	
	public void updateShoppingCartItems(List<CartItem> cartitems) {
		ShoppingCartSubsystemFacade.INSTANCE.updateShoppingCartItems(cartitems);
	}
	
	public List<CartItem> getCartItems() {
		return ShoppingCartSubsystemFacade.INSTANCE.getCartItems();
	}
	
	/** Makes saved cart live in subsystem and then returns the new list of cartitems */
	public void retrieveSavedCart() {
		ShoppingCartSubsystem shopCartSS = ShoppingCartSubsystemFacade.INSTANCE;
		
		// Saved cart was retrieved during login
		shopCartSS.makeSavedCartLive();	
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
	public CustomerProfile getCustomerProfile() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		return cust.getCustomerProfile();
	}
}
