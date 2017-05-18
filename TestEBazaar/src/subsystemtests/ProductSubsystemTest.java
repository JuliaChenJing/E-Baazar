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
	
	static {
		AllTests.initializeProperties();
	}
	
	public void testGetCatalogNames() {
		//setup
		/*
		 * Returns a String[] with values:
		 * 0 - query
		 * 1 - catalog id
		 * 2 - catalog name
		 */
		String[] insertResult = DbQueries.insertCatalogRow();
		String expected = insertResult[2];
		
		ProductSubsystem pss = ProductSubsystemFacade.createProductSubsystem();
		try {
			List<String> found = pss.getCatalogList()
				      .stream()
				      .map(cat -> cat.getName())
				      .collect(Collectors.toList());
			boolean valfound = false;
			for(String catData : found) {			
					if(catData.equals(expected)) valfound = true;
				
			}
			assertTrue(valfound);
			
		} catch(Exception e) {
			fail("Inserted value not found");
		} finally {
			DbQueries.deleteCatalogRow(Integer.parseInt(insertResult[1]));
		}
	
	}
	
}
