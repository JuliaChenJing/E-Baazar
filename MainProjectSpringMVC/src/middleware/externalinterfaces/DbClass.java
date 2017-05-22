
package middleware.externalinterfaces;

import java.sql.ResultSet;

import middleware.exceptions.DatabaseException;

/** All concrete dbclasses implement this interface */
public interface DbClass {
    public void populateEntity(ResultSet resultSet) throws DatabaseException ;
    public String getDbUrl();
    public String getQuery();
    public Object[] getQueryParams();
    public int[] getParamTypes();
}



