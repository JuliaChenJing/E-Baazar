package presentation.data;

import java.util.List;
import java.util.logging.Logger;

import presentation.util.CacheReader;
import presentation.util.UtilForUIClasses;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import middleware.externalinterfaces.DbClass;

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
		
		//LOG.warning("ViewOrdersData method getOrders() has not been implemented.");
		CustomerSubsystem csf = CacheReader.readCustomer();
		return UtilForUIClasses.orderListToOrderPresList(csf.getOrderHistory());
		//return DefaultData.ALL_ORDERS;
	}
}
