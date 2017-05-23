package unittests.ordertests.subsystemtests;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.junit.Test;

import unittests.customertests.subsystemtests.CustomerSubSystemTest;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.ordersubsystem.OrderImpl;
import business.ordersubsystem.OrderSubsystemFacade;
import business.shoppingcartsubsystem.CartItemImpl;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;

//implemented by segun
public class OrderSubsystemTest extends TestCase{
	
	public static final int DEFAULT_CUST_ID = 1;
	static Logger log = Logger.getLogger(CustomerSubSystemTest.class.getName());
	
	//implemented by segun
	@Test
	public void testGetOrderHistory(){
		
		CustomerSubsystem css = new CustomerSubsystemFacade();
		CustomerProfile custProfile = css.getGenericCustomerProfile(); 
		OrderSubsystem oss = new OrderSubsystemFacade(custProfile);
		
		try {
			List<Order> orders = oss.getOrderHistory();
			log.info("expected value : 0 < value, found : " + orders.size());
			assertTrue(orders != null && !orders.isEmpty());
			
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			fail("Order History data not loaded successfully");
		}
	}
	
	//implemented by segun
	@Test
	public void testSubmitOrder(){
		
		CustomerSubsystem css = new CustomerSubsystemFacade();
		CustomerProfile custProfile = css.getGenericCustomerProfile(); 
		OrderSubsystem oss = new OrderSubsystemFacade(custProfile);
		
		ShoppingCartSubsystem scs = new ShoppingCartSubsystemFacade();
		
		
		ShoppingCart cart = scs.getLiveCart();
		CartItemImpl[] cartItems = new CartItemImpl[1];
		
		try {
			cartItems[0] = new CartItemImpl("Gone With The Wind", "5", "100.0");
			
			for (CartItem c : cartItems){
				cart.getCartItems().add(c);
			}
			oss.submitOrder(cart);
			
			
			
		} catch (BackendException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
