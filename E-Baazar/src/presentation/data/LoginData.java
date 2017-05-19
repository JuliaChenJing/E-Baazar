package presentation.data;

import presentation.gui.LoginWindow;
import business.usecasecontrol.LoginControl;

import java.util.ArrayList;
import java.util.List;

import business.BusinessConstants;
import business.Login;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.exceptions.UserException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;

public class LoginData {
	LoginControl usecaseControl = new LoginControl() ;
	public int authenticate(Login login) throws UserException, BackendException {   	
    	try {
    		int authorizationLevel = usecaseControl.authenticate(login);
    		return authorizationLevel;
    	} catch(UserException e) {
    		throw(e);
    	} catch(BackendException e) {
    		throw(e);
    	}	
	}
	public Login extractLogin(LoginWindow loginWindow) {
		Integer id = Integer.parseInt(loginWindow.getId());
    	String pwd = loginWindow.getPassword();
    	Login login = new Login(id, pwd);
    	return login;
	}
    public void loadCustomer(Login login, int authorizationLevel) throws BackendException {
    	CustomerSubsystem customer = new CustomerSubsystemFacade();
		//need to place into SessionContext immediately since the facade will be used during
		//initialization; alternative: createAddress, createCreditCard methods
		//made to be static
		SessionCache cache = SessionCache.getInstance();
		cache.add(SessionCache.LOGGED_IN, Boolean.TRUE);
		cache.add(SessionCache.CUSTOMER, customer);
		//If a shopping cart already exists in memory,
		//extract the live cart items and send to Customer
		//to put into Customer's live cart 
		ShoppingCartSubsystem shopCartSs = (ShoppingCartSubsystem)cache.get(SessionCache.SHOP_CART);
		List<CartItem> liveCartItems = new ArrayList<>();
		if(shopCartSs != null) {
			liveCartItems = shopCartSs.getLiveCart().getCartItems();
		}
        usecaseControl.prepareAndStoreCustomerObject(customer, liveCartItems, login, authorizationLevel); 
        //require access of ShoppingCart to go through Customer now
        cache.remove(SessionCache.SHOP_CART);
    }
}
