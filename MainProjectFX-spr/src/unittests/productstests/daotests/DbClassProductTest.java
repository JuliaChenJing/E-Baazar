package unittests.productstests.daotests;


import java.util.logging.Logger;

import org.junit.Test;

import business.exceptions.BackendException;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import junit.framework.TestCase;
import unittests.util.DbQueries;

public class DbClassProductTest extends TestCase {
	public static final int DEFAULT_PROD_ID = 1;
	static Logger log = Logger.getLogger(DbClassProductTest.class.getName());
	
	@Test
	public void testReadQuantity(){
		Product prod1 = DbQueries.readProduct();
		int expected=prod1.getQuantityAvail();
		
		//test real dbclass address
		ProductSubsystem subSystem = new ProductSubsystemFacade();
		try {
			
			Product prod2 = subSystem.getProductFromId(DEFAULT_PROD_ID);
			int found  = subSystem.readQuantityAvailable(prod2); 
			log.info("expected = "+expected+" found="+found);
			assertTrue(found==expected);
		} catch (BackendException e) {
			fail("Product Quantity doesn't match");
		}
	}
}
