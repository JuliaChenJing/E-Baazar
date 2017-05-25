package business.externalinterfaces;

import java.util.List;

import middleware.externalinterfaces.DbClass;

/* Used only for testing DbClassShoppingCart */
public interface DbClassCartItemForTest extends DbClass {
	public List<CartItem> readCartItems(CustomerProfile custProfile) ;

}
