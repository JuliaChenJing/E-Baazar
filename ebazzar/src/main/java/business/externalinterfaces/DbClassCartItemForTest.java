package business.externalinterfaces;

import java.util.List;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

/* Used only for testing DbClassAddress */
public interface DbClassCartItemForTest extends DbClass {
	public List<CartItem> readCartItems(CustomerProfile custProfile) throws DatabaseException;


}
