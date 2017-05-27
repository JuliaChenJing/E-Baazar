package presentation.data;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.usecasecontrol.ViewOrdersController;
import presentation.util.CacheReader;
import presentation.util.UtilForUIClasses;

public enum ViewOrdersData {
	INSTANCE;
	private static final Logger LOG = Logger.getLogger(ViewOrdersData.class.getSimpleName());
	private OrderPres selectedOrder;

	public OrderPres getSelectedOrder() {
		return selectedOrder;
	}

	public void setSelectedOrder(OrderPres so) {
		selectedOrder = so;
	}

	public List<OrderPres> getOrders()  {
		// LOG.warning("ViewOrdersData method getOrders() has not been
		// implemented.");
		// return DefaultData.ALL_ORDERS;

		// data from database : only return null,needs to be fixed

		SessionCache session = SessionCache.getInstance();
		CustomerSubsystem customerSSInCache = (CustomerSubsystem) session.get(SessionCache.CUSTOMER);

		ViewOrdersController viewOrderController = new ViewOrdersController();

		CustomerProfile customerprofile = customerSSInCache.getCustomerProfile();

		OrderSubsystem orderSS = new OrderSubsystemFacade(customerprofile);
		List<Order> orderHistory = null;
		try {
			//from 1.3 Dat to 1.5 Subsystem
			orderHistory = orderSS.getOrderHistory();
			System.out.println("ViewOrdersData  getOrders()   orderHistory from orderSS:" + orderHistory);
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return UtilForUIClasses.orderListToOrderPresList(orderHistory);

	}
}
