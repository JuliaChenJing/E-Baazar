package subsystemtests;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import dbsetup.DbQueries;
import junit.framework.TestCase;
import alltests.AllTests;

public class ProductSubsystemTest extends TestCase {
	
	static String name = "Product Subsystem Test";
	static Logger log = Logger.getLogger(ProductSubsystemTest.class.getName());
	
	//initialize Properties, this one will run first based on what we learned in FPP class
	static {
		AllTests.initializeProperties();
	}
	
	//test the public method getCatalogList() in ProductSubsystemInterface
	public void testGetCatalogNames() {
		//setup
		/*
		 * Returns a String[] with values:
		 * 0 - query
		 * 1 - catalog id
		 * 2 - catalog name
		 */
		String[] insertResult = DbQueries.insertCatalogRow();//testly insert a new catalog into DB
		String catalogInserted = insertResult[2];//catalog name
		
		//create an object for ProductSubsystem
		ProductSubsystem pss =new ProductSubsystemFacade();
		try {
			List<String> found = pss.getCatalogList()//the list of all the names of catalog
				      .stream()
				      .map(cat -> cat.getName())
				      .collect(Collectors.toList());
			boolean valfound = false;
			for(String catData : found) {			
					if(catData.equals(catalogInserted)) //as long as one of the catalog in found equals catalogInserted
						valfound = true;
				
			}
			assertTrue(valfound);
			
		} catch(Exception e) {
			fail("Inserted value not found");
		} finally {
			DbQueries.deleteCatalogRow(Integer.parseInt(insertResult[1]));
		}
	
	}
	
}