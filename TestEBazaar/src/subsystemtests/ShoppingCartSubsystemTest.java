package subsystemtests;

import java.util.List;
import java.util.logging.Logger;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import dbsetup.DbQueries;
import junit.framework.TestCase;

public class ShoppingCartSubsystemTest extends TestCase{

	static String name = "ShoppingCartSubsystem Test";
	static Logger log = Logger.getLogger(ShoppingCartSubsystemTest.class.getName());

	static {
		AllTests.initializeProperties();
	}

	// test the public method getEmptyCartForTest() in ShoppingCartSubsystemInterface
	public void testGetGenericCustomerProfile() {
		// setup
		/**
		 * Returns a String[] with values: 
		 * 0 - query 
		 * 1 - customer id 
		 * 2 - cust fname 
		 * 3 - cust lname
		 */
		String[] insertResult = DbQueries.insertCustomerRow();// testly insert a new customer into DB
		String customerFirstNameInserted = insertResult[2];// customer first  name "testf"
															
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

	// test the public method GetEmptyCartItemForTest() in  ShoppingCartSubsystemInterface
	public void testGetEmptyCartItemForTest() {
		
		// setup

		// Returns a list of Addresses
		List<Address> addressList = DbQueries.readCustAddresses();

		System.out.println("1  addressList----------------------------------");
		System.out.println(addressList.size());
		for (int i = 0; i < addressList.size(); i++)
			System.out.println(addressList.get(i));

		// create an object for CustomerSubysystem
		CustomerSubsystem pss = new CustomerSubsystemFacade();
		try {

			CustomerProfile custProfile = pss.getGenericCustomerProfile();
			List<Address> addressListFromSubsystem = pss.getGenericDbClassAddress().readAllAddresses(custProfile);
			for (int i = 0; i < addressList.size(); i++)
				System.out.println(addressList.get(i));

			System.out.println("2 addressListFromSubsystem----------------------------------");
			System.out.println(addressListFromSubsystem.size());
			for (int i = 0; i < addressListFromSubsystem.size(); i++)
				System.out.println(addressListFromSubsystem.get(i));

			boolean valfound = true;
			for (int i = 0; i < addressListFromSubsystem.size(); i++) {
				if (!addressListFromSubsystem.get(i).getCity().equals(addressList.get(i).getCity()))
					valfound = false;
				else if (!addressListFromSubsystem.get(i).getState().equals(addressList.get(i).getState()))
					valfound = false;
				else if (!addressListFromSubsystem.get(i).getStreet().equals(addressList.get(i).getStreet()))
					valfound = false;
				else if (!addressListFromSubsystem.get(i).getZip().equals(addressList.get(i).getZip()))
					valfound = false;
			}
			assertTrue(valfound);
			assertEquals(addressListFromSubsystem.toString(), addressList.toString());

		} catch (Exception e) {
			fail("adddres not found");
		}

	}
}
