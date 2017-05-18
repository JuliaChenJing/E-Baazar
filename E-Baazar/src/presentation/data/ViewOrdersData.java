package presentation.data;

import java.util.List;

import java.util.logging.Logger;

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
	}
}
