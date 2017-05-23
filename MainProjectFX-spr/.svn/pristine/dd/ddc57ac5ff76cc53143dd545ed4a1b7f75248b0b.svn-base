package unittests.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.Product;
import business.productsubsystem.ProductSubsystemFacade;
import middleware.DbConfigProperties;
import middleware.externalinterfaces.DbConfigKey;
import unittests.customertests.daotests.DbClassAddressTest1;
import unittests.productstests.daotests.DbClassProductTest;
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
		}
		catch(ClassNotFoundException e){
			//debug
			e.printStackTrace();
		}
		try {
			prodCon = DriverManager.getConnection(PROD_DBURL, USER, PWD);
			acctCon = DriverManager.getConnection(ACCT_DBURL, USER, PWD);
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	public static Address readDefaultShipAddress() {
		String query = readDefaultShipAddressesSql();
		Address address=null;
		try {
			stmt = acctCon.createStatement();
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			System.out.println(rs);
                if(rs!=null){
                	rs.next();
                    String street = rs.getString("shipaddress1")+rs.getString("shipaddress2");
                    String city = rs.getString("shipcity");
                    String state = rs.getString("shipstate");
                    String zip = rs.getString("shipzipcode");
                    
                     address = CustomerSubsystemFacade.createAddress(street,city,state,zip,true,true);
                }  
                stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return address;
	}
	public static Address readDefaultBillAddress() {
		String query = readDefaultBillAddressesSql();
		Address address=null;
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
                if(rs!=null){
                	rs.next();
                    String street = rs.getString("billaddress1")+rs.getString("billaddress2");
                    String city = rs.getString("billcity");
                    String state = rs.getString("billstate");
                    String zip = rs.getString("billzipcode");
                    
                     address = CustomerSubsystemFacade.createAddress(street,city,state,zip,true,true);
                }  
                stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return address;
	}

	public static CreditCard readDefaultPaymentInfo() {
		String query = readDefaultPaymentInfoSql();
		CreditCard creditCard=null;
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
                if(rs!=null){
                	rs.next();
                    String cardType = rs.getString("cardtype");
                    String nameOnCard = rs.getString("nameoncard");
                    String expirationDate = rs.getString("expdate");
                    String cardNum = rs.getString("cardnum");
                    
                    creditCard = CustomerSubsystemFacade.createCreditCard(nameOnCard, expirationDate, cardNum, cardType);
                }  
                stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return creditCard;
	}
	public static Product readProduct() {
		String query = readProductSql();
		Product product=null;
		try {
			stmt = prodCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
                if(rs!=null){
                	rs.next();
                    int numAvail = rs.getInt("totalquantity");
                    
                    product = ProductSubsystemFacade.createProduct(null, "", null, numAvail, 0.0);
                }  
                stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	
	
	///queries
	public static String readDefaultShipAddressesSql() {
		return "SELECT shipaddress1, shipaddress2, shipcity, shipstate, shipzipcode "+
        "FROM Customer WHERE custid = " + DbClassAddressTest1.DEFAULT_CUST_ID;
	}
	public static String readDefaultBillAddressesSql() {
		return "SELECT billaddress1, billaddress2, billcity, billstate, billzipcode "+
        "FROM Customer WHERE custid = " + DbClassAddressTest1.DEFAULT_CUST_ID;
	}
	public static String readDefaultPaymentInfoSql() {
		return "SELECT nameoncard,expdate, cardtype, cardnum "+
        "FROM Customer WHERE custid = " + DbClassAddressTest1.DEFAULT_CUST_ID;
	}
	public static String readProductSql() {
		return "SELECT productid,totalquantity "+
        "FROM Product WHERE productid = " + DbClassProductTest.DEFAULT_PROD_ID;
	}

}
