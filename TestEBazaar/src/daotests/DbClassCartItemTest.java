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
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import dbsetup.DbQueries;
import junit.framework.TestCase;

public class DbClassCartItemTest extends TestCase {
	public static final int DEFAULT_SHOPPINGCART_ID = 18;
	static String name = "Browse and Select Test";
	static Logger log = Logger.getLogger(DbClassAddressTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testReadCartItems() {
		
		//from DbQueries
		List<CartItem> expectedList = DbQueries.readCartItems();//DEFAULT_SHOPPINGCART_ID = 18;

		System.out.println("1 expected List from DbQueries---------------------------------");
		System.out.println("size:"+expectedList.size());
		for (int i = 0; i < expectedList.size(); i++)
			System.out.println(expectedList.get(i));

		//from DbClass
		ShoppingCartSubsystem shoppingcartss =new ShoppingCartSubsystemFacade();
		DbClassCartItemForTest dbclass = shoppingcartss.getGenericDbClassCartItems();
		CustomerProfile custProfile = CustomerSubsystemFacade.createCustProfile(18, "testf", "testl", false);
	    
		try {
			List<CartItem> foundList = dbclass.readCartItems(custProfile);  
			System.out.println("2 found List in database----------------------------------");
			System.out.println("size:"+foundList.size());
			for (int i = 0; i < foundList.size(); i++)
				System.out.println(foundList.get(i));
			
		
			
			assertTrue(expectedList.size() == foundList.size());
			
			
			boolean valfound = true;
			for (int i = 0; i < expectedList.size(); i++) {
				if (!expectedList.get(i).getCartid().equals(foundList.get(i).getCartid()))
					valfound = false;
				else if (!expectedList.get(i).getProductid().equals(foundList.get(i).getProductid()))
					valfound = false;
				else if (!expectedList.get(i).getLineitemid().equals(foundList.get(i).getLineitemid()))
					valfound = false;
				else if (!expectedList.get(i).getProductName().equals(foundList.get(i).getProductName()))
					valfound = false;
				else if (!expectedList.get(i).getQuantity().equals(foundList.get(i).getQuantity()))
					valfound = false;
				else if (!expectedList.get(i).getQuantity().equals(foundList.get(i).getQuantity()))
					valfound = false;
			}
			assertTrue(valfound);
			
			
		} catch(Exception e) {
			fail("Cart Items Lists don't match");
			System.out.println("something wrong happened");
		}
		
	}
}