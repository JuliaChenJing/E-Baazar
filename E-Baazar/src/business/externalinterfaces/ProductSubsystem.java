
package business.externalinterfaces;
import java.util.List;
import business.exceptions.BackendException;

public interface ProductSubsystem {

	public int readQuantityAvailable(Product product) throws BackendException;
	
	/** obtains product for a given product name */
    public Product getProductFromName(String prodName) throws BackendException;
    
    /** reads the product from the productid */
	public Product getProductFromId(Integer prodId) throws BackendException;
	
	public List<Catalog> getCatalogList() throws BackendException;
	
	/** gets a list of products from the database, based on catalog */
	public List<Product> getProductList(Catalog catalog) throws BackendException;
	
	public Integer getProductIdFromName(String prodName) throws BackendException;
	public CatalogTypes getCatalogTypes() throws BackendException;
	
	
	/** saves newly created catalog */
	public int saveNewCatalog(String catName) throws BackendException;

	public Catalog getCatalogFromName(String catName) throws BackendException;
	/** saves a new product obtained from user input */
	public void saveNewProduct(Product product, Catalog catalog) throws BackendException;

	/** deletes a product obtained from user input */
	public void deleteProduct(Product product) throws BackendException;
	
	/** deletes a catalog obtained from user input */
	public void deleteCatalog(Catalog catalog) throws BackendException;
	
	//For testing
	public DbClassProductForTest getGenericDbClassProduct(); 
	public Catalog getGenericCatalog();

}