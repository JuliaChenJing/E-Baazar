package alltests;

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
    // a static method, could be called by AllTests.initializeProperties();
    public static void initializeProperties() {
    	if (!initialized) {
    		DbConfigProperties.readProps("resources\\dbconfig.properties");
    		RulesConfigProperties.readProps("resources\\rulesconfig.properties");
    		initialized = true;
    	}
    }
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
		//$JUnit-BEGIN$ -- put fully qualified classnames of all tests here
		suite.addTest(new TestSuite(daotests.DbClassAddressTest.class));


		//suite.addTest(new TestSuite(daotests.DbClassCartItemTest.class));
		suite.addTest(new TestSuite(subsystemtests.ProductSubsystemTest.class));
		suite.addTest(new TestSuite(subsystemtests.CustomerSubsystemTest.class));
		//suite.addTest(new TestSuite(performancetests.RulesPerformanceTests.class));
		//suite.addTest(new TestSuite(unittests.business.StringParseTest.class));		
		//suite.addTest(new TestSuite(daotests.DbClassProductTest.class));
		suite.addTest(new TestSuite(subsystemtests.ProductSubsystemTest.class));


		//$JUnit-END$
		return suite;
	}
	
	//////////////// old code //////////////////
	//for JSF project the path is
    // <project_name>\\src\\java\\properties\\dbconfig.properites
//    private static final String LOC_DB_PROPS 
//      = "\\MainProjectFXSoln\\src\\resources\\dbconfig.properties";
//    private static final String LOC_RULES_PROPS 
//      = "\\MainProjectFXSoln\\src\\resources\\rulesconfig.properties";
    //private static final String context = computeDir();
	/*
	 * private static String computeDir() {
    	File f = new File(System.getProperty("user.dir"));
    	if(f.exists() && f.isDirectory()) {
    		System.out.println( f.getParent());
    		return f.getParent();
    	}
    	return null;
    	
    }
	 */
	
}

