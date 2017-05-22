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
import business.externalinterfaces.DbClassCartItemForTest;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
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

		//test real dbclass cartitems
		ShoppingCartSubsystem css =new ShoppingCartSubsystemFacade();
		DbClassCartItemForTest dbclass = css.getGenericDbClassCartItems();
		CustomerProfile custProfile = css.getGenericCustomerProfile();
		custProfile.setCustId(DEFAULT_CUST_ID);
		
		try {
			List<CartItem> foundList = dbclass.readCartItems(custProfile);
			
			System.out.println("2 foundAddressList----------------------------------");
			System.out.println(foundList.size());
			for (int i = 0; i < foundList.size(); i++)
				System.out.println(foundList.get(i));
			
			
			System.out.println(foundList.toString().equals(expectedList.toString()));
			System.out.println("1"+expectedList);
			System.out.println("2"+foundList);
			
			assertTrue(expectedList.size() == foundList.size());
			
			assertEquals(foundList.toString(), expectedList.toString());
			
	
			
			
		} catch(Exception e) {
			fail("Address Lists don't match");
			System.out.println("something wrong happened");
		}
		
	}
}