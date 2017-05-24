
package com.black.middleware.externalinterfaces;

import java.sql.ResultSet;

import com.black.middleware.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

@Repository
/** All concrete dbclasses implement this interface */
public interface DbClass {
    public void populateEntity(ResultSet resultSet) throws DatabaseException ;
    public String getDbUrl();
    public String getQuery();
    public Object[] getQueryParams();
    public int[] getParamTypes();
}



