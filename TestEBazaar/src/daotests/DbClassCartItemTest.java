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
	public static final int DEFAULT_SHOPPINGCART_ID = 13;
	static String name = "Browse and Select Test";
	static Logger log = Logger.getLogger(DbClassAddressTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testReadCartItems() {
		
		List<CartItem> expectedList = DbQueries.readCartItems();

		System.out.println("1 expected List----------------------------------");
		System.out.println("size:"+expectedList.size());
		for (int i = 0; i < expectedList.size(); i++)
			System.out.println(expectedList.get(i));

		//test real dbclass cartitems
		ShoppingCartSubsystem shoppingcartss =new ShoppingCartSubsystemFacade();
		DbClassCartItemForTest dbclass = shoppingcartss.getGenericDbClassCartItems();
		CustomerSubsystem customerss=new CustomerSubsystemFacade();
		CustomerProfile custProfile = customerss.getGenericCustomerProfile();
		custProfile.setCustId(DEFAULT_SHOPPINGCART_ID);//Check the database to see which shopping cart id you want to test
		
		try {
			List<CartItem> foundList = dbclass.readCartItems(custProfile);
			
			System.out.println("2 foundAddressList----------------------------------");
			System.out.println("size:"+foundList.size());
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