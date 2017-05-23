package unittests.customertests.daotests;


import java.util.logging.Logger;

import org.junit.Test;

import business.customersubsystem.CustomerSubsystemFacade;
import business.customersubsystem.DbClassCreditCard;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import junit.framework.TestCase;
import middleware.exceptions.DatabaseException;
import unittests.util.DbQueries;

public class DbClassCreditCardTest extends TestCase {
	public static final int DEFAULT_CUST_ID = 1;
	static Logger log = Logger.getLogger(DbClassCreditCardTest.class.getName());
	
	//implemented by: abdul kareem
	@Test
	public void testDefaultPaymentInfo(){
		CreditCard expected = DbQueries.readDefaultPaymentInfo();
		
		//test real dbclass address
		CustomerSubsystem css = new CustomerSubsystemFacade();
		CustomerProfile custProfile = css.getGenericCustomerProfile();
		custProfile.setCustId(DEFAULT_CUST_ID);
		CreditCard found=null;
		try {
			DbClassCreditCard dbClass = new DbClassCreditCard();
			
			found = dbClass.readDefaultPaymentInfo(custProfile);
			
			assertTrue(expected.getCardNum().equals(found.getCardNum())
					&& expected.getCardType().equals(found.getCardType())
					&& expected.getExpirationDate().equals(found.getExpirationDate())
					&& expected.getNameOnCard().equals(found.getNameOnCard())
					);
			
		} catch (DatabaseException e) {
			fail("Default Payment info doesn't match");
		}
	}
}
