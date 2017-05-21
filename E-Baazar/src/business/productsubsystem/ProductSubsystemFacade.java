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
	private static final Logger LOG = Logger.getLogger(ProductSubsystemFacade.class.getPackage().getName());

	public static Catalog createCatalog(int id, String name) {
		return new CatalogImpl(id, name);
	}

	public static Product createProduct(Catalog c, String name, LocalDate date, int numAvail, double price) {
		return new ProductImpl(c, name, date, numAvail, price);
	}

	public static Product createProduct(Catalog c, Integer pi, String pn, int qa, double up, LocalDate md,
			String desc) {
		return new ProductImpl(c, pi, pn, qa, up, md, desc);
	}

	/** obtains product for a given product name */
	public Product getProductFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(getProductIdFromName(prodName));
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}

	public Integer getProductIdFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			TwoKeyHashMap<Integer, String, Product> table = dbclass.readProductTable();
			return table.getFirstKey(prodName);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}

	}

	public Product getProductFromId(Integer prodId) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(prodId);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}

	

	public List<Catalog> getCatalogList() throws BackendException {
		try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes().getCatalogs();
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}

	}

	@SuppressWarnings("serial")
	public List<Product> getProductList(Catalog catalog) throws BackendException {
		// Uses stub data -- replace with database data
		// if(catalog.getName().equals("Books")) {
		// return new ArrayList<Product>() {
		// {
		// add(new ProductImpl(catalog, "Messiah Of Dune", LocalDate.of(2000,
		// 11, 11), 20, 15.00));
		// add(new ProductImpl(catalog, "Gone with the Wind", LocalDate.of(1995,
		// 12, 5), 15, 12.00));
		// add(new ProductImpl(catalog, "Garden of Rama", LocalDate.of(2005, 1,
		// 1), 5, 18.00));
		// }
		// };
		// } else if(catalog.getName().equals("Clothing")) {
		// return new ArrayList<Product>() {
		// {
		// add(new ProductImpl(catalog, "Pants", LocalDate.of(2000, 11, 1), 20,
		// 15.00));
		// add(new ProductImpl(catalog, "Skirts", LocalDate.of(1995, 1, 5), 15,
		// 12.00));
		// add(new ProductImpl(catalog, "T-Shirts", LocalDate.of(2003, 6, 18),
		// 10, 22.00));
		// }
		// };
		// } else {
		// return new ArrayList<Product>() {
		// {
		// add(new ProductImpl(catalog, "Test", LocalDate.now(), 1, 1.00));
		// }
		// };
		// }
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProductList(catalog);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}

	// ===================================================================================
	public int readQuantityAvailable(Product product) throws BackendException {
		// IMPLEMENT
		// LOG.warning("Method readQuantityAvailable(Product product) has not
		// been implemented");
		// return 2;
		Product p = null;
		try {
			DbClassProduct dbClass = new DbClassProduct();
			p = dbClass.readProduct(product.getProductId());

		} catch (DatabaseException e) {

		}
		return p.getQuantityAvail();
	}

	// =======================================================================================
	public int saveNewCatalog(String catalogName) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.saveNewCatalog(catalogName);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}

	@Override
	public void deleteCatalog(Catalog catalog) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			dbclass.deleteCatalog(catalog);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}

	
	
	
	@Override
	public Catalog getCatalogFromName(String catName) throws BackendException {
	
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.readCatalogByName(catName);
	}
	
	public CatalogTypes getCatalogTypes() throws BackendException {
		try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes();
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}
	

	public void deleteProduct(Product product) throws BackendException {
		// try {
		// Catalog asm= new CatalogImpl();
		// DbClassProduct dbclass = new DbClassProduct();
		// //int productId = getProductIdFromName(product.getProductName());
		// dbclass.deleteProduct(product,asm);
		// } catch(DatabaseException e) {
		// throw new BackendException(e);
		// }
	}

	@Override
	public void saveNewProduct(Product product, Catalog catalog) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			dbclass.saveNewProduct(product, catalog);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}

}
