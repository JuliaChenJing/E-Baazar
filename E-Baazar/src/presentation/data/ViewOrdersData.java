package presentation.data;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import presentation.util.CacheReader;
import presentation.util.UtilForUIClasses;

public enum ViewOrdersData {
	INSTANCE;
	private static final Logger LOG = 
		Logger.getLogger(ViewOrdersData.class.getSimpleName());
	private OrderPres selectedOrder;
	public OrderPres getSelectedOrder() {
		return selectedOrder;
	}
	public void setSelectedOrder(OrderPres so) {
		selectedOrder = so;
	}
	
	public List<OrderPres> getOrders() {
		LOG.warning("ViewOrdersData method getOrders() has not been implemented.");
		return DefaultData.ALL_ORDERS;
		
		//data from database : only return null,needs to be fixed
		
		/*CustomerSubsystem csf = CacheReader.readCustomer();
		System.out.println("---ViewOrdersData----getOrders()------1------------------/n csf:"+csf);
		List<CartItem> cartItems=new ArrayList <CartItem> ();
		List<Order> orderHistory = csf.getOrderHistory();//null
		System.out.println("----ViewOrdersData---getOrders()------2------------------/n orderHistory:"+orderHistory);
		return UtilForUIClasses.orderListToOrderPresList(orderHistory);
		*/
		
	}
}
