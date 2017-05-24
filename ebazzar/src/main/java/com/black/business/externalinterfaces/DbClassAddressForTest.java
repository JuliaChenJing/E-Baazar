package com.black.business.externalinterfaces;

import java.util.List;

import com.black.middleware.exceptions.DatabaseException;
import com.black.middleware.externalinterfaces.DbClass;

/* Used only for testing DbClassAddress */
public interface DbClassAddressForTest extends DbClass {
	public List<Address> readAllAddresses(CustomerProfile custProfile) throws DatabaseException;
}
