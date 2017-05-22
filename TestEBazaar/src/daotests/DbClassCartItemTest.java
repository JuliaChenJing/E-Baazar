package daotests;

import java.util.List;
import java.util.logging.Logger;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import dbsetup.DbQueries;
import junit.framework.TestCase;

public class DbClassCartItemTest extends TestCase {
	public static final int DEFAULT_CUST_ID = 1;
	public static final int DEFAULT_Cart_ID = 4;
	static String name = "Browse and Select Test";
	static Logger log = Logger.getLogger(DbClassAddressTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testReadCartItems() {
		
		List<CartItem> expectedList = DbQueries.readCartItems();

		System.out.println("1 expected List----------------------------------");
		System.out.println(expectedList.size());
		for (int i = 0; i < expectedList.size(); i++)
			System.out.println(expectedList.get(i));

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
			
			
			System.out.println(foundAddressList.toString().equals(expectedList.toString()));
			System.out.println("1"+expectedList);
			System.out.println("2"+foundAddressList);
			
			assertTrue(expectedList.size() == foundAddressList.size());
			
			assertEquals(foundAddressList.toString(), expectedList.toString());
			
	
			
			
		} catch(Exception e) {
			fail("Address Lists don't match");
		}
		
	}
}