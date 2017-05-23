
package business.usecasecontrol;


import java.util.List;

import business.DbClassLogin;
import business.Login;
import business.exceptions.BackendException;
import business.exceptions.UserException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerSubsystem;


public class LoginControl {
	
	//returns authorization level if authenticated
	public int authenticate(Login login) throws UserException, BackendException {
		
		DbClassLogin dbClass = new DbClassLogin(login);
        if(!dbClass.authenticate()) {
        	throw new UserException("Authentication failed for ID: " + login.getCustId());
        }
        return dbClass.getAuthorizationLevel();
        
	}
	
	public CustomerSubsystem prepareAndStoreCustomerObject(CustomerSubsystem cust, 
			List<CartItem> cartItems, Login login, int authorizationLevel) throws BackendException {
		
        //initialize by loading data from database
        cust.initializeCustomer(login.getCustId(), cartItems, authorizationLevel);
        return cust;
	}
    
}
