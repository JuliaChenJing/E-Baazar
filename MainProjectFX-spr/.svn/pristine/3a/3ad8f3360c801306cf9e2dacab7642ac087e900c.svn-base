
package middleware.externalinterfaces;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import middleware.exceptions.DatabaseException;

/** All concrete dbclasses implement this interface */
public interface DbClass2 {
    public String getDbUrl();
    public String getQuery();
    public Object[] getQueryParams();
    public int[] getParamTypes();
    public void populateEntity(ResultSet resultSet) throws DatabaseException ;
    public void populateEntity(List<Map<String, Object>> map);
    public void setDataSource(DataSource dataSource);
    public JdbcTemplate getJdbcTemplate();
}



