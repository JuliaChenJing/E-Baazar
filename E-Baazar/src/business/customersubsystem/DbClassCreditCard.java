package business.customersubsystem;

import java.sql.ResultSet;

import business.externalinterfaces.CustomerProfile;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

class DbClassCreditCard implements DbClass {

	@Override
	public String getDbUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getQueryParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getParamTypes() {
		// TODO Auto-generated method stub
		return null;
	}
	//stub
	public CreditCardImpl readDefaultPaymentInfo(CustomerProfile custProfile) 
			 throws DatabaseException {
		 return new CreditCardImpl("test name", "11/11/2019",
	               "1111222233334444", "Visa");
	 }

	@Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

}
