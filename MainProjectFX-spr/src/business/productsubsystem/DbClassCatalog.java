package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.productsubsystem.DbClassProduct.Type;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

/**
 * This class is concerned with managing data for a single
 * catalog. To read or update the entire list of catalogs in
 * the database, see DbClassCatalogs
 *
 */
public class DbClassCatalog implements DbClass {
	enum Type {INSERT,DELETE_CATALOG,READ_CATALOG};
	@SuppressWarnings("unused")
	private static final Logger LOG = 
		Logger.getLogger(DbClassCatalog.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
	
	private Type queryType;
	private Catalog catalog;
	
	private String insertQuery = "INSERT into CatalogType (catalogname) VALUES(?)"; 
	private String deleteQuery = "DELETE FROM CatalogType WHERE catalogid = ?"; //added
	private String readQuery = "SELECT * from CatalogType where catalogname = ?";//added
	private Object[] insertParams,deletParams,readParams;
	private int[] insertTypes,deleteType,readTypes;
    
    public int saveNewCatalog(String catalogName) throws DatabaseException {
    	queryType = Type.INSERT;
    	insertParams = new Object[]{catalogName};
    	insertTypes = new int[]{Types.VARCHAR};
    	return dataAccessSS.insertWithinTransaction(this);  	
    }
    public void deleteCatalog(Catalog catalog) 
			throws DatabaseException {
		//implemented by --Tsegay--
		LOG.warning("Method saveNewProduct in DbClassProduct has not been impemented");
		queryType = Type.DELETE_CATALOG;
		deletParams = new Object[]
	        	{catalog.getId()};
		deleteType = new int[]
	        	{Types.INTEGER}; 
	        int count = dataAccessSS.deleteWithinTransaction(this);
	      //this is for testing
	    	if(count > 0 ) catalog.setId(-1);
    }
    
    
    @Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}
    //added
    public Catalog getCatalogFromName(String catName) throws DatabaseException {
    	
    	queryType = Type.READ_CATALOG;
    	readParams = new Object[]{catName};
    	readTypes = new int[]{Types.VARCHAR};
    	dataAccessSS.atomicRead(this);
    	return catalog;
    }
    
    @Override
	public String getQuery() {
		switch(queryType) {
			case INSERT:
				return insertQuery;
			case DELETE_CATALOG:
				return deleteQuery;
			default:
				return null;
		}
	}
    @Override
   	public Object[] getQueryParams() {
   		switch(queryType) {
   			case INSERT:
   				return insertParams;
   			case DELETE_CATALOG:
   				return deletParams;
   			default:
   				return null;
   		}
    }		
	 @Override
	public int[] getParamTypes() {
		 switch(queryType) {
			case INSERT:
				return insertTypes;
			case DELETE_CATALOG:
				return deleteType;
			default:
				return null;
		}
	 }
	  @Override
		public void populateEntity(ResultSet resultSet) throws DatabaseException {
			// do nothing
			switch(queryType){
				case READ_CATALOG:
					populateCatalog(resultSet);
					break;
				case INSERT:
					populateCatalog(resultSet);
					break;
				default:
					//do nothing
			}
		}
	    
	    private void populateCatalog(ResultSet rs) throws DatabaseException{
	     //do nothing 
	    }
	
}
