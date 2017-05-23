package business.productsubsystem;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CatalogTypes;
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
    ///needs find this =================
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
    
    public List<Product> getProductList(Catalog catalog) throws BackendException {
    	try {
    		DbClassProduct dbclass = new DbClassProduct();
    		return dbclass.readProductList(catalog);
    	} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
    }
    
    
	public int readQuantityAvailable(Product product) throws BackendException {
		//IMPLEMENTED by Tsegay
		try{
			DbClassProduct dbClass = new DbClassProduct();
			Product p = dbClass.readProduct(product.getProductId());
			return p.getQuantityAvail();
		}catch(DatabaseException e){
			
		}
		return 0;
	}
	
	public int saveNewCatalog(String catalogName) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.saveNewCatalog(catalogName);
		} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
	}
	//I need to impliment this 
	///i need to find this ......================...............
	@Override
	public int saveNewProduct(Product product, Catalog catalog) throws BackendException {
		// TODO Auto-generated method stub
		//--Implemented by --Tsegay--
		try {
			DbClassProduct dbClass = new DbClassProduct();
			return dbClass.saveNewProduct(product,catalog);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}
	//Thi abave one======================
	//Implemented by --Tsegay-- it  goes to dbClassProduct
	@Override
	public void deleteProduct(Product product) throws BackendException {
		try{
			DbClassProduct dbclass = new DbClassProduct();
			dbclass.deleteProduct(product);
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	}


	@Override
	public Catalog getCatalogFromName(String catName) throws BackendException {
		try{
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.getCatalogFromName(catName);
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	}
	//Implemented by -- Tsegay-- it goes to dbClassCatalog
	@Override
	public void deleteCatalog(Catalog catalog) throws BackendException {
		try{
			DbClassCatalog dbclass = new DbClassCatalog();
			dbclass.deleteCatalog(catalog);
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	
		}
	//-- implimented by --Tsegay--
	public void updateProduct(Product product) throws BackendException {
		// TODO Auto-generated method stub
		try{
			DbClassProduct dbclass = new DbClassProduct();
			dbclass.updateProduct(product);
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	}

	}
		

