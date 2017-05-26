
package presentation.data;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Standalone version of a web session context. Intended to provide
 * a simple cache for the app during execution. It belongs
 * in the presentation layer since different clients (in a web
 * app) would need to have different caches.
 * 
 * To use the cache effectively, it is necessary for
 * UI controllers to extract the Customer subsystem from
 * the cache and pass it to the business layer for processing
 * steps -- the business layer must never attempt to
 * read the cache directly.
 *
 * Shopping Cart is expected to be loaded into the cache as long
 * as there is no owning Customer; when a customer logs in, 
 * Shopping Cart should be removed from the cache.
 */
public class SessionCache {
	
	private static final Logger LOG 
	   = Logger.getLogger(SessionCache.class.getName());
	public static final String CUSTOMER = "Customer";
	public static final String SHOP_CART = "Shopping Cart";
    public static final String LOGGED_IN = "LoggedIn";
    
    // private
 	private static SessionCache instance = new SessionCache();// singleton
 	private HashMap<Object, Object> context;

 	private SessionCache() {
 		context = new HashMap<Object,Object>();
         context.put(LOGGED_IN, Boolean.FALSE);      
     }
    
    //public interface
    public static SessionCache getInstance() {
        return instance;   
    }  
   //session cache
    public void add(Object name, Object value){
        if(context != null) {
            context.put(name,value);
            LOG.info("Key/value pair (" + name + ", " + value + ") added to SessionCache");
        }
    }
    
    public Object get(Object name){
    	
        if(context == null) {
        	LOG.info("No matching value for " + name + " from cache found. Returning null");
            return null;
        }
        Object retval = context.get(name);
        LOG.info("Returning value " + retval + " from cache, for name " + name);
        return retval;
    }
    
    public void remove(Object name){
    	if(name == null) {
    		LOG.warning("Attempt to remove from cache a value with key null has failed.");
    	}
    	Object itemRemoved = context.remove(name);
    	LOG.info("Attempt to remove item from cache with key " + name + " caused " 
    	   + itemRemoved + " to be removed");
    
    }
    
}
