package daotests;

import java.util.List;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import business.externalinterfaces.DbClassProductForTest;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import dbsetup.DbQueries;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;
import junit.framework.TestCase;

public class DbClassProductTest extends TestCase {
	
	public static final int DEFAULT_CATALOG_ID = 1;
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testReadAllProducts() {
		//---------------from DbQueries
		List<Product> expected = DbQueries.readAllProducts();//DEFAULT_CATALOG_ID = 1
		System.out.println("1 expected----------------------------------");
		System.out.println("size:"+expected.size());
		for (int i = 0; i < expected.size(); i++)
			System.out.println(expected.get(i).getProductId());
		
		//-----------------from dbclass
		ProductSubsystem pss =new ProductSubsystemFacade();
		DbClassProductForTest dbclass = pss.getGenericDbClassProduct();
		Catalog catalog = ProductSubsystemFacade.createCatalog(DEFAULT_CATALOG_ID, "Books");//DEFAULT_CATALOG_ID = 1
		
		boolean valfound = true;
			try {
				List<Product> found = dbclass.readProductList(catalog);
				
				System.out.println("1 found---------------------------------");
				System.out.println("size:"+found.size());
				for (int i = 0; i < found.size(); i++)
					System.out.println(found.get(i).getProductId());
				
				
				assertTrue(expected.size() == found.size());

				for(int i = 0;i<expected.size();i++ ){
					if(expected.get(i).getProductId() != found.get(i).getProductId()){
						valfound = false;
				}

				assertTrue(valfound);

				}
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			
	}
		
 
	
	
	
	
	
	
	
	
	
	

}
