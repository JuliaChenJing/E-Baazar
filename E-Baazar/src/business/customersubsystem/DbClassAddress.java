package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassAddressForTest;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

class DbClassAddress implements DbClass, DbClassAddressForTest {
	enum Type {
		INSERT, READ_ALL, READ_DEFAULT_BILL, READ_DEFAULT_SHIP
	};

	private static final Logger LOG = Logger.getLogger(DbClassAddress.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();

	DbClassAddress() {
	}

	// used when an Address object needs to be saved to the db
	void setAddress(Address addr) {
		address = addr;
	}

	///// queries ///////
	private String insertQuery = "INSERT into altaddress " + "(custid,street,city,state,zip) " + "VALUES(?,?,?,?,?)";
	private String readAllQuery = "SELECT * from altaddress WHERE custid = ?";
	// param value to set: custProfile.getCustId()
	private String readDefaultBillQuery = "SELECT billaddress1, billaddress2, billcity, billstate, billzipcode "
			+ "FROM Customer WHERE custid = ?";
	// param value to set: custProfile.getCustId()
	private String readDefaultShipQuery = "SELECT shipaddress1, shipaddress2, shipcity, shipstate, shipzipcode "
			+ "FROM Customer WHERE custid = ?";
	// param value to set: custProfile.getCustId()

	private Object[] insertParams, readAllParams, readDefaultBillParams, readDefaultShipParams;
	private int[] insertTypes, readAllTypes, readDefaultBillTypes, readDefaultShipTypes;

	// this object is stored here, using setAddress, when it needs to be saved
	// to db
	private Address address;

	// these are populated after database reads
	private List<Address> addressList;
	private AddressImpl defaultShipAddress;
	private AddressImpl defaultBillAddress;
	private Type queryType;

	// column names for Address table
	private final String STREET = "street";
	private final String CITY = "city";
	private final String STATE = "state";
	private final String ZIP = "zip";
	private final String IS_SHIP = "isship";
	private final String IS_BILL = "isbill";

	// Precondition: Address has been set in this object
	void saveAddress(CustomerProfile custProfile) throws DatabaseException {
		queryType = Type.INSERT;
		insertParams = new Object[] { custProfile.getCustId(), address.getStreet(), address.getCity(),
				address.getState(), address.getZip() };
		insertTypes = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		dataAccessSS.insertWithinTransaction(this);
	}

	AddressImpl readDefaultShipAddress(CustomerProfile custProfile) throws DatabaseException {
		return new AddressImpl("test street", "testcity", "test state", "11111", true, false);
		// implement
	}

	AddressImpl readDefaultBillAddress(CustomerProfile custProfile) throws DatabaseException {
		return new AddressImpl("test street", "testcity", "test state", "11111", false, true);
	}

	public List<Address> readAllAddresses(CustomerProfile custProfile) throws DatabaseException {
		// this.custProfile = custProfile;
		queryType = Type.READ_ALL;
		readAllParams = new Object[] { custProfile.getCustId() };
		readAllTypes = new int[] { Types.INTEGER };
		dataAccessSS.atomicRead(this);
		return addressList;
	}

	@Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());

	}

	@Override
	public String getQuery() {
		switch (queryType) {
		case INSERT:
			return insertQuery;
		case READ_ALL:
			return readAllQuery;
		case READ_DEFAULT_BILL:
			return readDefaultBillQuery;
		case READ_DEFAULT_SHIP:
			return readDefaultShipQuery;
		default:
			return null;
		}
	}

	@Override
	public Object[] getQueryParams() {
		switch (queryType) {
		case INSERT:
			return insertParams;
		case READ_ALL:
			return readAllParams;
		case READ_DEFAULT_BILL:
			return readDefaultBillParams;
		case READ_DEFAULT_SHIP:
			return readDefaultShipParams;
		default:
			return null;
		}
	}

	@Override
	public int[] getParamTypes() {
		switch (queryType) {
		case INSERT:
			return insertTypes;
		case READ_ALL:
			return readAllTypes;
		case READ_DEFAULT_BILL:
			return readDefaultBillTypes;
		case READ_DEFAULT_SHIP:
			return readDefaultShipTypes;
		default:
			return null;
		}
	}
	////// populate objects after reads ///////////

	@Override
	public void populateEntity(ResultSet rs) throws DatabaseException {
		switch (queryType) {
		case READ_ALL:
			populateAddressList(rs);
			break;
		case READ_DEFAULT_SHIP:
			populateDefaultShipAddress(rs);
			break;
		case READ_DEFAULT_BILL:
			populateDefaultBillAddress(rs);
			break;
		default:
			// do nothing
		}
	}

	void populateAddressList(ResultSet rs) throws DatabaseException {
		addressList = new LinkedList<Address>();
		if (rs != null) {
			try {
				while (rs.next()) {
					address = new AddressImpl();
					String str = rs.getString(STREET);
					address.setStreet(str);
					address.setCity(rs.getString(CITY));
					address.setState(rs.getString(STATE));
					address.setZip(rs.getString(ZIP));
					boolean isShipping = rs.getInt(IS_SHIP) == 1;
					boolean isBilling = rs.getInt(IS_BILL) == 1;
					address.isShippingAddress(isShipping);
					address.isBillingAddress(isBilling);
					addressList.add(address);
				}
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}
	}

	void populateDefaultShipAddress(ResultSet rs) throws DatabaseException {
		LOG.warning("Method populateDefaultShipAddress(ResultSet rs) not yet implemented");
		// implement
		defaultShipAddress = new AddressImpl();
		if (rs != null) {
			try {
				
					address = defaultShipAddress;
					String str = rs.getString(STREET);
					address.setStreet(str);
					address.setCity(rs.getString(CITY));
					address.setState(rs.getString(STATE));
					address.setZip(rs.getString(ZIP));
					boolean isShipping = rs.getInt(IS_SHIP) == 1;
					boolean isBilling = rs.getInt(IS_BILL) == 1;
					address.isShippingAddress(isShipping);
					address.isBillingAddress(isBilling);
					addressList.add(address);
				
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}
	}

	void populateDefaultBillAddress(ResultSet rs) throws DatabaseException {
		//LOG.warning("Method populateDefaultBillAddress(ResultSet rs) not yet implemented");
		// implement
		defaultBillAddress = new AddressImpl();
		if (rs != null) {
			try {
				
					address = defaultBillAddress;
					String str = rs.getString(STREET);
					address.setStreet(str);
					address.setCity(rs.getString(CITY));
					address.setState(rs.getString(STATE));
					address.setZip(rs.getString(ZIP));
					boolean isShipping = rs.getInt(IS_SHIP) == 1;
					boolean isBilling = rs.getInt(IS_BILL) == 1;
					address.isShippingAddress(isShipping);
					address.isBillingAddress(isBilling);
					addressList.add(address);
				
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}
	}

	public static void main(String[] args) {
		DbClassAddress dba = new DbClassAddress();
		CustomerProfile cp = new CustomerProfileImpl(1, "John", "Smith");
		try {
			System.out.println(dba.readAllAddresses(cp));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

}
