package business.ordersubsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import business.externalinterfaces.*;
import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import presentation.util.CacheReader;

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
    	//implement
    	LOG.warning("Method getOrderHistory() still needs to be implemented");
    	return new ArrayList<Order>();
    }

    public void submitOrder(ShoppingCart cart) throws BackendException {
        //implemented
        //LOG.warning("The method submitOrder(ShoppingCart cart) in OrderSubsystemFacade has not been implemented");


        DbClassOrder dbClass = new DbClassOrder();
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem oItem = null;

        CustomerSubsystem csf = CacheReader.readCustomer();

        Order order = new OrderImpl();
        order.setDate(LocalDate.now());

        for (CartItem cartItem : cart.getCartItems()){
            //calculate unit price by dividing total price by qty, since no helper method exists in cartItem for that purpose.
            String name= cartItem.getProductName();
            int quantity= Integer.valueOf(cartItem.getQuantity());
            double price = Double.valueOf(cartItem.getTotalprice())/Double.valueOf(cartItem.getQuantity());
            oItem = new OrderItemImpl(name,quantity,price);

            oItem.setProductId(cartItem.getProductid());
            oItem.setTaxAmount(1.5);//fixed tax amount
            orderItems.add(oItem);
        }
        order.setOrderItems(orderItems);


        order.setBillAddress(csf.getDefaultBillingAddress());
        order.setShipAddress(csf.getDefaultShippingAddress());
        order.setPaymentInfo(csf.getDefaultPaymentInfo());
        order.setTotalPrice(order.getTotalPrice());


        try {
            dbClass.submitOrder(custProfile, order);
        } catch (DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



	
	/** Used whenever an order item needs to be created from outside the order subsystem */
    public static OrderItem createOrderItem(
    		Integer prodId, Integer orderId, String quantityReq, String totalPrice) {
    	//implement
        LOG.warning("Method createOrderItem(prodid, orderid, quantity, totalprice) still needs to be implemented");
    	return null;
    }
    
    /** to create an Order object from outside the subsystem */
    public static Order createOrder(Integer orderId, String orderDate, String totalPrice) {
    	//implement
        LOG.warning("Method  createOrder(Integer orderId, String orderDate, String totalPrice) still needs to be implemented");
    	return null;
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
    OrderImpl getOrderData(Integer orderId) throws DatabaseException {
    	DbClassOrder dbClass = new DbClassOrder();
    	return dbClass.getOrderData(orderId);
    }
}
