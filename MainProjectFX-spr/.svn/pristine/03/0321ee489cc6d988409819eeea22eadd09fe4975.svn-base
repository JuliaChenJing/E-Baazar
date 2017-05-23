package unittests.customertests.daotests;


import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import junit.framework.TestCase;
import middleware.exceptions.DatabaseException;
import unittests.util.DbQueries;

public class DbClassAddressTest2 extends TestCase {
	public static final int DEFAULT_CUST_ID = 1;
	static Logger log = Logger.getLogger(DbClassAddressTest2.class.getName());
	
	//implemented by: abdul kareem
	@Test
	public void testDefaultShipAddress(){
		Address expected = DbQueries.readDefaultShipAddress();
		
		//test real dbclass address
		CustomerSubsystem css = new CustomerSubsystemFacade();
		CustomerProfile custProfile = css.getGenericCustomerProfile();
		custProfile.setCustId(DEFAULT_CUST_ID);
		DbClassAddressForTest dbclass = css.getGenericDbClassAddress();
		try {
			Address found = dbclass.readDefaultShipAddress(custProfile);
			assertTrue(expected.getCity().equals(found.getCity())
					&& expected.getState().equals(found.getState())
					&& expected.getStreet().equals(found.getStreet())
					);
		} catch (DatabaseException e) {
			fail("Default Shipp address doesn't match");
		}
				
	}
	
	
	   public static void main(String[] args) {
		      Result result = JUnitCore.runClasses(DbClassAddressTest2.class);
				
		      for (Failure failure : result.getFailures()) {
		         System.out.println(failure.toString());
		      }
				
		      System.out.println(result.wasSuccessful());
		   }
}
