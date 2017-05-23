package business.ordersubsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import presentation.util.CacheReader;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.productsubsystem.ProductSubsystemFacade;

public class OrderSubsystemFacade implements OrderSubsystem {
	private static final Logger LOG = 
			Logger.getLogger(OrderSubsystemFacade.class.getPackage().getName());
	CustomerProfile custProfile;
	    
    public OrderSubsystemFacade(CustomerProfile custProfile){
        this.custProfile = custProfile;
    }
	
	/** 
     *  Used by customer subsystem at login to obtain this customer's order history from the database.
	 *  Assumes cust id has already been stored into the order subsystem facade 
	 *  This is created by using auxiliary methods at the bottom of this class file.
	 *  First get all order ids for this customer. For each such id, get order data
	 *  and form an order, and with that order id, get all order items and insert
	 *  into the order.
	 */
    public List<Order> getOrderHistory() throws BackendException {
    	//implemented by segun
    	List<Order> orders = new ArrayList<>();
    	try {
			List<Integer> orderIDs = getAllOrderIds();
			for (Integer orderId : orderIDs){
				Order order = getOrderData(orderId);
				List<OrderItem> orderItems = getOrderItems(orderId);
				order.setOrderItems(orderItems);
				orders.add(order);
			}
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//LOG.warning("Method getOrderHistory() still needs to be implemented");
    	return orders;//new ArrayList<Order>();
    }
    
    public void submitOrder_(ShoppingCart cart){
    	//for testing only
    }
    
    
    //implemented by segun
    public void submitOrder(ShoppingCart cart) throws BackendException {
    	//implement
    	//LOG.warning("The method submitOrder(ShoppingCart cart) in OrderSubsystemFacade has not been implemented");   	
    	
    	
    	DbClassOrder dbClass = new DbClassOrder();
    	List<OrderItem> orderItems = new ArrayList<>();
    	OrderItem oItem = null;
    	
    	CustomerSubsystem csf = CacheReader.readCustomer();
    	
    	Order order = new OrderImpl();
    	order.setDate(LocalDate.now());
    	
    	for (CartItem cartItem : cart.getCartItems()){
    		oItem = new OrderItemImpl();
    		
    		oItem.setProductId(cartItem.getProductid());
    		oItem.setQuantity(Integer.valueOf(cartItem.getQuantity()));
    		
    		orderItems.add(oItem);
    	}
    	order.setOrderItems(orderItems);
    	
    	order.setBillAddress(csf.getDefaultBillingAddress());
    	order.setShipAddress(csf.getDefaultShippingAddress());
    	order.setPaymentInfo(csf.getDefaultPaymentInfo());
    	//order.setBillAddress(CustomerSubsystemFacade.createAddress("street", "city", "state", "zip", false, true));
    	//order.setShipAddress(CustomerSubsystemFacade.createAddress("street", "city", "state", "zip", true, false));
    	//order.setPaymentInfo(CustomerSubsystemFacade.createCreditCard("nameOnCard", "expirationDate", "cardNum", "cardType"));
    	order.setTotalPrice(order.getTotalPrice());
    	
    	
    	try {
			dbClass.submitOrder(custProfile, order);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/** Used whenever an order item needs to be created from outside the order subsystem */
  //implemented by segun
    public static OrderItem createOrderItem(
    		Integer prodId, Integer orderId, String quantityReq, String totalPrice, String tax) {
    	//implement
        //LOG.warning("Method createOrderItem(prodid, orderid, quantity, totalprice) still needs to be implemented");
    	
    	int qtyReq = Integer.valueOf(quantityReq);
    	String name = null;
    	int unitPrice = qtyReq/Integer.valueOf(quantityReq);
    	try {
			name = new ProductSubsystemFacade().getProductFromId(prodId).getProductName();
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	OrderItemImpl orderItem = new OrderItemImpl(name, qtyReq, unitPrice);
    	orderItem.setProductId(prodId);
    	orderItem.setOrderId(orderId);
    	orderItem.setTaxAmount(Double.valueOf(tax));
    
        return orderItem;
    }
    
    /** to create an Order object from outside the subsystem */
  //implemented by segun
    public static Order createOrder(Integer orderId, String orderDate, String totalPrice) {
    	//implement
        //LOG.warning("Method  createOrder(Integer orderId, String orderDate, String totalPrice) still needs to be implemented");
    	Order order = new OrderImpl();
    	order.setOrderId(orderId);
    	order.setDate(LocalDate.parse(orderDate));
    	order.setTotalPrice(Double.valueOf(totalPrice));
    	return order;
    }
    
    ///////////// Methods internal to the Order Subsystem -- NOT public
    List<Integer> getAllOrderIds() throws DatabaseException {
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getAllOrderIds(custProfile);
        
    }
    
    /** Part of getOrderHistory */
    List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getOrderItems(orderId);
    }
    
    /** Uses cust id to locate top-level order data for customer -- part of getOrderHistory */
    OrderImpl getOrderData(Integer custId) throws DatabaseException {
    	DbClassOrder dbClass = new DbClassOrder();
    	return dbClass.getOrderData(custId);
    }
}
