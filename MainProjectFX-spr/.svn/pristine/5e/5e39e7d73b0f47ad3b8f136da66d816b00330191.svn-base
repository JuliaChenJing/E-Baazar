package springtest;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class BeanTest {
	
	   private DataSource dataSource;
	   public JdbcTemplate jdbcTemplate;
	   
	   
	   public void setDataSource(DataSource dataSource) {
	      this.dataSource = dataSource;
	      this.jdbcTemplate = new JdbcTemplate(dataSource);
	   }

	   private String message;

	   public void setMessage(String message){
	      this.message  = message;
	   }

	   public void getMessage(){
	      System.out.println("Your Message : " + message);
	   }
	   
	}