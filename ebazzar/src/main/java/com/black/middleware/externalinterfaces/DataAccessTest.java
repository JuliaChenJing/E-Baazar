package com.black.middleware.externalinterfaces;

import java.sql.ResultSet;

import com.black.middleware.exceptions.DatabaseException;

public interface DataAccessTest {
	public ResultSet[] multipleInstanceQueries(String[] queries, String[] dburls) throws DatabaseException;
}
