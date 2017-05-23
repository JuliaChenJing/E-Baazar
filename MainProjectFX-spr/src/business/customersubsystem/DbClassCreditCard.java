package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import business.customersubsystem.DbClassAddress.Type;
import business.externalinterfaces.CustomerProfile;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassCreditCard implements DbClass {
	enum Type {INSERT, READ_ALL, READ_DEFAULT_PAYMENT_INFO};
	private static final Logger LOG 
	    = Logger.getLogger(DbClassCreditCard.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	///// queries ///////
	      //param value to set: custProfile.getCustId()
	private String readDefaultPaymentInfo = "SELECT nameoncard,expdate, cardtype, cardnum " +
                "FROM Customer WHERE custid = ?";
	
	private Object[] readDefaultPaymentInfoParams;
	private int[] readDefaultPaymentInfoTypes;
    private Type queryType;
    private CreditCardImpl defaultPaymentInfo;
    @Override
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
        
    }
    @Override
    public String getQuery() {
        switch(queryType) {
	        case READ_DEFAULT_PAYMENT_INFO: 
	        	return readDefaultPaymentInfo;
	        default :
	        	return null;
        } 
    }
    @Override
    public Object[] getQueryParams() {
    	switch(queryType) {
	        case READ_DEFAULT_PAYMENT_INFO: 
	        	return readDefaultPaymentInfoParams;
	        default :
	        	return null;
	    } 
    }
    
    @Override
    public int[] getParamTypes() {
    	switch(queryType) {
	        case READ_DEFAULT_PAYMENT_INFO: 
	        	return readDefaultPaymentInfoTypes;
	        default :
	        	return null;
	    } 
    }
    ////// populate objects after reads ///////////
    
    @Override
    public void populateEntity(ResultSet rs) throws DatabaseException {
    	switch(queryType) {
    	case READ_DEFAULT_PAYMENT_INFO:
    		populateDefaultPaymentInfo(rs);
    		break;
    	default:
    		//do nothing
    	}
    }
    
    private void populateDefaultPaymentInfo(ResultSet rs) throws DatabaseException {
        if(rs != null){
            try {
            	rs.next();
            	defaultPaymentInfo = new CreditCardImpl( rs.getString("nameoncard") ,
            			rs.getString("expdate"),
            			rs.getString("cardnum"),
            			rs.getString("cardtype"));
                    }
            catch(SQLException e){
                throw new DatabaseException(e);
            }   
        }  
    }
	//stub
	 public CreditCardImpl readDefaultPaymentInfo(CustomerProfile custProfile) throws DatabaseException {
	        queryType = Type.READ_DEFAULT_PAYMENT_INFO;
	        readDefaultPaymentInfoParams = new Object[]
	        	{custProfile.getCustId()};
	        readDefaultPaymentInfoTypes = new int[]
	        	{Types.INTEGER};
	        dataAccessSS.atomicRead(this);
	 /*
			  return new CreditCardImpl("test name", "11/11/2019",
	               "1111222233334444", "Visa");
	               */
		 return defaultPaymentInfo;
	 }


}
