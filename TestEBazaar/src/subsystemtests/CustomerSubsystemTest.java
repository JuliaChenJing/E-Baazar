package subsystemtests;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import dbsetup.DbQueries;
import junit.framework.TestCase;
import alltests.AllTests;

public class CustomerSubsystemTest extends TestCase {

	static String name = "Customer Subsystem Test";
	static Logger log = Logger.getLogger(CustomerSubsystemTest.class.getName());

	static {
		AllTests.initializeProperties();
	}

	// test the public method getGenericCustomerProfile() in
	// CustomerSubsystemInterface
	public void testGetGenericCustomerProfile() {
		// setup
		/**
		 * Returns a String[] with values: 0 - query
		 *  1 - customer id 
		 *  2 - cust fname 
		 *  3 - cust lname
		 */
		String[] insertResult = DbQueries.insertCustomerRow();// testly insert a
																// new customer
																// into DB
		String customerFirstNameInserted = insertResult[2];// customer first
															// name "testf"
		System.out.println(customerFirstNameInserted);
		// create an object for CustomerSubysystem
		CustomerSubsystem pss = new CustomerSubsystemFacade();
		try {
			CustomerProfile profile = pss.getGenericCustomerProfile();
			boolean valfound = false;

			System.out.println(profile.getFirstName());
			if (profile.getFirstName().equals(customerFirstNameInserted))
				valfound = true;

			assertTrue(valfound);

		} catch (Exception e) {
			fail("Inserted value not found");
		} finally {
			DbQueries.deleteCustomerRow(Integer.parseInt(insertResult[1]));
		}

	}

	
	
	
	// test the public method getGenericCustomerProfile() in CustomerSubsystemInterface
	public void testGetGenericDbClassAddress() {
		// setup
		
		/**
		 * Returns a list of Addresses
		 */
		 List<Address> addressList = DbQueries.readCustAddresses();
		
		 for (int i=0;i<addressList.size();i++)
		  System.out.println(addressList.get(i));
		
		// create an object for CustomerSubysystem
		CustomerSubsystem pss = new CustomerSubsystemFacade();
		try {
			DbClassAddressForTest address = pss.getGenericDbClassAddress();
			boolean valfound = false;
			for(Address a : addressList) {			
				if(a.equals(address)) 
					valfound = true;
			
		}
		assertTrue(valfound);
			
			
		} catch (Exception e) {
			fail("addres not found");
		} 

	}

}
