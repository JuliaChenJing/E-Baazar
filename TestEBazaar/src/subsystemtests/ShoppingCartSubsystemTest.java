package subsystemtests;


import java.util.List;
import java.util.logging.Logger;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassCartItemForTest;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import dbsetup.DbQueries;
import junit.framework.TestCase;

public class ShoppingCartSubsystemTest extends TestCase{
	public static final int DEFAULT_SHOPPINGCART_ID = 18;
	static String name = "ShoppingCartSubsystem Test";
	static Logger log = Logger.getLogger(ShoppingCartSubsystemTest.class.getName());

	static {
		AllTests.initializeProperties();
	}

	// test the public method getCartItems() in  ShoppingCartSubsystemInterface
	public void testGetCartItems() {
		// setup

		List<CartItem> expectedList = DbQueries.readCartItems();

		System.out.println("1 expected List----------------------------------");
		System.out.println("size:"+expectedList.size());
		for (int i = 0; i < expectedList.size(); i++)
			System.out.println(expectedList.get(i));

	
		ShoppingCartSubsystem shoppingcartss =new ShoppingCartSubsystemFacade();
		try {
			shoppingcartss.retrieveSavedCart();
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<CartItem> listFromSubsystem=shoppingcartss.getCartItems();
		System.out.println("2 listFromSubsystem---------------------------------");
		System.out.println("size:"+listFromSubsystem.size());
		for (int i = 0; i < listFromSubsystem.size(); i++)
			System.out.println(listFromSubsystem.get(i));
		
		assertTrue(expectedList.size() == listFromSubsystem.size());
		assertEquals(listFromSubsystem.toString(), expectedList.toString());
	}
}
