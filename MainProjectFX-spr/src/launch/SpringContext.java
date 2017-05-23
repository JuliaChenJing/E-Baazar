package launch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {
	private static ApplicationContext context;
	public static void initiate(){
		context = new ClassPathXmlApplicationContext("ebazaar.xml");
	}
	private static ApplicationContext getContext(){
		return context;
	}
	
	public static Object getBean(Class clas){
		return  getContext().getBean(clas.getSimpleName());
	}
}
