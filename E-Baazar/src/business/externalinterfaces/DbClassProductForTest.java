package business.externalinterfaces;
import java.util.List;

import middleware.exceptions.DatabaseException;
/* Used only for testing DbClassProduct */
import middleware.externalinterfaces.DbClass;

public interface DbClassProductForTest extends DbClass{
	public List<Product> readProductList(Catalog cat) throws DatabaseException;

}


