package dbsetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.Convert;
import daotests.DbClassAddressTest;
import daotests.DbClassCartItemTest;
import daotests.DbClassProductTest;
import middleware.DbConfigProperties;
//import middleware.dataaccess.DataAccessUtil;
import middleware.externalinterfaces.DbConfigKey;

public class DbQueries {
	static {
		AllTests.initializeProperties();
	}
	static final DbConfigProperties PROPS = new DbConfigProperties();
	static Connection con = null;
	static Statement stmt = null;
	static final String USER = PROPS.getProperty(DbConfigKey.DB_USER.getVal());
	static final String PWD = PROPS.getProperty(DbConfigKey.DB_PASSWORD.getVal());
	static final String DRIVER = PROPS.getProperty(DbConfigKey.DRIVER.getVal());
	static final int MAX_CONN = Integer.parseInt(PROPS.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal()));
	static final String PROD_DBURL = PROPS.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	static final String ACCT_DBURL = PROPS.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	static Connection prodCon = null;
	static Connection acctCon = null;
	String insertStmt = "";
	String selectStmt = "";

	/* Connection setup */
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// debug
			e.printStackTrace();
		}
		try {
			prodCon = DriverManager.getConnection(PROD_DBURL, USER, PWD);
			acctCon = DriverManager.getConnection(ACCT_DBURL, USER, PWD);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	// just to test this class
	public static void testing() {
		try {
			stmt = prodCon.createStatement();
			stmt.executeQuery("SELECT * FROM Product");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//////////////// The public methods to be used in the unit tests
	//////////////// ////////////
	/**
	 * Returns a String[] with values: 0 - query 1 - product id 2 - product name
	 */
	public static String[] insertProductRow() {
		String[] vals = saveProductSql();
		String query = vals[0];
		try {
			stmt = prodCon.createStatement();

			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}

	/**
	 * Returns a list of Addresses
	 */
	public static List<Address> readCustAddresses() {
		String query = readAddressesSql();
		List<Address> addressList = new LinkedList<Address>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				boolean isShippingAddress = rs.getBoolean("isship");
				boolean isBillingAddress = rs.getBoolean("isbill");

				Address addr = CustomerSubsystemFacade.createAddress(street, city, state, zip, isShippingAddress,
						isBillingAddress);

				addressList.add(addr);
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return addressList;

	}

	/**
	 * Returns a String[] with values: 0 - query 1 - catalog id 2 - catalog name
	 */
	public static String[] insertCatalogRow() {
		String[] vals = saveCatalogSql();
		// 0 - query
		String query = vals[0];
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				// 1 - catalog id previously vals[1] = null;
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}

	/**
	 * Returns a String[] with values: 0 - query 1 - customer id 2 - cust fname
	 * 3 - cust lname
	 */
	public static String[] insertCustomerRow() {
		String[] vals = saveCustomerSql();
		String query = vals[0];
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}

	public static void deleteCatalogRow(Integer catId) {
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(deleteCatalogSql(catId));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteProductRow(Integer prodId) {
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(deleteProductSql(prodId));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteCustomerRow(Integer custId) {
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(deleteCustomerSql(custId));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/// queries
	public static String readAddressesSql() {
		return "SELECT * from altaddress WHERE custid = " + DbClassAddressTest.DEFAULT_CUST_ID;
	}

	public static String[] saveCatalogSql() {
		String[] vals = new String[3];

		String name = "testcatalog";
		vals[0] = "INSERT into CatalogType " + "(catalogid,catalogname) " + "VALUES(NULL, '" + name + "')";
		vals[1] = null;
		vals[2] = name;
		return vals;
	}

	public static String[] saveProductSql() {
		String[] vals = new String[3];
		String name = "testprod";
		vals[0] = "INSERT into Product "
				+ "(productid,productname,totalquantity,priceperunit,mfgdate,catalogid,description) " + "VALUES(NULL, '"
				+ name + "',1,1,'12/12/00',1,'test')";
		vals[1] = null;
		vals[2] = name;
		return vals;
	}

	public static String[] saveCustomerSql() {
		String[] vals = new String[4];
		String fname = "testf";
		String lname = "testl";
		vals[0] = "INSERT into Customer " + "(custid,fname,lname) " + "VALUES(NULL,'" + fname + "','" + lname + "')";

		vals[2] = fname;
		vals[3] = lname;
		return vals;
	}

	public static String deleteProductSql(Integer prodId) {
		return "DELETE FROM Product WHERE productid = " + prodId;
	}

	public static String deleteCatalogSql(Integer catId) {
		return "DELETE FROM CatalogType WHERE catalogid = " + catId;
	}

	public static String deleteCustomerSql(Integer custId) {
		return "DELETE FROM Customer WHERE custid = " + custId;
	}

	public static void main(String[] args) {
		readAddressesSql();
		// System.out.println(System.getProperty("user.dir"));
		/*
		 * String[] results = DbQueries.insertCustomerRow();
		 * System.out.println("id = " + results[1]);
		 * DbQueries.deleteCustomerRow(Integer.parseInt(results[1])); results =
		 * DbQueries.insertCatalogRow(); System.out.println("id = " +
		 * Integer.parseInt(results[1]));
		 * DbQueries.deleteCatalogRow(Integer.parseInt(results[1]));
		 */
	}

	public static List<CartItem> readCartItems() {
		String query = readCartItemSql();
		List<CartItem> cartItemList = new LinkedList<CartItem>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

				int cartid = rs.getInt("cartitemid");
				int productid = rs.getInt("productid");
				int cartitemid = rs.getInt("cartitemid");
				String quantity = rs.getString("quantity");
				String totalprice = rs.getString("totalprice");

				CartItem item = ShoppingCartSubsystemFacade.createCartItem(cartid, productid, cartitemid, quantity,
						totalprice, false);

				cartItemList.add(item);
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return cartItemList;
	}

	private static String readCartItemSql() {
		return "SELECT * from shopcartitem WHERE shopcartid = " + DbClassCartItemTest.DEFAULT_Cart_ID;
	}
	public static String readProductSql(){
		return "SELECT * FROM product WHERE catalogid ="+ DbClassProductTest.DEFAULT_CATALOG_ID;
	}
	
	public static List<Product> readAllProducts(){
		String query = readProductSql();
		List<Product> productList = new LinkedList<Product>();
		try {
			stmt = prodCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Catalog catalog = ProductSubsystemFacade.createCatalog(DbClassProductTest.DEFAULT_CATALOG_ID, "Books");
				Integer pi = rs.getInt("productid");
				String productName = rs.getString("productName");
				String mfgDate = rs.getString("mfgDate");
				double unitPrice = rs.getDouble("priceperunit");
				int quantity = rs.getInt("totalquantity");
				String description = rs.getString("description");
				Product prod = ProductSubsystemFacade.createProduct(catalog,pi, productName, quantity,unitPrice,Convert.localDateForString(mfgDate),
						description);
				productList.add(prod);
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return productList;
	}
}
