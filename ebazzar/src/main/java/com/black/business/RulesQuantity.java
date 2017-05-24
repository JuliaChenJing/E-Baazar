package com.black.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.black.business.exceptions.BusinessException;
import com.black.business.exceptions.RuleException;
import com.black.business.externalinterfaces.DynamicBean;
import com.black.business.externalinterfaces.Rules;
import com.black.business.externalinterfaces.RulesSubsystem;
import com.black.business.externalinterfaces.RulesConfigKey;
import com.black.business.externalinterfaces.RulesConfigProperties;
import com.black.business.rulesbeans.QuantityBean;
import com.black.business.rulesubsystem.RulesSubsystemFacade;

public class RulesQuantity implements Rules {

	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;
	private RulesConfigProperties config = new RulesConfigProperties();
	
	public RulesQuantity(int quantityAvail, String quantityRequested){
		bean = new QuantityBean(quantityRequested, quantityAvail);
	}
	public String getModuleName() {
		return config.getProperty(RulesConfigKey.QUANTITY_MODULE.getVal());
	}

	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.QUANTITY_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		String deftemplate = config.getProperty(RulesConfigKey.QUANTITY_DEFTEMPLATE.getVal());
		table.put(deftemplate, bean);
	}
	public void runRules() throws BusinessException, RuleException {
    	RulesSubsystem rules = new RulesSubsystemFacade();
    	rules.runRules(this);
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}

	public List<Object> getUpdates() {
		// nothing to do
		return new ArrayList<Object>();
	}

	public void populateEntities(List<String> updates) {
		// nothing to do
		
	}




}
