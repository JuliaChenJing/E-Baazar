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
		
		List<Address> expectedAddressList = DbQueries.readCustAddresses();

		System.out.println("1 expectedAddressList----------------------------------");
		System.out.println(expectedAddressList.size());
		for (int i = 0; i < expectedAddressList.size(); i++)
			System.out.println(expectedAddressList.get(i));

		//test real dbclass address
		CustomerSubsystem css =new CustomerSubsystemFacade();
		DbClassAddressForTest dbclass = css.getGenericDbClassAddress();
		CustomerProfile custProfile = css.getGenericCustomerProfile();
		custProfile.setCustId(DEFAULT_CUST_ID);
		
		try {
			List<Address> foundAddressList = dbclass.readAllAddresses(custProfile);
			
			System.out.println("2 foundAddressList----------------------------------");
			System.out.println(foundAddressList.size());
			for (int i = 0; i < foundAddressList.size(); i++)
				System.out.println(foundAddressList.get(i));
			
			
			System.out.println(foundAddressList.toString().equals(expectedAddressList.toString()));
			System.out.println("1"+expectedAddressList);
			System.out.println("2"+foundAddressList);
			
			assertTrue(expectedAddressList.size() == foundAddressList.size());
			
			assertEquals(foundAddressList.toString(), expectedAddressList.toString());
			
			
			boolean valfound = true;
			for (int i = 0; i < expectedAddressList.size(); i++) {
				if (!expectedAddressList.get(i).getCity().equals(foundAddressList.get(i).getCity()))
					valfound = false;
				else if (!expectedAddressList.get(i).getState().equals(foundAddressList.get(i).getState()))
					valfound = false;
				else if (!expectedAddressList.get(i).getStreet().equals(foundAddressList.get(i).getStreet()))
					valfound = false;
				else if (!expectedAddressList.get(i).getZip().equals(foundAddressList.get(i).getZip()))
					valfound = false;
			}
			assertTrue(valfound);
			
			
		} catch(Exception e) {
			fail("Address Lists don't match");
		}
		
	}
}
