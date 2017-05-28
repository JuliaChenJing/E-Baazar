package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.DbClassProductForTest;
import business.externalinterfaces.Product;
import business.util.Convert;
import business.util.TwoKeyHashMap;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

class DbClassProduct implements DbClass, DbClassProductForTest {
	enum Type {
		LOAD_PROD_TABLE, READ_PRODUCT, READ_PROD_LIST, SAVE_NEW_PROD, DELETE_PROD, READ_ALL
	};

	private static final Logger LOG = Logger.getLogger(DbClassProduct.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	private Type queryType;

	private String loadProdTableQuery = "SELECT * FROM product";
	private String readProductQuery = "SELECT * FROM Product WHERE productid = ?";
	private String readProdListQuery = "SELECT * FROM Product WHERE catalogid = ?";
	private String saveNewProdQuery = "INSERT into Product "
			+ "(catalogid,productname,totalquantity,priceperunit,mfgdate,description)" + " VALUES(?,?,?,?,?,?)"; // implement
	private String deleteQuery = "DELETE From Product Where productid = ?";

	private Object[] loadProdTableParams, readAllParams, readProductParams, readProdListParams, saveNewProdParams,
			deleteParam;
	private int[] loadProdTableTypes, readAllTypes, readProductTypes, readProdListTypes, saveNewProdTypes,
			deleteParamType;

	/**
	 * The productTable matches product ID and product name with the
	 * corresponding Product object. It is static so that requests for "read
	 * product" based on product ID can be handled without extra db hits. Useful
	 * for customer use cases, but not for manage products use case
	 */

	public void deleteProduct(int productId) throws DatabaseException {
		queryType = Type.DELETE_PROD;
		deleteParamType = new int[] { Types.SMALLINT };
		deleteParam = new Object[] { productId };
		dataAccessSS.deleteWithinTransaction(this);

	}

	private static TwoKeyHashMap<Integer, String, Product> productTable;
	private Product product;
	private List<Product> productList;

	public TwoKeyHashMap<Integer, String, Product> readProductTable() throws DatabaseException {
		if (productTable != null) {
			return productTable.clone();
		}
		// productTable needs to be populated, so call refresh
		return refreshProductTable();
	}

	/**
	 * Force a database call
	 */
	public TwoKeyHashMap<Integer, String, Product> refreshProductTable() throws DatabaseException {
		queryType = Type.LOAD_PROD_TABLE;
		loadProdTableParams = new Object[] {};
		loadProdTableTypes = new int[] {};
		dataAccessSS.atomicRead(this);

		// Return a clone since productTable must not be corrupted
		return productTable.clone();
	}

	// for testing
	public List<Product> readProductList(Catalog cat) throws DatabaseException {
		if (productList == null) {
			return refreshProductList(cat);
		}
		return Collections.unmodifiableList(productList);
	}

	public List<Product> refreshProductList(Catalog catalog) throws DatabaseException {
		queryType = Type.READ_PROD_LIST;
		readProdListParams = new Object[] { catalog.getId() };
		readProdListTypes = new int[] { Types.INTEGER };
		dataAccessSS.atomicRead(this);
		return productList;
	}

	public Product readProduct(Integer productId) throws DatabaseException {
		if (productTable != null && productTable.isAFirstKey(productId)) {
			return productTable.getValWithFirstKey(productId);
		}
		queryType = Type.READ_PRODUCT;
		readProductParams = new Object[] { productId };
		readProductTypes = new int[] { Types.INTEGER };
		dataAccessSS.atomicRead(this);
		return product;
	}

	/**
	 * Database columns: productid, productname, totalquantity, priceperunit,
	 * mfgdate, catalogid, description
	 */
	public int saveNewProduct(Product product, Catalog catalog) throws DatabaseException {
		// implement//////////////////////////////////////////////////////////
		queryType = Type.SAVE_NEW_PROD;
		saveNewProdParams = new Object[] { catalog.getId(), product.getProductName(), product.getQuantityAvail(),
				product.getUnitPrice(), Convert.localDateAsString(product.getMfgDate()), product.getDescription() };
		saveNewProdTypes = new int[] { Types.SMALLINT, Types.VARCHAR, Types.SMALLINT, Types.DOUBLE, Types.VARCHAR,
				Types.VARCHAR };
		int NumofRows = dataAccessSS.insertWithinTransaction(this);
		// LOG.warning("Method saveNewProduct in DbClassProduct has not been
		// impemented");
		return NumofRows;
	}

	/// DbClass implemented methods
	@Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

	@Override
	public String getQuery() {
		switch (queryType) {
		case LOAD_PROD_TABLE:
			return loadProdTableQuery;
		case READ_PRODUCT:
			return readProductQuery;
		case READ_PROD_LIST:
			return readProdListQuery;
		case SAVE_NEW_PROD:
			return saveNewProdQuery;
		case DELETE_PROD:
			return deleteQuery;
		default:
			return null;
		}
	}

	@Override
	public Object[] getQueryParams() {
		switch (queryType) {
		case LOAD_PROD_TABLE:
			return loadProdTableParams;
		case READ_PRODUCT:
			return readProductParams;
		case READ_PROD_LIST:
			return readProdListParams;
		case SAVE_NEW_PROD:
			return saveNewProdParams;
		case DELETE_PROD:
			return deleteParam;
		default:
			return null;
		}
	}

	@Override
	public int[] getParamTypes() {
		switch (queryType) {
		case LOAD_PROD_TABLE:
			return loadProdTableTypes;
		case READ_PRODUCT:
			return readProductTypes;
		case READ_PROD_LIST:
			return readProdListTypes;
		case SAVE_NEW_PROD:
			return saveNewProdTypes;
		case DELETE_PROD:
			return deleteParamType;
		default:
			return null;
		}
	}

	@Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		switch (queryType) {
		case LOAD_PROD_TABLE:
			populateProdTable(resultSet);
		case READ_PRODUCT:
			populateProduct(resultSet);
		case READ_PROD_LIST:
			populateProdList(resultSet);
		default:
			// do nothing
		}
	}

	private void populateProdList(ResultSet rs) throws DatabaseException {
		productList = new LinkedList<Product>();
		try {
			Product product = null;
			Integer prodId = null;
			String productName = null;
			Integer quantityAvail = null;
			Double unitPrice = null;
			String mfgDate = null;
			Integer catalogId = null;
			String description = null;
			while (rs.next()) {
				prodId = rs.getInt("productid");
				productName = rs.getString("productname");
				quantityAvail = rs.getInt("totalquantity");
				unitPrice = rs.getDouble("priceperunit");
				mfgDate = rs.getString("mfgdate");
				catalogId = rs.getInt("catalogid");
				description = rs.getString("description");
				CatalogImpl catalog = new CatalogImpl(catalogId, (new CatalogTypesImpl()).getCatalogName(catalogId));
				product = new ProductImpl(catalog, prodId, productName, quantityAvail, unitPrice,
						Convert.localDateForString(mfgDate), description);
				productList.add(product);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * Internal method to ensure that product table is up to date.
	 */
	private void populateProdTable(ResultSet rs) throws DatabaseException {
		productTable = new TwoKeyHashMap<Integer, String, Product>();
		try {
			Product product = null;
			Integer prodId = null;
			String productName = null;
			Integer quantityAvail = null;
			Double unitPrice = null;
			String mfgDate = null;
			Integer catalogId = null;
			String description = null;
			while (rs.next()) {
				prodId = rs.getInt("productid");
				productName = rs.getString("productname");
				quantityAvail = rs.getInt("totalquantity");
				unitPrice = rs.getDouble("priceperunit");
				mfgDate = rs.getString("mfgdate");
				catalogId = rs.getInt("catalogid");
				description = rs.getString("description");
				CatalogImpl catalog = new CatalogImpl(catalogId, (new CatalogTypesImpl()).getCatalogName(catalogId));
				product = new ProductImpl(catalog, prodId, productName, quantityAvail, unitPrice,
						Convert.localDateForString(mfgDate), description);
				productTable.put(prodId, productName, product);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	private void populateProduct(ResultSet rs) throws DatabaseException {
		try {
			if (rs.next()) {
				CatalogImpl catalog = new CatalogImpl(rs.getInt("catalogid"),
						(new CatalogTypesImpl()).getCatalogName(rs.getInt("catalogid")));

				product = new ProductImpl(catalog, rs.getInt("productid"), rs.getString("productname"),
						rs.getInt("totalquantity"), rs.getDouble("priceperunit"),
						Convert.localDateForString(rs.getString("mfgdate")), rs.getString("description"));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
