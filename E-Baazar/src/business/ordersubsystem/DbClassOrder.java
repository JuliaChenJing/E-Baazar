
package business.ordersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.util.Convert;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

class DbClassOrder implements DbClass {
	enum Type {
		GET_ORDER_ITEMS, GET_ORDER_IDS, GET_ORDER_DATA, SUBMIT_ORDER, SUBMIT_ORDER_ITEM
	};

	private static final Logger LOG = Logger.getLogger(DbClassOrder.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();

	private Type queryType;

	private String orderItemsQuery = "SELECT * FROM OrderItem WHERE orderid = ?";
	private String orderIdsQuery = "SELECT orderid FROM Ord WHERE custid = ?";
	private String orderDataQuery = "SELECT * FROM Ord WHERE orderid = ?";
	
	private String submitOrderQuery = "INSERT into Ord "
			+ "(custid, shipaddress1, shipcity, shipstate, shipzipcode, "
			+           "billaddress1, billcity, billstate,"+ "billzipcode,"
			+ " nameoncard,  cardnum,cardtype, expdate, orderdate, totalpriceamount) "
			+ "VALUES(?,?,?,?,?  ,?,?,?,?,?   ,?,?,?,?,?)";
	
	private String submitOrderItemQuery = "INSERT into OrderItem "
			+ "(orderid, productid,quantity,totalprice,shipmentcost,totalamount) " + "VALUES(?,?,?,?,?,?,)";
	Object[] orderItemsParams, orderIdsParams, orderDataParams, submitOrderParams, submitOrderItemParams;
	int[] orderItemsTypes, orderIdsTypes, orderDataTypes, submitOrderTypes, submitOrderItemTypes;

	// This is set by submitOrder and then used by submitOrderData
	private CustomerProfile custProfile;
	private List<Integer> orderIds;
	private List<OrderItem> orderItems;
	private OrderImpl orderData;
	private Order order;
	private int orderid;

	DbClassOrder() {
	}

	OrderImpl retrieveSavedOrder(Integer id) throws DatabaseException {
		dataAccessSS.establishConnection(this);
		dataAccessSS.startTransaction();
		try {
			OrderImpl order = getOrderData(id);
			List<OrderItem> items = getOrderItems(id);
			order.setOrderItems(items);

			// dataAccessSS.commit();
			return order;

		} catch (DatabaseException e) {
			// dataAccessSS.rollback();
			LOG.warning("Rolling back...");
			throw (e);
		} finally {
			dataAccessSS.releaseConnection();
		}
	}
	
	List<Integer> getAllOrderIds(CustomerProfile custProfile) throws DatabaseException {
		queryType = Type.GET_ORDER_IDS;
		orderIdsParams = new Object[] { custProfile.getCustId() };
		orderIdsTypes = new int[] { Types.INTEGER };
		dataAccessSS.atomicRead(this);
		return Collections.unmodifiableList(orderIds);
	}

	public List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
		LOG.warning("Method getOrderItems(Integer orderId) has not been implemented");
		orderItems = new ArrayList<>();
		queryType = Type.GET_ORDER_ITEMS;
		orderItemsParams = new Object[] { orderId };
		orderItemsTypes = new int[] { Types.INTEGER };
		dataAccessSS.atomicRead(this);
		return Collections.unmodifiableList(orderItems);
	}

	OrderImpl getOrderData(Integer orderId) throws DatabaseException {
		queryType = Type.GET_ORDER_DATA;
		orderDataParams = new Object[] { orderId };
		orderDataTypes = new int[] { Types.INTEGER };
		dataAccessSS.atomicRead(this);
		return orderData;
	}

	
	/**
	 * This method submits top-level data in Order to the Ord table (this is
	 * executed within the helper method submitOrderData) and then, after it
	 * gets the order id, it submits each OrderItem from Order to the OrderItem
	 * table (items are submitted one at a time using submitOrderItem). All this
	 * is done within a transaction. Separate methods are provided
	 */
	void submitOrder(CustomerProfile custProfile, Order order) throws DatabaseException {
		LOG.warning(
				"Method submitOrder(CustomerProfile custProfile, Order order)  in DbClassOrder has not beenimplemented");
		this.order = order;
		this.custProfile = custProfile;
		orderid = submitOrderData();
		orderItems = order.getOrderItems();
		for (int i = 0; i < orderItems.size(); i++)
			submitOrderItem(orderItems.get(i));
		
	}

	/** This is part of the general submitOrder method */
	private Integer submitOrderData() throws DatabaseException {
		LOG.warning("Method submitOrderData() in DbClassOrder has not been implemented.");
		queryType = Type.SUBMIT_ORDER;
		Address shipAddr = order.getShipAddress();
		Address billAddr = order.getBillAddress();
		CreditCard cc = order.getPaymentInfo();
		
		submitOrderParams = new Object[] { custProfile.getCustId(),
				shipAddr.getStreet(), shipAddr.getCity(),shipAddr.getState(), shipAddr.getZip(), // shipping
				billAddr.getStreet(), billAddr.getCity(), billAddr.getState(), billAddr.getZip(), // billing
				cc.getNameOnCard(), cc.getCardNum(), cc.getCardType(), cc.getExpirationDate(), // credit  card																	
				Convert.localDateAsString(order.getOrderDate()), order.getTotalPrice() };

		submitOrderTypes = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, // shipping
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, // billing
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, // credit																// card
				Types.VARCHAR, Types.DOUBLE };
		// creation and release of connection handled by submitOrder
		// this should be part of a transaction started in submitOrder
		//return dataAccessSS.insert();//with problem
		return dataAccessSS.insertWithinTransaction(this);
	}

	/** This is part of the general submitOrder method */
	private void submitOrderItem(OrderItem item) throws DatabaseException {
		LOG.warning("Method submitOrderItem(OrderItem item) in DbClassOrder has not been implemented.");
		queryType = Type.SUBMIT_ORDER_ITEM;
		// orderid, productid,quantity,totalprice,shipmentcost,totalamount
		submitOrderItemParams = new Object[] { orderid, item.getProductId(), item.getQuantity(), item.getTotalPrice(),
				item.getTotalPrice() * 0.1, item.getTotalPrice() * 1.1 };
		submitOrderItemTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.DOUBLE, Types.DOUBLE,
				Types.DOUBLE };

		// creation and release of connection handled by submitOrder
		// this should be part of a transaction started in submitOrder
		//dataAccessSS.insert();
		dataAccessSS.insertWithinTransaction(this);
	}

	private void populateOrderItems(ResultSet resultSet) throws DatabaseException {
		LOG.warning("Method populateOrderItems(ResultSet) still needs to be implemented");
		// implement
		orderItems = new ArrayList<OrderItem>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("productid");
				int quantity = resultSet.getInt("quantity");
				double price = resultSet.getDouble("totalprice");
				ProductSubsystem productSS = new ProductSubsystemFacade();
				Product product = productSS.getProductFromId(id);
				String productname = product.getProductName();
				OrderItem orderItem = new OrderItemImpl(productname, quantity, price / quantity);

				orderItems.add(orderItem);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void populateOrderIds(ResultSet resultSet) throws DatabaseException {
		LOG.warning("populateOrderIds(ResultSet resultSet) still needs to be implemented");
		orderIds = new LinkedList<Integer>();
		try {
			while (resultSet.next()) {
				orderIds.add(resultSet.getInt("orderid"));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	private void populateOrderData(ResultSet rs) throws DatabaseException {
		// implement
		LOG.warning("Method populateOrderData(ResultSet resultSet) still needs to be implemented");
		orderData = new OrderImpl();
		Address shippingAddress = null;
		Address billingAddress = null;
		CreditCard creditCard = null;
		try {
			while (rs.next()) {
				int orderId = rs.getInt("orderid");
				orderData.setOrderId(orderId);

				LocalDate date = Convert.localDateForString(rs.getString("orderdate"));
				orderData.setDate(date);

				double price = rs.getDouble("totalpriceamount");
				orderData.setTotalPrice(price);

				orderData.setOrderItems(orderItems);
				System.out.println("DbClassOrder   populateOrderData()     orderItems:" + orderItems);

				// load shipping address
				String shipStreet = rs.getString("shipaddress1");
				String shipCity = rs.getString("shipcity");
				String shipState = rs.getString("shipstate");
				String shipZip = rs.getString("shipzipcode");
				shippingAddress = CustomerSubsystemFacade.createAddress(shipStreet, shipCity, shipState, shipZip, true,
						false);

				// load billing address
				String billStreet = rs.getString("shipaddress1");
				String billCity = rs.getString("shipcity");
				String billState = rs.getString("shipstate");
				String billpZip = rs.getString("shipzipcode");
				billingAddress = CustomerSubsystemFacade.createAddress(billStreet, billCity, billState, billpZip, false,
						true);

				// load credit card: createCreditCard(String name, String num,
				// String type, expDate)
				String name = rs.getString("nameoncard");
				String num = rs.getString("cardnum");
				String type = rs.getString("cardtype");
				String exp = rs.getString("expdate");
				creditCard = CustomerSubsystemFacade.createCreditCard(name, exp, num, type);

				// load cart: set all the things needed
				orderData.setShipAddress(shippingAddress);
				orderData.setBillAddress(billingAddress);
				orderData.setPaymentInfo(creditCard);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		switch (queryType) {
		case GET_ORDER_ITEMS:
			populateOrderItems(resultSet);
			break;
		case GET_ORDER_IDS:
			populateOrderIds(resultSet);
			break;
		case GET_ORDER_DATA:
			populateOrderData(resultSet);
			break;
		default:
			// do nothing
		}
	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	}

	public String getQuery() {
		switch (queryType) {
		case GET_ORDER_ITEMS:
			return orderItemsQuery;
		case GET_ORDER_IDS:
			return orderIdsQuery;
		case GET_ORDER_DATA:
			return orderDataQuery;
		case SUBMIT_ORDER:
			return submitOrderQuery;
		case SUBMIT_ORDER_ITEM:
			return submitOrderItemQuery;
		default:
			return null;
		}
	}

	@Override
	public Object[] getQueryParams() {
		switch (queryType) {
		case GET_ORDER_ITEMS:
			return orderItemsParams;
		case GET_ORDER_IDS:
			return orderIdsParams;
		case GET_ORDER_DATA:
			return orderDataParams;
		case SUBMIT_ORDER:
			return submitOrderParams;
		case SUBMIT_ORDER_ITEM:
			return submitOrderItemParams;
		default:
			return null;
		}
	}

	@Override
	public int[] getParamTypes() {
		switch (queryType) {
		case GET_ORDER_ITEMS:
			return orderItemsTypes;
		case GET_ORDER_IDS:
			return orderIdsTypes;
		case GET_ORDER_DATA:
			return orderDataTypes;
		case SUBMIT_ORDER:
			return submitOrderTypes;
		case SUBMIT_ORDER_ITEM:
			return submitOrderItemTypes;
		default:
			return null;
		}
	}

}
