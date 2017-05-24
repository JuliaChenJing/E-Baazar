
package com.black.middleware.dataaccess;

import java.util.logging.Logger;

import com.black.middleware.DbConfigProperties;
import com.black.middleware.exceptions.DatabaseException;
import com.black.middleware.externalinterfaces.DbConfigKey;

/**
 * @author pcorazza
 * @since Nov 10, 2004
 * Class Description:
 * 
 * 
 */
class DataAccessUtil {
	private static final Logger LOG =
		Logger.getLogger(DataAccessUtil.class.getName());
	
	static void initializePool() throws DatabaseException {
		DbConfigProperties props = new DbConfigProperties();
		SimpleConnectionPool.INSTANCE.init(
	            props.getProperty(DbConfigKey.DB_USER.getVal()), 
	            props.getProperty(DbConfigKey.DB_PASSWORD.getVal()), 
	            props.getProperty(DbConfigKey.DRIVER.getVal()),
	            Integer.parseInt(props.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal())));
	    
	}
   
    
}
