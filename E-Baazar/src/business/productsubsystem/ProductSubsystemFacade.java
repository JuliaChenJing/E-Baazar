package business.productsubsystem;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CatalogTypes;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassProductForTest;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.util.TwoKeyHashMap;
import middleware.exceptions.DatabaseException;

public class ProductSubsystemFacade implements ProductSubsystem {
	private static final Logger LOG = 
			Logger.getLogger(ProductSubsystemFacade.class.getPackage().getName());
	
	public static Catalog createCatalog(int id, String name) {
		return new CatalogImpl(id, name);
	}
	
	public static Product createProduct(Catalog c, String name, 
			LocalDate date, int numAvail, double price) {
		return new ProductImpl(c, name, date, numAvail, price);
	}
	
	public static Product createProduct(Catalog c, Integer pi, String pn, int qa, 
			double up, LocalDate md, String desc) {
		return new ProductImpl(c, pi, pn, qa, up, md, desc);
	}
	
	/** obtains product for a given product name */
    public Product getProductFromName(String prodName) throws BackendException {
    	try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(getProductIdFromName(prodName));
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}	
    }
  
    public Integer getProductIdFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			TwoKeyHashMap<Integer,String,Product> table = dbclass.readProductTable();
			return table.getFirstKey(prodName);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
    
    public Product getProductFromId(Integer prodId) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(prodId);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
	}
   
    public CatalogTypes getCatalogTypes() throws BackendException {
    	try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes();
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
    }
    
    
    public List<Catalog> getCatalogList() throws BackendException {
    	try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes().getCatalogs();
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
		
    }
   
    @SuppressWarnings("serial")
	public List<Product> getProductList(Catalog catalog) throws BackendException {
    	
    	DbClassProduct dbClass = new DbClassProduct();
    	List<Product> list = null;
    try {
		list = dbClass.readProductList(catalog);
	} catch (DatabaseException e) {
		e.printStackTrace();
	}
    	
    return list;
    }
    
	public int readQuantityAvailable(Product product) throws BackendException {
		//IMPLEMENT
//		LOG.warning("Method readQuantityAvailable(Product product) has not been implemented");
//		return 2;
		Product p =null;
		try{
			DbClassProduct dbClass = new DbClassProduct();
			 p = dbClass.readProduct(product.getProductId());

		}catch(DatabaseException e){

		}
		return p.getQuantityAvail();
	}
	
	public int saveNewCatalog(String catalogName) throws BackendException {
		
		
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.saveNewCatalog(catalogName);
		} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
	}
	
	
	@Override
	public Catalog getCatalogFromName(String catName) throws BackendException {
		DbClassCatalogTypes dbclass = new DbClassCatalogTypes();
		int id =0;
		try {
			 id = dbclass.getCatalogTypes().getCatalogId(catName);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		Catalog catalog = new CatalogImpl();
		catalog.setName(catName);
		catalog.setId(id);
		return catalog;
		//LOG.warning("Method ProductSubsystemFacade.getCatalogFromName has not been implemented.");
		
	}
	
	@Override
	public void saveNewProduct(Product product, Catalog catalog) throws BackendException {
		//LOG.warning("Method ProductSubsystemFacade.saveNewProduct has not been implemented.");
		DbClassProduct db = new DbClassProduct();
		try {
			int a = db.saveNewProduct(product, catalog);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
	
	@Override
	public void deleteProduct(Product product) throws BackendException {
	//LOG.warning("Method ProductSubsystemFacade.deleteProduct has not been implemented.");
		DbClassProduct db = new DbClassProduct();
		int prodId = product.getProductId();		
		try {
			db.deleteProduct(prodId);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
	
	@Override
	public void deleteCatalog(Catalog catalog) throws BackendException {
	//LOG.warning("Method ProductSubsystemFacade.deleteCatalog has not been implemented.");
		DbClassCatalog dbclass = new DbClassCatalog();
		int catalogId = catalog.getId();
		try {
			dbclass.deleteCatalog(catalogId);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		
		}
		
			
	}

	@Override
	public DbClassProductForTest getGenericDbClassProduct() {
		return new DbClassProduct();
	}
	public Catalog getGenericCatalog() {
		return new CatalogImpl(1, "Books");

	}
	
	
	}
