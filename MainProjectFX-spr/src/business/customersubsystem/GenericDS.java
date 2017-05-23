package business.customersubsystem;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import middleware.externalinterfaces.DbClass2;


public class GenericDS {
    private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);		
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void atomicRead(DbClass2 dbclass){
		String query = getFormattedQuery(dbclass.getQuery(), dbclass.getQueryParams(), dbclass.getParamTypes());
		List<Map<String, Object>> map = jdbcTemplate.queryForList(query);
		dbclass.populateEntity(map);
	}
	public void insertWithinTransaction(DbClass2 dbclass){
		String query = getFormattedQuery(dbclass.getQuery(), dbclass.getQueryParams(), dbclass.getParamTypes());
		jdbcTemplate.execute(query);
	}

	private  String getFormattedQuery(String query, Object[] params, int[] paramTypes)  {
		String value=null;
	
		for (int i = 0; i < paramTypes.length; ++i) {
			value ="";
		
			int type = paramTypes[i];
			switch (type) {
			case Types.BIGINT:
				value += ((Integer) params[i]);
				break;
			case Types.BOOLEAN:
				value += ((Boolean) params[i]);
				break;
			case Types.DATE:
				value += "'"+((String) params[i])+"'";
				break;
			case Types.DOUBLE:
				value += ((Double) params[i]);
				break;
			case Types.FLOAT:
				value += ((Float) params[i]);
				break;
			case Types.INTEGER:
				value += ((Integer) params[i]);
				break;
			case Types.NULL:
				value += 0;
				break;
			case Types.VARCHAR:
				value += "'"+((String) params[i])+"'";
				break;
			default:
				value += (params[i]);
			}
			
			query = query.replaceFirst("\\?", value);
		}
		return query;
	}
	/*
	public static void main(String[] arg){
		GenericDS ds = new GenericDS();
		Object params[] = {1,"varchar",true};
		int paramTypes[]= {Types.INTEGER, Types.VARCHAR,Types.BOOLEAN};
		String query = "SELECT * FROM TEST integer=? ,varchar=? boolean=?";
		System.out.println(ds.getFormattedQuery("SELECT * FROM TEST integer=? ,varchar=? boolean=?", params, paramTypes));
	}
	*/
 
}
