package subsystemtests;
import java.util.logging.Logger;
import alltests.AllTests;
import junit.framework.TestCase;

public class OrderSubsystemTest extends TestCase{

	static String name = "OrderSubsystem Test";
	static Logger log = Logger.getLogger(OrderSubsystemTest.class.getName());

	static {
		AllTests.initializeProperties();
	}

	// test the public method GetOrderHistory() in OrderSubsystemInterface
	public void testGetOrderHistory() {
		
		
	}
}