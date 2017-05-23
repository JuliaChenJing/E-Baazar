package unittests.util;

import java.io.File;
import java.util.logging.Logger;

import business.externalinterfaces.RulesConfigProperties;

import middleware.DbConfigProperties;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Requires junit.jar to be added as an external library.
 * Runs all JUnit tests specified in the suite() method.
 * Tests require access to the EBazaar project (this should
 * be configured in the Properties for this project) and 
 * will require access to configuration files for the database
 * and rules engine. If running on a PC,
This class will find the necessary configuration files. However, on a Mac, 
the file separator \\ (as it
referenced in Java) must be replaced by /.
See the method initializeProperties.
*/
public class AllTests extends TestSuite {
    static Logger log = Logger.getLogger(AllTests.class.getName());
    static {
    	initializeProperties();
	} 
	private static boolean initialized = false;
    
    public static void initializeProperties() {
    	if (!initialized) {
    		DbConfigProperties.readProps("resources/dbconfig.properties");
    		//RulesConfigProperties.readProps("resources\\rulesconfig.properties");
    		initialized = true;
    	}
    }
	
	
}

