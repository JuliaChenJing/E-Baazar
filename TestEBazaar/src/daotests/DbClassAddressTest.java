package daotests;


import java.util.List;
import java.util.logging.Logger;

import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;

import dbsetup.DbQueries;

import junit.framework.TestCase;
import alltests.AllTests;

public class DbClassAddressTest extends TestCase {
	public static final int DEFAULT_CUST_ID = 1;
	static String name = "Browse and Select Test";
	static Logger log = Logger.getLogger(DbClassAddressTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testReadAllAddresses() {
		List<Address> expected = DbQueries.readCustAddresses();
		
		//test real dbclass address
		CustomerSubsystem css = CustomerSubsystemFacade.createCustomer();
		DbClassAddressForTest dbclass = css.getGenericDbClassAddress();
		CustomerProfile custProfile = css.getGenericCustomerProfile();
		custProfile.setCustId(DEFAULT_CUST_ID);
		
		try {
			List<Address> found = dbclass.readAllAddresses(custProfile);
			System.out.println(found);
			System.out.println(expected);
			assertTrue(expected.size() == found.size());
			assertEquals(found.toString(), expected.toString());
			
		} catch(Exception e) {
			fail("Address Lists don't match");
		}
		
	}
}
