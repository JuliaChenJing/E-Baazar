package springtest;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringTest {
	  public static void main(String[] args) {
	      ApplicationContext context = 
	             new ClassPathXmlApplicationContext("ebazaar.xml");

	      BeanTest obj = (BeanTest) context.getBean("beanTest");

	      obj.getMessage();
	  	

	      String SQL = "select * from catalogtype";
	      List<Map<String, Object>> rowCount =  obj.jdbcTemplate.queryForList( SQL );
	      rowCount.forEach(map -> System.out.println(map) );
	      for(Map map:rowCount){
	    	  System.out.println(map.get("catalogname"));
	      }
	   }

}
