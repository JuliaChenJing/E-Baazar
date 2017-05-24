package com.black.business.externalinterfaces;
import java.util.*;

import com.black.business.exceptions.BusinessException;
import com.black.business.exceptions.RuleException;

public interface Rules  {
	String getModuleName();
	String getRulesFile();
	void prepareData();
	HashMap<String,DynamicBean> getTable();
	void runRules() throws BusinessException, RuleException;
	void populateEntities(List<String> updates);
	
	//updates are placed in a List -- object types may vary
	List<?> getUpdates();

}
