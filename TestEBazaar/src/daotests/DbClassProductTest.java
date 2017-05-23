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
		List<Product> expected = DbQueries.readAllProducts();
		System.out.println("expected: " + expected.size());
		
		ProductSubsystem pss =new ProductSubsystemFacade();
		DbClassProductForTest dbclass = pss.getGenericDbClassProduct();
		Catalog catalog = ProductSubsystemFacade.createCatalog(DEFAULT_CATALOG_ID, "Books");
		
		boolean valfound = true;
			try {
				List<Product> found = dbclass.readProductList(catalog);
				
				System.out.println("found: " + found.size());
				System.out.println("expected: " + expected.size());
				assertTrue(expected.size() == found.size());
				System.out.println(expected.toString());
			
//				assertEquals(found.toString(), expected.toString());
//				assertEquals(found.toString(),(expected.toString()));
				for(int i = 0;i<expected.size();i++ ){
					if(expected.get(i).getProductId() != found.get(i).getProductId()){
						valfound = false;
				}//else if(expected.get(i).getProductName() != found.get(i).getProductName()){
//					valfound = false;
//			   }else if(expected.get(i).getQuantityAvail() != found.get(i).getQuantityAvail()){
//				valfound = false;
//		
//				}else if(expected.get(i).getUnitPrice() != found.get(i).getUnitPrice()){
//					valfound = false;
//		
//				}else if(expected.get(i).getDescription() != found.get(i).getDescription()){
//					valfound = false;
//			}
//				}
				assertTrue(valfound);
//			} catch(Exception e) {
//				fail("Product Lists don't match");
//			}

				}
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			
	}
		
 
	
	
	
	
	
	
	
	
	
	

}
