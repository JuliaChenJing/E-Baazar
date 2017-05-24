package com.black.business.externalinterfaces;

import com.black.business.exceptions.BusinessException;
import com.black.business.exceptions.RuleException;

public interface RulesSubsystem {
	public void runRules(Rules rulesIface) throws BusinessException,RuleException;
	
}
