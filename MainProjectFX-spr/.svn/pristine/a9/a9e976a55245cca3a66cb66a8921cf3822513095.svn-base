package presentation.util;

import business.externalinterfaces.CustomerSubsystem;
import presentation.data.SessionCache;

/**
 * Provides convenience methods to extract values from SessionCache
 *
 */
public class CacheReader {
	
	public static CustomerSubsystem readCustomer() {
		return (CustomerSubsystem)SessionCache.getInstance().get(SessionCache.CUSTOMER);
	}
	public static boolean readLoggedIn() {
		return (Boolean)SessionCache.getInstance().get(SessionCache.LOGGED_IN);
	}
	
	public static boolean custIsAdmin() {
		return (readCustomer()!=null?readCustomer().isAdmin():false);
	}
}
