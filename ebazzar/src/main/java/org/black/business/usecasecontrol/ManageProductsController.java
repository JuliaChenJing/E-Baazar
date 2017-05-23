
package business.usecasecontrol;

import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;

import business.exceptions.BackendException;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;


public enum ManageProductsController   {
    INSTANCE;
    private static final Logger LOG = 
    	Logger.getLogger(ManageProductsController.class.getName());
    
    public List<Product> getProductsList(String catalog) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    
    	LOG.warning("ManageProductsController method getProductsList has not been implemented");
    	//return pss.getProductList(catalog);
    	return null;
    }
    
    public int saveNewCatalog(String catName) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade(); 
    	return pss.saveNewCatalog(catName);
    }
    
    
    public void deleteProduct() {
    	//implement
    }
    
    
}
